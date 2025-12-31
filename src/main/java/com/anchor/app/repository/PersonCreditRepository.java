package com.anchor.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.model.PersonCredit;

@Repository
public interface PersonCreditRepository extends MongoRepository<PersonCredit, String>{


	
}