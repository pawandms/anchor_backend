package com.anchor.app.users.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.oauth.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUserName(String userName);
    
    Optional<User> findByMobile(String mobile);
    
    boolean existsByEmail(String email);
    
    boolean existsByUserName(String userName);
    
    boolean existsByMobile(String mobile);
}
