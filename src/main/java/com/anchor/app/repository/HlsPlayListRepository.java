package com.anchor.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.model.hls.HlsPlayList;

@Repository
public interface HlsPlayListRepository extends MongoRepository<HlsPlayList, String> {

}
