package com.anchor.app.sequencer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.sequencer.model.Sequance;



@Repository
public interface SequanceRepository extends MongoRepository<Sequance, String> {

	@Query(value="{ 'name' : ?0 }")
	Sequance findByName(String name);

}
