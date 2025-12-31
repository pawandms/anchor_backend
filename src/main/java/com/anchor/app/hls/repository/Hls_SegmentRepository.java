package com.anchor.app.hls.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.hls.model.Hls_PlayList;
import com.anchor.app.hls.model.Hls_Segment;

@Repository
public interface Hls_SegmentRepository extends MongoRepository<Hls_Segment, String> {
	
	@DeleteQuery
	Long deleteByMediaId(String mediaID);     

}
