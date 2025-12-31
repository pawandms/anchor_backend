package com.anchor.app.media.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.media.model.AppUser;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {
		
}
