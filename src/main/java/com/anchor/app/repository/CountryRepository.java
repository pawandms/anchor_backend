package com.anchor.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.model.Country;

@Repository
public interface CountryRepository extends MongoRepository<Country, String>{
	
}