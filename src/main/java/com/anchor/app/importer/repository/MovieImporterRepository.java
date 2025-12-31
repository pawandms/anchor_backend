package com.anchor.app.importer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.importer.model.CompanyImport;
import com.anchor.app.importer.model.MovieImport;

@Repository
public interface MovieImporterRepository extends MongoRepository<MovieImport, Integer> {
	
	/**
	 * Get Unprocessed Persons
	 * @param pageable
	 * @return
	 */
	Page<MovieImport> findAllByisProcessedIsFalse(Pageable pageable);

	/**
	 * Find Add CompanyImport where isProcess = false
	 * @return
	 */
	List<MovieImport> findByisProcessedIsFalse();

}
