package com.anchor.app.repository.Impl;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.anchor.app.model.Company;
import com.anchor.app.repository.CompanyRepositoryCustome;

public class CompanyRepositoryCustomeImpl implements CompanyRepositoryCustome {

	@Override
	 @Query(value = "{ 'tmdb_id' : {'$in' : ?0 } }")
	 public List<Company> getAllByTmdbids(List<String> tmdb_ids) {
		// TODO Auto-generated method stub
		return null;
	}

}
