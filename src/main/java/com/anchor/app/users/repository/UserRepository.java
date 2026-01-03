package com.anchor.app.users.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import com.anchor.app.oauth.model.User;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUserName(String userName);
    
    Optional<User> findByMobile(String mobile);
    
    boolean existsByEmail(String email);
    
    boolean existsByUserName(String userName);
    
    boolean existsByMobile(String mobile);
    
    /**
     * Update profileImageMediaId for a specific user
     * 
     * @param userId User ID
     * @param mediaId Media ID to set
     * @param modUser User who modified
     * @param modDate Modification date
     */
    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'profileImageMediaId': ?1, 'modUser': ?2, 'modDate': ?3 } }")
    void updateProfileImageMediaId(String userId, String mediaId, String modUser, Date modDate);
}
