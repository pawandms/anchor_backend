package com.anchor.app.msg.service;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.anchor.app.msg.model.UserConnection;
import com.anchor.app.msg.repository.UserConnectionRepository;
import com.anchor.app.msg.vo.FriendSuggestion;

@Service
public class FriendSuggestionService {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private UserConnectionRepository connectionRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    /**
     * Get friend suggestions based on mutual connections
     * 
     * Algorithm:
     * 1. Get all accepted friends of the user
     * 2. Get friends of those friends
     * 3. Exclude: current user, existing connections, blocked users
     * 4. Count mutual friends for each candidate
     * 5. Sort by number of mutual connections
     * 
     * @param userId Current user ID
     * @param limit Maximum number of suggestions
     * @return List of suggested users with mutual connection count
     */
    public List<FriendSuggestion> getSuggestedFriends(Long userId, int limit) {
        try {
            // Step 1: Get user's direct connections (friends)
            List<UserConnection> userConnections = connectionRepository.findAcceptedConnections(userId);
            
            if (userConnections.isEmpty()) {
                return Collections.emptyList();
            }
            
            // Extract friend IDs
            Set<Long> friendIds = extractFriendIds(userConnections, userId);
            
            // Step 2: Get friends of friends
            Map<Long, Integer> suggestedUsers = new HashMap<>();
            
            for (Long friendId : friendIds) {
                List<UserConnection> friendConnections = connectionRepository.findAcceptedConnections(friendId);
                Set<Long> friendOfFriendIds = extractFriendIds(friendConnections, friendId);
                
                // Count mutual connections
                for (Long candidateId : friendOfFriendIds) {
                    // Exclude current user and existing friends
                    if (!candidateId.equals(userId) && !friendIds.contains(candidateId)) {
                        suggestedUsers.merge(candidateId, 1, Integer::sum);
                    }
                }
            }
            
            // Step 3: Sort by mutual connection count and limit
            List<FriendSuggestion> suggestions = suggestedUsers.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> new FriendSuggestion(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
            
            return suggestions;
            
        } catch (Exception e) {
            logger.error("Error getting friend suggestions for user: " + userId, e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get friend suggestions using MongoDB Aggregation Pipeline (More efficient)
     */
    public List<FriendSuggestion> getSuggestedFriendsOptimized(Long userId, int limit) {
        try {
            // MongoDB Aggregation Pipeline approach
            Aggregation aggregation = Aggregation.newAggregation(
                // Match user's accepted connections
                Aggregation.match(Criteria.where("userId").is(userId)
                    .and("status").is(UserConnection.ConnectionStatus.ACCEPTED)),
                
                // Lookup friends of friends
                Aggregation.lookup("user_connections", "connectedUserId", "userId", "friendsOfFriends"),
                
                // Unwind friends of friends
                Aggregation.unwind("friendsOfFriends"),
                
                // Match only accepted connections
                Aggregation.match(Criteria.where("friendsOfFriends.status")
                    .is(UserConnection.ConnectionStatus.ACCEPTED)),
                
                // Group by candidate and count mutual friends
                Aggregation.group("friendsOfFriends.connectedUserId")
                    .count().as("mutualCount"),
                
                // Sort by mutual count descending
                Aggregation.sort(org.springframework.data.domain.Sort.Direction.DESC, "mutualCount"),
                
                // Limit results
                Aggregation.limit(limit)
            );
            
            AggregationResults<MutualFriendCount> results = mongoTemplate.aggregate(
                aggregation, "user_connections", MutualFriendCount.class
            );
            
            return results.getMappedResults().stream()
                .map(result -> new FriendSuggestion(result.id, result.mutualCount))
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            logger.error("Error in optimized friend suggestions for user: " + userId, e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Extract friend IDs from connections for a given user
     */
    private Set<Long> extractFriendIds(List<UserConnection> connections, Long userId) {
        return connections.stream()
            .map(conn -> conn.getUserId().equals(userId) 
                ? conn.getConnectedUserId() 
                : conn.getUserId())
            .collect(Collectors.toSet());
    }
    
    /**
     * Check if two users have mutual friends
     */
    public int getMutualFriendCount(Long userId1, Long userId2) {
        Set<Long> user1Friends = extractFriendIds(
            connectionRepository.findAcceptedConnections(userId1), userId1
        );
        Set<Long> user2Friends = extractFriendIds(
            connectionRepository.findAcceptedConnections(userId2), userId2
        );
        
        // Find intersection
        user1Friends.retainAll(user2Friends);
        return user1Friends.size();
    }
    
    // Helper class for aggregation results
    public static class MutualFriendCount {
        public Long id;
        public int mutualCount;
    }
}
