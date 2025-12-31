package com.anchor.app.msg.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

	@Query(value = "{ '_id' : {'$in' : ?0 } }")
	List<Message> findAllByIdsIn(Iterable<String> msgIds);
	
}
