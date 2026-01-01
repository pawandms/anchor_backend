package com.anchor.app.users.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.oauth.model.UserVerifyToken;

import java.util.Optional;

@Repository
public interface UserVerifyTokenRepository extends MongoRepository<UserVerifyToken, String> {
    
    Optional<UserVerifyToken> findByVerifyToken(String verifyToken);
    
    Optional<UserVerifyToken> findByEmailAddress(String emailAddress);
    
    Optional<UserVerifyToken> findByUserName(String userName);
}
