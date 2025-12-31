package com.anchor.app.importer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.importer.model.CountryImport;

@Repository
public interface CountryImporterRepository extends MongoRepository<CountryImport, String> {

}
