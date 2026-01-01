package com.anchor.app.users.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.oauth.model.UserAuth;

import java.util.Optional;


@Repository
public interface UserAuthRepository extends MongoRepository<UserAuth, String> {
    
    Optional<UserAuth> findById(String id);
    
    Optional<UserAuth> findByIdentifier(String identifier);
    
    boolean existsByIdentifier(String identifier);

     @Query(value = "{ 'identifier' : ?0 }")
	 public UserAuth findByUserName(String userName);
	    
}
