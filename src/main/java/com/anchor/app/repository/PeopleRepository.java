package com.anchor.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.media.model.Person;

@Repository
public interface PeopleRepository extends MongoRepository<Person, String> {
	
	@Query(value="{ 'name' : ?0 }")
	Person findByName(String name);

	Page<Person> findAll(Pageable pageable);
	
	Page<Person> findByNameLikeOrOriginalnameLike(String name, String original_name, Pageable pageable);
	
	 @Query(value = "{ 'tmdb_id' : {'$in' : ?0 } }")
	public List<Person> findAllByTmdbidIn(Iterable<String> ids);

}
