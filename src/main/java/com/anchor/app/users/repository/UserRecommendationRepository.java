package com.anchor.app.users.repository;

import com.anchor.app.users.model.UserRecommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRecommendationRepository extends MongoRepository<UserRecommendation, String> {

    List<UserRecommendation> findByUserIdOrderByScoreDesc(String userId);

    void deleteByUserId(String userId);

    boolean existsByUserIdAndRecommendedUserId(String userId, String recommendedUserId);
}
