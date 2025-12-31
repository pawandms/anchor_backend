package com.anchor.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.hls.model.Hls_PlayList;
import com.anchor.app.model.hls.HlsMasterPlayList;

@Repository
public interface HlsMasterPlayListRepository extends MongoRepository<HlsMasterPlayList, String> {

	@Query(value="{ 'playList._id' : ?0 }")
	HlsMasterPlayList findByplayListId(String playListId);
}
