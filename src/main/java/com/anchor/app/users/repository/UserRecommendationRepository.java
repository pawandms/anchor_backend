package com.anchor.app.users.repository;

import com.anchor.app.users.model.UserRecommendation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecommendationRepository extends MongoRepository<UserRecommendation, String> {

    Page<UserRecommendation> findByUserId(String userId, Pageable pageable);

    void deleteByUserId(String userId);

    boolean existsByUserIdAndRecommendedUserId(String userId, String recommendedUserId);
}
