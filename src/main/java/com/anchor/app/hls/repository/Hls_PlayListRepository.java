package com.anchor.app.hls.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.hls.model.Hls_PlayList;

@Repository
public interface Hls_PlayListRepository extends MongoRepository<Hls_PlayList, String> {

}
