package com.anchor.app.importer.service.Impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import com.anchor.app.enums.SequenceType;
import com.anchor.app.exception.ImageException;
import com.anchor.app.exception.ImageServiceException;
import com.anchor.app.exception.ImporterException;
import com.anchor.app.exception.PeopleException;
import com.anchor.app.exception.SequencerException;
import com.anchor.app.importer.model.CompanyImport;
import com.anchor.app.importer.model.PersonImport;
import com.anchor.app.importer.model.TmdbPerson;
import com.anchor.app.importer.repository.CompanyImporterRepository;
import com.anchor.app.importer.service.CompanyImporterService;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.media.model.Person;
import com.anchor.app.model.Company;
import com.anchor.app.model.Genre;
import com.anchor.app.model.Image;
import com.anchor.app.model.ParentCompany;
import com.anchor.app.repository.CompanyRepository;
import com.anchor.app.repository.GenreRepository;
import com.anchor.app.service.ImageService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class CompanyImporterServiceImpl implements CompanyImporterService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private HelperBean helper;

	@Autowired
	private CompanyImporterRepository ciRep;

	@Autowired
	private CompanyRepository compRep;

	@Autowired
	private ImageService imgService;

	
	@Override
	public int importCompanyData(String localPath) throws ImporterException {
	
		int result = 0;
		try {
			Date processDate = new Date();

			//Save Json File Record to DB before Processing
			//saveImportCompanyEntity(localPath);
			
			// Import CompanyData 
			result =  importCompanyData(processDate); 
			
			// Update Imported Companies ParentCompany Details
			updateParentCompanyDetails();
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
			
		
	
		
		return result;

		
	}
	
	private int importCompanyData(Date processDate) throws ImporterException
	{
		int result = 0;
		try {
		
			// Get Total Pages to Process
			Page page =  getTotalCompanyImportPage();
			
			int startPageIndex = 0;
			int endPageIndex = page.getTotalPages(); 
			int itemCount = page.getNumberOfElements();
			int totalCount = (int) page.getTotalElements();
			
			// Process by item
			if (totalCount <= 2000)
			{
				result = processImportedCompanyItem(processDate);
			}
			// Process by Page
			else {
				// Process PersonImport Collection
				for (int i = 0 ; i<= endPageIndex ; i++)
				{
					int batch_result = processImportedCompany(i, processDate);	
					result = result + batch_result;
					int remining = totalCount - result;
					logger.info("Company Processing Batch:"+i+" , Total Process:"+result+" , Remaining:"+remining);
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

	private int processImportedCompany(int pageIndex, Date processDate) throws ImporterException 
	{
		int result = 0;
		try {
			int pageNo = pageIndex;
			int size = env.getPerson_import_size();
			Pageable requestedPage = PageRequest.of(pageNo, size);
			
			Page<CompanyImport> pri = ciRep.findAllByisProcessedIsFalse(requestedPage);
			
			
			List<CompanyImport> priList = pri.getContent();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					processImportCompany(i,processDate);
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId(), e);
				}
			})).get();
			
			result = pri.getNumberOfElements();
			ciRep.saveAll(priList);
			
		}
		catch(InterruptedException | ExecutionException e)
		{
			throw new ImporterException(e.getMessage(), e);
		}
		
		return result;
	
	}

	
	private int processImportedCompanyItem(Date processDate) throws ImporterException 
	{
		int result = 0;
		try {
			
			List<CompanyImport> priList = ciRep.findByisProcessedIsFalse();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					processImportCompany(i, processDate);
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId(), e);
				}
			})).get();
			
			result = priList.size();
			ciRep.saveAll(priList);
			
		}
		catch(InterruptedException | ExecutionException e)
		{
			throw new ImporterException(e.getMessage(), e);
		}
		
		return result;
	
	}

	
	private void processImportCompany(CompanyImport pri, Date processDate) throws ImporterException
	{
	// Get Respective Person Details from TMDB Rest API
		Company comp =  getCompanyFromMovieDB(pri);	
	
	// get Compnay Alternate Names if Available any	not yet Implemented
	
		
	// Store Company to Local DB
		if(comp != null)
		{
			StoreCompanytoDB(pri, comp, processDate);
			
		}
		
		
	}
	
	 	@Transactional
	 	private void StoreCompanytoDB(CompanyImport pri, Company comp, Date processDate) throws ImporterException
		{
			 try {
			
				 if(null != comp)
					{
					 comp.setTmdbid(comp.getId());
					 comp.setId(helper.getSequanceNo(SequenceType.COMPANY));
					 	
						if( null != comp.getTmdb_logo_path())
						{
							String imgName = comp.getTmdb_logo_path().replace("/", "");
							InputStream stream = imgService.getImage(comp.getTmdb_logo_path());
							if( null != stream)
							{
								MediaImage img = imgService.addImage(EntityType.Company, ImageType.CompanyLogo, comp.getId(), imgName, stream);
								comp.setImage_id(img.getId());	
							}
							else {
								comp.setTmdb_logo_path(null);
							}
								
							
								
						}
						if(( null != comp.getParent_company()) && (null !=comp.getParent_company().getName()))
						{
							comp.getParent_company().setTmdb_id(comp.getParent_company().getId());
							
						comp.setChild(true);	
						}
						
						comp.setCreatedBy("SYSTEM");
						comp.setCreatedOn(processDate);
						compRep.save(comp);
						
						// Mark Import as Complete
						pri.setProcessed(true);
					//	piRep.save(pri);
						
						
					}
				 
			 }
			 catch(  SequencerException | ImageServiceException e)
			 {
				 throw new ImporterException(e.getMessage(), e);
			 }
			
		}
		
	
	/**
	 * Get Total PersonImport Pages as per BatchSize
	 * @return
	 */
	private Page getTotalCompanyImportPage()
	{
		int pageNo = 0;
		int size = env.getPerson_import_size();
		Pageable requestedPage = PageRequest.of(pageNo, size);
		
		Page<CompanyImport> page = ciRep.findAllByisProcessedIsFalse(requestedPage);
	
		return page;
	}
	
	@Async
	private Company getCompanyFromMovieDB(CompanyImport pri) throws ImporterException
	{
		 Company comp =null;
		 try {
			 final String url = env.getMovieDB_URL()+"company/{id}";
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
		     
		     comp = restTemplate.getForObject(uri, Company.class);
		     
		 }
		 catch(Exception e)
		 {
			throw new ImporterException("Exception in getCompanyFrom MDB with ID :"+pri.getId()+", Msg:"+e.getMessage(), e); 
		 }
		     
	     return comp;
	}

	/**
	 * Update Parement Compnay Details for Imported Companies
	 */
	private void updateParentCompanyDetails()
	{
		//Step 1 : Get All Companies where Parent Company details are Present
		List<Company> compList = compRep.findByisChildIsTrue();
		
		// Get Distinct ParentCompany and their ID by Applying Stream Filter 
		Map<String,ParentCompany> parentMap = convertCompanyListToMap(compList);
		List<String> pcompList = new ArrayList<String>(parentMap.keySet());

		
		// get List of Parent Company Details from DB
		List<Company> parentCompList = compRep.findAllByTmdbidIn(pcompList);
									
		
		// Convert DB List to Map 
		Map<String, Company> pcomMap = convertCompanysToMap(parentCompList);
		
		// Populate Parent Company Details
		
		populateParentCompanyDetails(compList, pcomMap );
		
		//Save Updated Company Details to DB
		compRep.saveAll(compList);
		
		
	}
	
	private void populateParentCompanyDetails(List<Company> compList, Map<String, Company> pcomMap )
	{
		 compList.stream().forEach(comp -> {
				try {
				if(pcomMap.containsKey(comp.getParent_company().getTmdb_id()))
				{
					Company pcomp = pcomMap.get(comp.getParent_company().getId());
					comp.getParent_company().setId(pcomp.getId());
					
					if( null != pcomp.getImage_id())
					{
						comp.getParent_company().setImage_id(pcomp.getImage_id());
						
					}
					
				}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+comp.getId(), e);
				}
			});
		
		
	}
	
	/**
	 * Convert Mpatching CompanyList to Map where Key : Parent tmdbID, Value : Parent tmdb LogoPath
	 * @param compList
	 * @return
	 */
	 private Map<String, ParentCompany> convertCompanyListToMap(List<Company> compList) {
		 Map<String, ParentCompany> map = new HashMap<>();
		 
		 compList.stream().forEach(i -> {
				try {
				if(!map.containsKey(i.getParent_company().getTmdb_id()))
				{
					map.put(i.getParent_company().getTmdb_id(), i.getParent_company());
				}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId(), e);
				}
			});
			
		    return map;
		}
	 
	 
	 private Map<String, Company> convertCompanysToMap(List<Company> compList) {
		
		 Map<String, Company> map = new HashMap<>();
		 
		 compList.stream().forEach(i -> {
				try {
				if(!map.containsKey(i.getTmdbid()))
				{
					map.put(i.getTmdbid(), i);
				}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId(), e);
				}
			});
			
		    return map;
		}
	 
	 /**
	  * Get List of tmdb id of Parent Company
	  * @param compList
	  * @return
	  */
	 private List<String> getParentCompanyIDs(List<Company> compList) {
		 List<String> prlist = compList.stream()
		    		.filter(company -> company.getParent_company().getTmdb_logo_path() != null)
		    		.map(company -> company.getParent_company().getTmdb_id())
		    		.collect(Collectors.toList());
		    return prlist;
		}
	 
	 /**
		 * Save ImportPerson Entity to DB by Processing JSON File
		 * @param path
		 * @return
		 * @throws ImporterException
		 */
		private int saveImportCompanyEntity(String path) throws ImporterException
		{
			int result = 0;
			int batch_size = env.getPerson_import_size();
			List<CompanyImport> cilist = new ArrayList<CompanyImport>();
			try {
				if (( null == path) || (path.equalsIgnoreCase("")))
				{
					throw new ImporterException("Path can not be null or empty");
				}
			
				  File jsonFile = new File(path);
				  
				  LineIterator fileContents= FileUtils.lineIterator(jsonFile, StandardCharsets.UTF_8.name());
				  int i = 0;
				  while(fileContents.hasNext()){
					  String compTxt = fileContents.nextLine();
					  
					 CompanyImport ci =   helper.convertCompanyImportToObject(compTxt);
					 cilist.add(ci);
			         i++;
			         result++;
			         
			         if(i >= batch_size)
			         {
			        	 saveCompanyImport(cilist);
			        	 //Reset Counter
			        	 i = 0;
			         }
			         
			       }
				  if(!cilist.isEmpty())
				  {
					  saveCompanyImport(cilist);
					  
				  }
			}
			catch(IOException e)
			{
				throw new ImporterException(e.getMessage(), e);
			}
			
			
			return result;
		}
		
		private void saveCompanyImport(List<CompanyImport> cilist)
		{
			// Save all Unprocess Records to DB
			ciRep.saveAll(cilist);
			// Clear the List
			cilist.clear();
		}


}
