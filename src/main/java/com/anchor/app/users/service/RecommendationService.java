package com.anchor.app.users.service;

import com.anchor.app.msg.enums.ChannelSubType;
import com.anchor.app.msg.enums.ContactType;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.model.ChannelMember;
import com.anchor.app.msg.model.UserContact;
import com.anchor.app.msg.repository.ChannelMemberRepository;
import com.anchor.app.msg.repository.ChannelRepository;
import com.anchor.app.oauth.dto.AuthReq;
import com.anchor.app.oauth.enums.PermissionType;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.users.dto.RecommendationProfile;
import com.anchor.app.users.model.UserRecommendation;
import com.anchor.app.users.repository.UserRecommendationRepository;
import com.anchor.app.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository memberRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthService authService;

    /**
     * Get stored recommendations for a user.
     * 
     * @param request DTO to fill with results and validation status
     */
    public void getRecommendations(RecommendationProfile request) {
        try {
            String userId = request.getId();
            log.info("Fetching stored recommendations for user: {}", userId);

            // Authorization check
            AuthReq authReq = new AuthReq(null, userId, PermissionType.UsrView);
            if (!authService.hasPersmission(authReq)) {
                request.setValid(false);
                request.setErrorCode("AUTHORIZATION_FAILED");
                request.setErrorMessage("Invalid Permission to view recommendations for user: " + userId);
                return;
            }

            List<UserRecommendation> recommendations = recommendationRepository.findByUserIdOrderByScoreDesc(userId);
            request.setRecommendations(recommendations);
            request.setValid(true);

        } catch (Exception e) {
            log.error("Error fetching recommendations: {}", e.getMessage(), e);
            request.setValid(false);
            request.setErrorMessage("Internal error: " + e.getMessage());
        }
    }

    /**
     * Generate and refresh recommendations for a user.
     */
    public void refreshRecommendations(RecommendationProfile request) {
        try {
            String userId = request.getId();
            log.info("Refreshing recommendations for user: {}", userId);

            // Authorization check
            AuthReq authReq = new AuthReq(null, userId, PermissionType.UsrEdit);
            if (!authService.hasPersmission(authReq)) {
                request.setValid(false);
                request.setErrorCode("AUTHORIZATION_FAILED");
                request.setErrorMessage("Invalid Permission to refresh recommendations for user: " + userId);
                return;
            }

            // Logic to calculate recommendations...
            performRefresh(userId);

            request.setValid(true);
            log.info("Successfully refreshed recommendations for user: {}", userId);

        } catch (Exception e) {
            log.error("Error refreshing recommendations: {}", e.getMessage(), e);
            request.setValid(false);
            request.setErrorMessage("Internal error: " + e.getMessage());
        }
    }

    private void performRefresh(String userId) {
        // 1. Get current friends
        List<String> friendIds = getFriendIds(userId);
        Set<String> excludedIds = new HashSet<>(friendIds);
        excludedIds.add(userId);

        // 2. Get user's groups (circles)
        List<String> userChannelIds = memberRepository.findByUserId(userId).stream()
                .map(ChannelMember::getChannelId)
                .collect(Collectors.toList());

        // FIX: Explicitly ignore channels with subType: Self
        List<String> groupChannelIds = channelRepository.findAllById(userChannelIds).stream()
                .filter(c -> ChannelSubType.GROUP.equals(c.getSubType()))
                .filter(c -> !ChannelSubType.Self.equals(c.getSubType()))
                .map(Channel::getId)
                .collect(Collectors.toList());

        Map<String, RecommendationData> potentialRecommendations = new HashMap<>();

        // 3. Find Friends of Friends
        for (String friendId : friendIds) {
            List<String> fofIds = getFriendIds(friendId);
            for (String fofId : fofIds) {
                if (!excludedIds.contains(fofId)) {
                    potentialRecommendations.computeIfAbsent(fofId, k -> new RecommendationData())
                            .addMutualFriend(friendId);
                }
            }
        }

        // 4. Find users in same groups (circles)
        for (String channelId : groupChannelIds) {
            List<ChannelMember> members = memberRepository.findByChannelId(channelId);
            for (ChannelMember member : members) {
                String memberId = member.getUserId();
                if (!excludedIds.contains(memberId)) {
                    potentialRecommendations.computeIfAbsent(memberId, k -> new RecommendationData())
                            .addMutualChannel(channelId);
                }
            }
        }

        // 5. Cold Start Strategy (Geo-Location)
        // If we have few recommendations, look for nearby users
        if (potentialRecommendations.size() < 20) {
            log.info("Applying cold start proximity search for user: {}", userId);
            userRepository.findById(userId).ifPresent(currentUser -> {
                if (currentUser.getLocation() != null && currentUser.getLocation().length == 2) {
                    double longitude = currentUser.getLocation()[0];
                    double latitude = currentUser.getLocation()[1];

                    // Find users within 50km radius
                    // MongoDB uses radians for distance in $nearSphere: distance / 6378.1
                    double radiusInRadians = 50.0 / 6378.1;

                    Query proximityQuery = new Query(Criteria.where("location")
                            .nearSphere(new org.springframework.data.geo.Point(longitude, latitude))
                            .maxDistance(radiusInRadians)
                            .and("_id").nin(excludedIds));
                    proximityQuery.limit(50);

                    List<User> nearbyUsers = mongoTemplate.find(proximityQuery, User.class);
                    for (User nearbyUser : nearbyUsers) {
                        if (!potentialRecommendations.containsKey(nearbyUser.getId())) {
                            potentialRecommendations.computeIfAbsent(nearbyUser.getId(), k -> new RecommendationData())
                                    .setNearby(true);
                        }
                    }
                }
            });
        }

        // 6. Save results
        recommendationRepository.deleteByUserId(userId);

        List<UserRecommendation> recommendations = new ArrayList<>();
        for (Map.Entry<String, RecommendationData> entry : potentialRecommendations.entrySet()) {
            String recUserId = entry.getKey();
            RecommendationData data = entry.getValue();

            Optional<User> userOpt = userRepository.findById(recUserId);
            if (userOpt.isPresent()) {
                User recUser = userOpt.get();

                UserRecommendation rec = UserRecommendation.builder()
                        .userId(userId)
                        .recommendedUserId(recUserId)
                        .firstName(recUser.getFirstName())
                        .lastName(recUser.getLastName())
                        .face(recUser.getFace())
                        .description(generateDescription(data))
                        .mutualFriendIds(new ArrayList<>(data.mutualFriendIds))
                        .mutualChannelIds(new ArrayList<>(data.mutualChannelIds))
                        .score(calculateScore(data))
                        .createdAt(new Date())
                        .build();

                recommendations.add(rec);
            }
        }

        // Sort and limit
        recommendations.sort(Comparator.comparing(UserRecommendation::getScore).reversed());
        List<UserRecommendation> topRecs = recommendations.stream().limit(50).collect(Collectors.toList());

        recommendationRepository.saveAll(topRecs);
        log.info("Saved {} recommendations for user: {}", topRecs.size(), userId);
    }

    private List<String> getFriendIds(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId).and("contactType").is(ContactType.FRIEND));
        return mongoTemplate.find(query, UserContact.class).stream()
                .map(UserContact::getContactId)
                .collect(Collectors.toList());
    }

    private Double calculateScore(RecommendationData data) {
        double score = (data.mutualFriendIds.size() * 10.0) + (data.mutualChannelIds.size() * 5.0);
        if (data.isNearby) {
            score += 3.0; // Bonus for being nearby
        }
        return score;
    }

    private String generateDescription(RecommendationData data) {
        if (!data.mutualFriendIds.isEmpty()) {
            return data.mutualFriendIds.size() + " mutual friends";
        } else if (!data.mutualChannelIds.isEmpty()) {
            return "In " + data.mutualChannelIds.size() + " of your circles";
        } else if (data.isNearby) {
            return "Nearby user";
        }
        return "Suggested for you";
    }

    private static class RecommendationData {
        Set<String> mutualFriendIds = new HashSet<>();
        Set<String> mutualChannelIds = new HashSet<>();
        boolean isNearby = false;

        void addMutualFriend(String id) {
            mutualFriendIds.add(id);
        }

        void addMutualChannel(String id) {
            mutualChannelIds.add(id);
        }

        void setNearby(boolean nearby) {
            isNearby = nearby;
        }
    }
}
