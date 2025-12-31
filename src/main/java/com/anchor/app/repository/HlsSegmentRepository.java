package com.anchor.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.model.hls.HlsSegment;

@Repository
public interface HlsSegmentRepository  extends MongoRepository<HlsSegment, String>{

	@Query(value="{ 'playListId' : ?0 }",fields="{ '_id' : 1, 'movieId' : 1, 'playListId' : 1, 'sequenceNo' : 1"
			+ ", 'duration' : 1 }")
	List<HlsSegment> findByplayListId(String playListID);
}
