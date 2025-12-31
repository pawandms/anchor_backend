package com.anchor.app.importer.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anchor.app.importer.model.PersonImport;
import com.anchor.app.importer.repository.PersonImporterRepository;
import com.anchor.app.importer.service.CollectionImporterService;
import com.anchor.app.importer.service.CompanyImporterService;
import com.anchor.app.importer.service.CountryImporterService;
import com.anchor.app.importer.service.LanguageImporterService;
import com.anchor.app.importer.service.MovieCreditImporterService;
import com.anchor.app.importer.service.MovieImporterService;
import com.anchor.app.importer.service.PersonImporterService;
import com.anchor.app.media.model.Person;
import com.anchor.app.model.Customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/import")
public class DataImporterController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PersonImporterService perImportService;
	
	@Autowired
	private CompanyImporterService compImportService;

	@Autowired
	private MovieImporterService movieImportService;

	@Autowired
	private  CollectionImporterService collectionImportService;
	
	@Autowired
	private PersonImporterRepository piRep;

	@Autowired
	private LanguageImporterService lanImpService; 

	@Autowired
	private CountryImporterService conImpService;

	@Autowired
	private MovieCreditImporterService mcImpServiec;
	
	@RequestMapping(value = "/person", method = RequestMethod.GET)
    public ResponseEntity<?> loadPerson() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	int count = perImportService.importPersonData();	
	    	response =  new ResponseEntity<>("Person Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	
	@RequestMapping(value = "/genre", method = RequestMethod.GET)
    public ResponseEntity<?> loadGenre() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	int count = perImportService.importGenreData();	
	    	response =  new ResponseEntity<>("Genre Load Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }


	@RequestMapping(value = "/company", method = RequestMethod.GET)
    public ResponseEntity<?> loadCompany(@Valid @NotBlank @RequestParam(required = true) String path) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	int count = compImportService.importCompanyData(path);	
	    	response =  new ResponseEntity<>("Person Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	@RequestMapping(value = "/movie", method = RequestMethod.GET)
    public ResponseEntity<?> loadMovie(@Valid @NotBlank @RequestParam(required = true) String path) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	int count = movieImportService.importMovieData(path);	
	    	response =  new ResponseEntity<>("Movie Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }

	
	@RequestMapping(value = "/movieCollection", method = RequestMethod.GET)
    public ResponseEntity<?> loadMovieCollection(@Valid @NotBlank @RequestParam(required = true) String path) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	int count = collectionImportService.importMovieCollectionData(path);	
	    	response =  new ResponseEntity<>("Movie Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	@RequestMapping(value = "/movieCredit", method = RequestMethod.GET)
    public ResponseEntity<?> loadMovieCredits() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	int count = mcImpServiec.importMovieCreditData();	
	    	response =  new ResponseEntity<>("Movie Credit Processed Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }

	
	@RequestMapping(value = "/language", method = RequestMethod.GET)
    public ResponseEntity<?> loadLanguage() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	int count = lanImpService.importLanguageData();	
	    	response =  new ResponseEntity<>("Language Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	@RequestMapping(value = "/country", method = RequestMethod.GET)
    public ResponseEntity<?> loadCountry() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	int count = conImpService.importCountryData();	
	    	response =  new ResponseEntity<>("Country Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	@GetMapping("/person/pendingPerson")
	public Slice<PersonImport> retrieveUnprocessPerson(@Param(value = "page") int page, 
													   @Param(value = "size") int size)
	{
		Pageable requestedPage = PageRequest.of(page, size);
		Page<PersonImport> persons  = piRep.findAllByisProcessedIsFalse(requestedPage);
		return persons;
	}

	@RequestMapping(value = "/singlePerson/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPerson(@Valid @NotBlank @PathVariable(required = true) int id) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	Person per = perImportService.importPerson(id);	
	    	response =  new ResponseEntity<>(per, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	

}
