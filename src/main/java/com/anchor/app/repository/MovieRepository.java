package com.anchor.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.importer.model.MovieImport;
import com.anchor.app.model.Movie;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String>{

	/**
	 * Find All Movie with Matching Tmdb ID
	 * @param id
	 * @return
	 */
	 @Query(value = "{ 'tmdbId' : {'$in' : ?0 } }")
	public List<Movie> findAllByTmdbidIn(Iterable<String> ids);

	Page<Movie> findAllByiscreditProcessedIsFalse(Pageable pageable);
	
	List<Movie> findAllByiscreditProcessedIsFalse();
	
	public Movie  findByTmdbId(String id);
 
	
}