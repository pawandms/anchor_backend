package com.anchor.app.importer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.importer.model.CompanyImport;
import com.anchor.app.importer.model.PersonImport;

@Repository
public interface PersonImporterRepository extends MongoRepository<PersonImport, Integer> {
	
	/**
	 * Get Unprocessed Persons
	 * @param pageable
	 * @return
	 */
	Page<PersonImport> findAllByisProcessedIsFalse(Pageable pageable);
	
	/**
	 * Find Add CompanyImport where isProcess = false
	 * @return
	 */
	List<PersonImport> findByisProcessedIsFalse();

	

}
