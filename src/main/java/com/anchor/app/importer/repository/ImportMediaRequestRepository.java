package com.anchor.app.importer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.importer.model.ImportMediaRequest;
import com.anchor.app.importer.model.YouTubeMedia;

@Repository
public interface ImportMediaRequestRepository extends MongoRepository<ImportMediaRequest, String> {

	Page<ImportMediaRequest> findAllByisProcessedIsFalse(Pageable pageable);
	
	/**
	 * Find  where isProcess = false
	 * @return
	 */
	List<ImportMediaRequest> findByisProcessedIsFalse();


}
