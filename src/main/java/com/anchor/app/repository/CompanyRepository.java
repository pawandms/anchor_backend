package com.anchor.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.importer.model.CompanyImport;
import com.anchor.app.model.Company;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

	/**
	 * Get All Companies where Parent Company Details are Present
	 * @return
	 */
	public List<Company> findByisChildIsTrue();
	
	/**
	 * Find All Company withc Matching Tmdb ID
	 * @param id
	 * @return
	 */
	 @Query(value = "{ 'tmdb_id' : {'$in' : ?0 } }")
	public List<Company> findAllByTmdbidIn(Iterable<String> ids);

	 @Query(value = "{ 'tmdb_id' : ?0 }")
	 public Company findByTmdbid(String id);
	 
	
}
