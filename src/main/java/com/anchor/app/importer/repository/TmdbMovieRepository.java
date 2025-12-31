package com.anchor.app.importer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.importer.model.TmdbMovie;

@Repository
public interface TmdbMovieRepository extends MongoRepository<TmdbMovie, String> {

}
