package com.anchor.app.importer.service.Impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.exception.ImageException;
import com.anchor.app.exception.ImageServiceException;
import com.anchor.app.exception.ImporterException;
import com.anchor.app.exception.PeopleException;
import com.anchor.app.exception.SequencerException;
import com.anchor.app.importer.model.GenreImport;
import com.anchor.app.importer.model.PersonImport;
import com.anchor.app.importer.model.TmdbPerson;
import com.anchor.app.importer.repository.PersonImporterRepository;
import com.anchor.app.importer.service.ImageImporterService;
import com.anchor.app.importer.service.PersonImporterService;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.media.model.Person;
import com.anchor.app.media.service.PeopleService;
import com.anchor.app.model.Genre;
import com.anchor.app.model.Image;
import com.anchor.app.repository.GenreRepository;
import com.anchor.app.service.ImageService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class PersonImporterServiceImpl implements PersonImporterService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private HelperBean helper;
	
	@Autowired
	private PersonImporterRepository piRep;

	@Autowired
	private ImageImporterService imgImportService;

	
	@Autowired
	private ImageService imgService;

	@Autowired
	private PeopleService peoService;
	
	@Autowired
	private EnvProp env;

	@Autowired
	private GenreRepository genreRepo;
	
	
	@Override
	public int importPersonData() throws ImporterException {
		int result = 0;
		
		//Save Json File Record to DB before Processing
		//saveImportPersonEntity(path);
		
		// Get Total Pages to Process
		Page page =  getTotalPersonImportPage();
		
		int startPageIndex = 0;
		int endPageIndex = page.getTotalPages(); 
		int itemCount = page.getNumberOfElements();
		int totalCount = (int) page.getTotalElements();

		// Process by item
		if (totalCount <= 2000)
		{
			processImportedPersonItem();
		}
		// Process by Page
		else {
			// Process PersonImport Collection
			for (int i = 0 ; i<= endPageIndex ; i++)
			{
				
				int batch_result = processImportedPerson(i);	
				result = result + batch_result;
				int remining = totalCount - result;
				logger.info("People Processing Batch:"+i+" , Total Process:"+result+" , Remaining:"+remining);
			}
				
		}
		
		
		
		return result;
		
	}
	
	/**
	 * Save ImportPerson Entity to DB by Processing JSON File
	 * @param path
	 * @return
	 * @throws ImporterException
	 */
	private int saveImportPersonEntity(String path) throws ImporterException
	{
		int result = 0;
		int batch_size = env.getPerson_import_size();
		List<PersonImport> pilist = new ArrayList<PersonImport>();
		try {
			if (( null == path) || (path.equalsIgnoreCase("")))
			{
				throw new ImporterException("Path can not be null or empty");
			}
		
			  File jsonFile = new File(path);
			  
			  LineIterator fileContents= FileUtils.lineIterator(jsonFile, StandardCharsets.UTF_8.name());
			  int i = 0;
			  while(fileContents.hasNext()){
				  String personTxt = fileContents.nextLine();
				  
				 PersonImport pri =   helper.convertPersonImportToObject(personTxt);
				 pilist.add(pri);
		         i++;
		         result++;
		         
		         if(i >= batch_size)
		         {
		        	 savePeronImport(pilist);
		        	 //Reset Counter
		        	 i = 0;
		         }
		         
		       }
			  if(!pilist.isEmpty())
			  {
				  savePeronImport(pilist);
				  
			  }
		}
		catch(IOException e)
		{
			throw new ImporterException(e.getMessage(), e);
		}
		
		
		return result;
	}
	
	private void savePeronImport(List<PersonImport> pilist)
	{
		// Save all Unprocess Records to DB
		piRep.saveAll(pilist);
		// Clear the List
		pilist.clear();
	}
	
	/**
	 * Get Total PersonImport Pages as per BatchSize
	 * @return
	 */
	private Page getTotalPersonImportPage()
	{
		int pageNo = 0;
		int size = env.getPerson_import_size();
		Pageable requestedPage = PageRequest.of(pageNo, size);
		
		Page<PersonImport> page = piRep.findAllByisProcessedIsFalse(requestedPage);
	
		return page;
	}
	
	/**
	 * Import Person Data from TMDB to local DB by Per Page
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	
	private int processImportedPerson(int pageIndex) throws ImporterException 
	{
		int result = 0;
		try {
			int pageNo = pageIndex;
			int size = env.getPerson_import_size();
			Pageable requestedPage = PageRequest.of(pageNo, size);
			
			Page<PersonImport> pri = piRep.findAllByisProcessedIsFalse(requestedPage);
			
			
			List<PersonImport> priList = pri.getContent();
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					processImportPerson(i);
					
				} catch (ImporterException e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId(), e);
				}
			})).get();
			
			result = pri.getNumberOfElements();
			piRep.saveAll(priList);
			
		}
		catch(InterruptedException | ExecutionException e)
		{
			throw new ImporterException(e.getMessage(), e);
		}
		
		return result;
	
	}
	
	private int processImportedPersonItem() throws ImporterException 
	{
		int result = 0;
		try {
			List<PersonImport> priList = piRep.findByisProcessedIsFalse();
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					processImportPerson(i);
					
				} catch (ImporterException e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId());
				}
			})).get();
			
			result = priList.size();
			//piRep.saveAll(priList);
			
		}
		catch(InterruptedException | ExecutionException e)
		{
			throw new ImporterException(e.getMessage(), e);
		}
		
		return result;
	
	}
	
	private void getTotalPageforPersonImport()
	{
		int page = 0;
		int size = 40;
		Pageable requestedPage = PageRequest.of(page, size);
		
		Page<PersonImport> pri = piRep.findAllByisProcessedIsFalse(requestedPage);
		int totalPages = pri.getTotalPages();
		int totalElements =	(int) pri.getTotalElements();
	}
	
	private void processImportPerson(PersonImport pri) throws ImporterException
	{
	// Get Respective Person Details from TMDB Rest API
		TmdbPerson tmperson = getPersonFromMovieDB(pri);	
	// Store Person to Local DB
		StorePersontoDB(pri, tmperson);
		
		
	}
	
	private TmdbPerson getPersonFromMovieDB(PersonImport pri) throws ImporterException
	{
		 TmdbPerson tmperson =null;
		 try {
			 final String url = env.getMovieDB_URL()+"person/{id}";
			  HttpHeaders headers = new HttpHeaders();
			  headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			  Map<String, String> params = new HashMap<String, String>();
			    params.put("id", String.valueOf(pri.getId())); 
			    
			  URI uri = UriComponentsBuilder.fromUriString(url)
				        .buildAndExpand(params)
				        .toUri();
				uri = UriComponentsBuilder
				        .fromUri(uri)
				        .queryParam("api_key", env.getMovieDB_apiKey())
				        .build()
				        .toUri();
			  
		    RestTemplate restTemplate = new RestTemplate();
		    
		     HttpEntity<String> entity = new HttpEntity<String>(headers);
		    
		     /*
		     HttpEntity<String> response = restTemplate.exchange(
		    	        uri, 
		    	        HttpMethod.GET, 
		    	        entity, 
		    	        String.class);
		     
		     */
		     
		     tmperson = restTemplate.getForObject(uri, TmdbPerson.class);
		 
		 }
		 catch(Exception e)
		 {
			 throw new ImporterException("Exception in getPerson From MDB with ID :"+pri.getId()+", Msg:"+e.getMessage(), e);
		 }
		     
	     return tmperson;
	}

	
	@Async
	private TmdbPerson getPersonFromMovieDB(int id) throws ImporterException
	{
		 TmdbPerson tmperson =null;
		 try {
			 final String url = env.getMovieDB_URL()+"person/{id}";
			  HttpHeaders headers = new HttpHeaders();
			  headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			  Map<String, String> params = new HashMap<String, String>();
			    params.put("id", String.valueOf(id)); 
			    
			  URI uri = UriComponentsBuilder.fromUriString(url)
				        .buildAndExpand(params)
				        .toUri();
				uri = UriComponentsBuilder
				        .fromUri(uri)
				        .queryParam("api_key", env.getMovieDB_apiKey())
				        .build()
				        .toUri();
			  
		    RestTemplate restTemplate = new RestTemplate();
		    
		     HttpEntity<String> entity = new HttpEntity<String>(headers);
		    
		     /*
		     HttpEntity<String> response = restTemplate.exchange(
		    	        uri, 
		    	        HttpMethod.GET, 
		    	        entity, 
		    	        String.class);
		     
		     */
		     
		     tmperson = restTemplate.getForObject(uri, TmdbPerson.class);
		 
		 }
		 catch(Exception e)
		 {
			 throw new ImporterException("Exception in getPerson From MDB with ID :"+id+", Msg:"+e.getMessage(), e);
		 }
		     
	     return tmperson;
	}
	
	private void StorePersontoDB(PersonImport pri, TmdbPerson tmperson) throws ImporterException
	{
		 try {
		
			 if(null != tmperson)
				{
					Person person = helper.convertTmdbPerson_to_Person(tmperson);
					if( null != tmperson.getProfile_path())
					{
						String imgName = tmperson.getProfile_path().replace("/", "");
						InputStream stream = imgImportService.getImageStreamFromMovieDB(tmperson.getProfile_path());
						if ( null != stream)
						{
							//MediaImage img = imgService.addImage(EntityType.People, ImageType.PersonImage, person.getId(), imgName, stream);
							MediaImage img = imgService.addImage(EntityType.Person, ImageType.PersonImage, person.getId(), imgName, 
									0,
									0, stream);
							person.setImage_id(img.getId());
								
						}
						
							
					}
					peoService.addPeople(person, true);
					
					// Mark Import as Complete
					pri.setProcessed(true);
					piRep.save(pri);
					
				}
			 
		 }
		 catch(SequencerException | PeopleException | ImageServiceException e)
		 {
			 throw new ImporterException(e.getMessage(), e);
		 }
		
	}

		private Person StorePersontoDB(TmdbPerson tmperson) throws ImporterException
		{
			Person result = null;
			 try {
			
				 if(null != tmperson)
					{
						Person person = helper.convertTmdbPerson_to_Person(tmperson);
						if( null != tmperson.getProfile_path())
						{
							String imgName = tmperson.getProfile_path().replace("/", "");
							InputStream stream =  imgImportService.getImageStreamFromMovieDB(tmperson.getProfile_path());
							if ( null != stream)
							{
								MediaImage img = imgService.addImage(EntityType.Person, ImageType.PersonImage, person.getId(), imgName, stream);
								person.setImage_id(img.getId());
									
							}
								
						}
						result = peoService.addPeople(person, true);
						
						
					}
				 
				 
			 }
			 catch(SequencerException |  PeopleException | ImageServiceException e)
			 {
				 throw new ImporterException(e.getMessage(), e);
			 }
			return result;
		}
 
	 
	/**
	 * Get Image From MovieDB 
	 * @param id
	 * @throws ImporterException 
	 * @throws IOException 
	 */
	 @Async
	private InputStream getImageFromMovieDB(String id) throws ImporterException 
	{
		InputStream input =null;
		try {
			final String url = env.getMovieDB_Image_URL()+"{id}";
			  HttpHeaders headers = new HttpHeaders();
			  headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			  Map<String, String> params = new HashMap<String, String>();
			    params.put("id", id); 
			    
			  URI uri = UriComponentsBuilder.fromUriString(url)
				        .buildAndExpand(params)
				        .toUri();
				uri = UriComponentsBuilder
				        .fromUri(uri)
				        .queryParam("api_key", env.getMovieDB_apiKey())
				        .build()
				        .toUri();
			  
		    RestTemplate restTemplate = new RestTemplate();
		    
		     HttpEntity<String> entity = new HttpEntity<String>(headers);
		    
		     
		     ResponseEntity<Resource> responseEntity  = restTemplate.exchange(
		    	        uri, 
		    	        HttpMethod.GET, 
		    	        entity, 
		    	        Resource.class);
		     
		     input = responseEntity.getBody().getInputStream();
		    
			
		}
		catch(IOException e)
		{
			// Swolling exception as if Image is not available then mark it as null
			//throw new ImporterException("Exception in getImage From MDB with ID :"+id+", Msg:"+e.getMessage(), e);
		}
		 
	    return input;
	}

	@Override
	public int importGenreData() throws ImporterException {
		int result = 0;
		List<Genre> genreList = getGenreFromMovieDB();
		genreRepo.saveAll(genreList);
		result = genreList.size();
		return result;
		
	}

	@Async
	private List<Genre> getGenreFromMovieDB() throws ImporterException
	{
		 List<Genre> genreList =null;
		 try {
			 final String url = env.getMovieDB_URL()+"genre/movie/list";
			  HttpHeaders headers = new HttpHeaders();
			  headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			  Map<String, String> params = new HashMap<String, String>();
			  //  params.put("id", String.valueOf(pri.getId())); 
			    
			  URI uri = UriComponentsBuilder.fromUriString(url)
				        .buildAndExpand(params)
				        .toUri();
				uri = UriComponentsBuilder
				        .fromUri(uri)
				        .queryParam("api_key", env.getMovieDB_apiKey())
				        .build()
				        .toUri();
			  
		    RestTemplate restTemplate = new RestTemplate();
		    
		     HttpEntity<String> entity = new HttpEntity<String>(headers);
	
		     ResponseEntity<GenreImport> response  = restTemplate.exchange(
		    	        uri, 
		    	        HttpMethod.GET, 
		    	        entity, 
		    	        GenreImport.class);
		     
		     // Convert Genre to GenreList 
		     genreList = helper.convertGenreImport_to_list(response.getBody());
		     
		 }
		 catch(Exception e)
		 {
			throw new ImporterException(e.getMessage(), e); 
		 }
		     
	     return genreList;
	}

	@Override
	public Person importPerson(int id) throws ImporterException {
		
		Person per = null;
		if(id <= 0)
		{
			throw new ImporterException("Id can be positive number only");
		}
		TmdbPerson tper = getPersonFromMovieDB(id);
		per =  StorePersontoDB(tper);
		return per;
	}


}
