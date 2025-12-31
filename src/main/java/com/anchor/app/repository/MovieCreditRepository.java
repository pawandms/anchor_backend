package com.anchor.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.model.MovieCredit;

@Repository
public interface MovieCreditRepository extends MongoRepository<MovieCredit, String>{


	
}