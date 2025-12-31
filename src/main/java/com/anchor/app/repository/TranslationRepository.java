package com.anchor.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.model.Translation;

@Repository
public interface TranslationRepository extends MongoRepository<Translation, String>{
	
}