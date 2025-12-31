package com.anchor.app.importer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.importer.model.ImportMediaIds;

@Repository
public interface ImportMediaIdsRepository extends MongoRepository<ImportMediaIds, String> {

	 @Query(value = "{ '_id' : {'$in' : ?0 } }")
		public List<ImportMediaIds> findAllByIdsIn(Iterable<String> ids);

}
