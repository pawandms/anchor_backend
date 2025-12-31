package com.anchor.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.anchor.app.model.Company;

public interface CompanyRepositoryCustome {
	
	@Query(value = "{ 'tmdb_id' : {'$in' : ?0 } }")
	List<Company> getAllByTmdbids(List<String> tmdb_ids);

}
