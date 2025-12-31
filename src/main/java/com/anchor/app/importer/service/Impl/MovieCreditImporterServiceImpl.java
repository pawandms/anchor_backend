package com.anchor.app.importer.service.Impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.anchor.app.exception.ImporterException;
import com.anchor.app.exception.SequencerException;
import com.anchor.app.importer.model.TmdbCast;
import com.anchor.app.importer.model.TmdbCrew;
import com.anchor.app.importer.model.TmdbMovieCredit;
import com.anchor.app.importer.service.MovieCreditImporterService;
import com.anchor.app.media.model.Person;
import com.anchor.app.model.Movie;
import com.anchor.app.model.MovieCredit;
import com.anchor.app.model.PersonCredit;
import com.anchor.app.repository.MovieCreditRepository;
import com.anchor.app.repository.MovieRepository;
import com.anchor.app.repository.PeopleRepository;
import com.anchor.app.repository.PersonCreditRepository;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class MovieCreditImporterServiceImpl implements MovieCreditImporterService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private HelperBean helper;

	@Autowired
	private MovieRepository movRep;

	@Autowired
	private PeopleRepository peopleRep;

	@Autowired
	private MovieCreditRepository movCreditRep;
	
	@Autowired
	private PersonCreditRepository perCreditRep;

	
	@Override
	public int importMovieCreditData() throws ImporterException {
	
		int result = 0;
		try {
			// Import CompanyData 
			result =  processMovieCreditData(); 
			
			
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
			
		
	
		
		return result;

		
	}
	
	private int processMovieCreditData() throws ImporterException
	{
		int result = 0;
		try {
			
			// Get Total Pages to Process
			Page page =  getTotalMovieCreditProcessingPage();
			
			int startPageIndex = 0;
			int endPageIndex = page.getTotalPages(); 
			int itemCount = page.getNumberOfElements();
			int totalCount = (int) page.getTotalElements();
			
			// Process by item
			if (totalCount <= 2000)
			{
				result = processMovieCreditItem();
			}
			// Process by Page
			else {
				// Process PersonImport Collection
				for (int i = 0 ; i<= endPageIndex ; i++)
				{
					
					int batch_result = processMovieCredit(i);	
					result = result + batch_result;
					int remining = totalCount - result;
					logger.info("Movie Credit Processing Batch:"+i+" , Total Process:"+result+" , Remaining:"+remining);
				}
					
			}
			
			result = totalCount;
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
		
		return result;
			
	}

	private int processMovieCredit(int pageIndex) throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			int pageNo = pageIndex;
			int size = env.getPerson_import_size();
			Pageable requestedPage = PageRequest.of(pageNo, size);
			
			Page<Movie> pri = movRep.findAllByiscreditProcessedIsFalse(requestedPage);
			
			List<Movie> priList = pri.getContent();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					// Import Movie Credit Detials from TMDB
					TmdbMovieCredit tmov = processImportMovieCredit(i);
					storeMovieCreditToDb(tmov, processDate);
					i.setIscreditProcessed(true);
					movRep.save(i);
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId(), e);
				}
			})).get();
			
			
			result = priList.size();
			
		}
		catch(InterruptedException | ExecutionException e)
		{
			throw new ImporterException(e.getMessage(), e);
		}
		
		return result;
	
	}

	
	private int processMovieCreditItem() throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			List<Movie> priList = movRep.findAllByiscreditProcessedIsFalse();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
				
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					// Import Movie Credit Detials from TMDB
					TmdbMovieCredit tmov = processImportMovieCredit(i);
					storeMovieCreditToDb(tmov, processDate);
					i.setIscreditProcessed(true);
					movRep.save(i);
					
					logger.info("Movie Credit Processed with ID:"+i.getId());
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId(), e);
				}
			})).get();
			
			
			
			result = priList.size();
			
		}
		catch(InterruptedException | ExecutionException e)
		{
			throw new ImporterException(e.getMessage(), e);
		}
		
		return result;
	
	}
	
	
	@Transactional
	private void storeMovieCreditToDb(TmdbMovieCredit tmov, Date processDate) throws ImporterException
	{
		try {
			// Get Movie by TmdbID
			 Movie mov = movRep.findByTmdbId(tmov.getTmdbId());
			
			 if ( null != mov)
			{
				// Extract People ID from  Respective Movie Credit Cast & Crew
				 Map<String, Person> personMap =  getUniquePerson_from_TmdbMovieCredit(tmov);
				
				 //Convert TmdbMovieCredit to MovieCredit
				 MovieCredit mc = helper.converttmdbMovieCredit_to_MovieCredit(tmov,mov, personMap, processDate); 
				  
				 // Convert MovieCredit Cast & Crew to PersonCredi Record
				 List<PersonCredit> personCreditList = helper.convertMovieCredit_to_PersonCredit(mc,mov);
				
				 movCreditRep.save(mc);
				 
				 perCreditRep.saveAll(personCreditList);
				 
		
			}
			
	
		}
		catch(Exception e)
		{
		throw new ImporterException("Exception in Storign Movie Credit for :"+tmov.getTmdbId()+" , ERROR"+e.getMessage());		
		}
		 				
		
	}
	

	/**
	 * Extract Unique TMDB People ID from TmdbMovieCredit
	 * @param tmov
	 * @return
	 */
	 private Map<String, Person> getUniquePerson_from_TmdbMovieCredit(TmdbMovieCredit tmov)
	 {
		 Map<String,Person> personMap = null;
		 if ( null != tmov)
		 {
			 Movie mov = movRep.findByTmdbId(tmov.getTmdbId());
			 Map<String,String> tmPeopleMap = new HashMap<String,String>();
			 //Get Unique TmdbPeople ID from TmDbMovieCredit
			 ExtractUniquePeople_from_TmdbCastList(tmov.getCastList(), tmPeopleMap);
			 ExtractUniquePeople_from_TmdbCrewList(tmov.getCrewList(), tmPeopleMap); 
			
			 if(!tmPeopleMap.isEmpty())
			 {
				 ArrayList<String> keyList = new ArrayList<String>(tmPeopleMap.keySet());
				List<Person> personList =  	peopleRep.findAllByTmdbidIn(keyList);
				personMap = convertPersonList_to_Map(personList);
				
			 }
			 
			 	 
		 }
		
		 return personMap;
	 }
	 
	 /**
	  * Convert PersonList to Map where Key is Person.tmdbId
	  * @param peopleList
	  * @return
	  */
	 private Map<String,Person> convertPersonList_to_Map(List<Person> personList) 
	 {
		 Map<String,Person> personMap = new HashMap<String,Person>();
		 
		 if (( null != personList) && (!personList.isEmpty()))
		 {
			 personList.stream().forEach(i -> {
					try {
					if(!personMap.containsKey(i.getTmdbId()))
					{
						personMap.put(i.getTmdbId(), i);
					}
						
					} catch (Exception e) {
						// Swallowing Parallel Stream exception
						logger.error("Import Error for :"+i.getId(), e);
					}
				});
			
				
		 }
		 
		  return personMap;  
	}
	
	 
	 
	
	 private void ExtractUniquePeople_from_TmdbCastList(List<TmdbCast> castList, Map<String,String> tmPeopleMap) 
	 {
		 
		 if (( null != castList) && (!castList.isEmpty()))
		 {
			 castList.stream().forEach(i -> {
					try {
					if(!tmPeopleMap.containsKey(i.getId()))
					{
						tmPeopleMap.put(i.getId(), String.valueOf(i.getId()));
					}
						
					} catch (Exception e) {
						// Swallowing Parallel Stream exception
						logger.error("Import Error for :"+i.getId(), e);
					}
				});
					 
		 }
		 
		    
	}

	 
	 private void ExtractUniquePeople_from_TmdbCrewList(List<TmdbCrew> crewList, Map<String,String> tmPeopleMap) 
	 {
		 
		 if (( null != crewList) && (!crewList.isEmpty()))
		 {
			 crewList.stream().forEach(i -> {
					try {
					if(!tmPeopleMap.containsKey(i.getId()))
					{
						tmPeopleMap.put(i.getId(), String.valueOf(i.getId()));
					}
						
					} catch (Exception e) {
						// Swallowing Parallel Stream exception
						logger.error("Import Error for :"+i.getId(), e);
					}
				});
					 
		 }
		 
		    
	}
	
	
	private TmdbMovieCredit processImportMovieCredit(Movie pri) throws ImporterException
	{
	// Get Respective Person Details from TMDB Rest API
		TmdbMovieCredit tmov =  getMovieCreditFromMovieDB(pri);	
		return tmov;
		
		
	}
	
	private Page getTotalMovieCreditProcessingPage()
	{
		int pageNo = 0;
		int size = env.getPerson_import_size();
		Pageable requestedPage = PageRequest.of(pageNo, size);
		Page<Movie> page=  movRep.findAllByiscreditProcessedIsFalse(requestedPage);
		return page;
	}
	
	@Async
	private TmdbMovieCredit getMovieCreditFromMovieDB(Movie pri) throws ImporterException
	{
		TmdbMovieCredit comp =null;
		 try {
			 final String url = env.getMovieDB_URL()+"movie/{id}/credits";
			  HttpHeaders headers = new HttpHeaders();
			  headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			  Map<String, String> params = new HashMap<String, String>();
			    params.put("id", String.valueOf(pri.getTmdbId())); 
			    
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
		     
		     comp = restTemplate.getForObject(uri, TmdbMovieCredit.class);
		     
		 }
		 catch(Exception e)
		 {
			throw new ImporterException("Exception in getMovieCreditFrom MDB with ID :"+pri.getId()+", Msg:"+e.getMessage(), e); 
		 }
		     
	     return comp;
	}

	
	 	 		
		
}
