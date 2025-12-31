package com.anchor.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.model.Language;

@Repository
public interface LanguageRepository extends MongoRepository<Language, String>{
	
}