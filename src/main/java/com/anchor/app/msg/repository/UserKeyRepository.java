package com.anchor.app.msg.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.UserKey;

@Repository
public interface UserKeyRepository extends MongoRepository<UserKey, String> {
    
    Optional<UserKey> findByUserId(String userId);
    
    Optional<UserKey> findByUserIdAndDeviceId(String userId, String deviceId);
    
    boolean existsByUserId(String userId);
    
    void deleteByUserId(String userId);
}
