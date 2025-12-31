package com.anchor.app.importer.service.Impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
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

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.exception.ImporterException;
import com.anchor.app.exception.SequencerException;
import com.anchor.app.importer.model.MovieCollectionImport;
import com.anchor.app.importer.model.MovieImport;
import com.anchor.app.importer.model.MovieTranslationImport;
import com.anchor.app.importer.model.TmdbCollection;
import com.anchor.app.importer.model.TmdbMovie;
import com.anchor.app.importer.repository.MovieCollectionImporterRepository;
import com.anchor.app.importer.repository.MovieImporterRepository;
import com.anchor.app.importer.repository.TmdbMovieRepository;
import com.anchor.app.importer.service.CollectionImporterService;
import com.anchor.app.importer.service.ImageImporterService;
import com.anchor.app.importer.service.MovieImporterService;
import com.anchor.app.importer.service.TranslationImporterService;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.model.Company;
import com.anchor.app.model.Image;
import com.anchor.app.model.MediaLanguage;
import com.anchor.app.model.Movie;
import com.anchor.app.model.MovieCollection;
import com.anchor.app.model.ParentCompany;
import com.anchor.app.model.Production_Country;
import com.anchor.app.model.Translation;
import com.anchor.app.repository.CompanyRepository;
import com.anchor.app.repository.MovieCollectionRepository;
import com.anchor.app.repository.MovieRepository;
import com.anchor.app.repository.TranslationRepository;
import com.anchor.app.service.ImageService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class CollectionImporterServiceImpl implements CollectionImporterService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private HelperBean helper;

	@Autowired
	private MovieCollectionImporterRepository mcImpRep;

	
	@Autowired
	private MovieRepository movRep;

	@Autowired
	private MovieCollectionRepository mcRep;

	
	@Autowired
	private ImageImporterService imgImportService;

	
	@Autowired
	private ImageService imgService;

	
	
	@Override
	public int importMovieCollectionData(String localPath) throws ImporterException {
	
		int result = 0;
		try {
			//Save Json File Record to DB before Processing
			//saveImportMovieCollectionEntity(localPath);
			
			// Import MovieCollectionData 
			result =  importMovieCollectionData(); 
			
			
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
			
		
	
		
		return result;

		
	}
	
	private int importMovieCollectionData() throws ImporterException
	{
		int result = 0;
		try {
			
			// Get Total Pages to Process
			Page page =  getTotalMovieCollectionImportPage();
			
			int startPageIndex = 0;
			int endPageIndex = page.getTotalPages(); 
			int itemCount = page.getNumberOfElements();
			int totalCount = (int) page.getTotalElements();
			
			// Process by item
			if (totalCount <= 2000)
			{
				result = processImportedMovieCollectionItem();
			}
			// Process by Page
			else {
				// Process PersonImport Collection
				for (int i = 0 ; i<= endPageIndex ; i++)
				{
					
					int batch_result = processImportedMovieCollection(i);	
					result = result + batch_result;
					int remining = totalCount - result;
					logger.info("Movie Processing Batch:"+i+" , Total Process:"+result+" , Remaining:"+remining);
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

	private int processImportedMovieCollection(int pageIndex) throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			int pageNo = pageIndex;
			int size = env.getPerson_import_size();
			Pageable requestedPage = PageRequest.of(pageNo, size);
			
			Page<MovieCollectionImport> pri = mcImpRep.findAllByisProcessedIsFalse(requestedPage);
			
			List<MovieCollectionImport> priList = pri.getContent();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					TmdbCollection tmov = processImportMovieCollection(i);
					storeMovieCollectionToDb(tmov, processDate);
					i.setProcessed(true);
					mcImpRep.save(i);
					logger.info("MovieCollection Process with ID:"+tmov.getTmdbId());
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+i.getId()+" ERROR:"+e.getMessage());
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

	
	private int processImportedMovieCollectionItem() throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			List<MovieCollectionImport> priList = mcImpRep.findByisProcessedIsFalse();
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
				
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					TmdbCollection tmov = processImportMovieCollection(i);
					
					storeMovieCollectionToDb(tmov, processDate);
					i.setProcessed(true);
					mcImpRep.save(i);
					
					logger.info("MovieCollection Process with ID:"+tmov.getTmdbId());
					
					
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
	private void storeMovieCollectionToDb(TmdbCollection tmov, Date processDate) throws SequencerException
	{
		
		// get All Movie from Db belongs to Respective Collection
		List<Movie> colMovieList =  getMovieListBelongs_to_Collection(tmov);
		
		MovieCollection mcol = helper.convertTmdbMovieCollection_to_MovieCollection(tmov, colMovieList, processDate);
		
		  // Save MovieCollection to DB
			mcRep.save(mcol);
	
			// Save Updated Movies to DB
			movRep.saveAll(colMovieList);	
		
		
	}
	
	
	private TmdbCollection processImportMovieCollection(MovieCollectionImport pri) throws ImporterException, SequencerException
	{
	// Get Respective Person Details from TMDB Rest API
		TmdbCollection tmov =  getMovieCollectionFromMovieDB(pri);	
		tmov.setId(helper.getSequanceNo(SequenceType.COLLECTION));
		addSupportingDetailstoTmdbMovieCollection(tmov);	
		return tmov;
		
		
	}
	
	
	private List<Movie> getMovieListBelongs_to_Collection(TmdbCollection tmov)
	{
		List<Movie> movieList = null;
		if( ( null != tmov.getPartList()) && ( !tmov.getPartList().isEmpty()))
		{
			List<String> tmovTmdbIds = getMovieTmdbID(tmov.getPartList());
			movieList = movRep.findAllByTmdbidIn(tmovTmdbIds);
		}
		
	
		
		return movieList;
	}
	
	/**
	 * Get List of Movies TmdbID
	 * @param movieList
	 * @return
	 */
	 private List<String> getMovieTmdbID(List<TmdbMovie> movieList) {
		 List<String> prlist = movieList.stream()
		    		.filter(tmov -> tmov.getTmdbId() != null)
		    		.map(tmov -> tmov.getTmdbId())
		    		.collect(Collectors.toList());
		    return prlist;
		}
	
	 	@Transactional
	 	private void addSupportingDetailstoTmdbMovieCollection(TmdbCollection tmov) throws ImporterException
		{
			 try {
			
				 if(null == tmov)
					{
					 throw new ImporterException ("Tmove Can not be null for Adding Supprting Details");
					}
						
						
						if( null != tmov.getPoster_path())
						{
							String imgName = tmov.getPoster_path().replace("/", "");
							InputStream stream = imgImportService.getImageStreamFromMovieDB(tmov.getPoster_path());
							if( null != stream)
							{
								MediaImage img = imgService.addImage(EntityType.Collection, ImageType.CollectionPoster, tmov.getId(), imgName, stream);
								tmov.setPoster_pathId(img.getId());	
							}
							else {
								tmov.setPoster_pathId(null);
							}
								
						}
						if( null != tmov.getBackdrop_path())
						{
							String imgName = tmov.getBackdrop_path().replace("/", "");
							InputStream stream = imgImportService.getImageStreamFromMovieDB(tmov.getBackdrop_path());
							if( null != stream)
							{
								MediaImage img = imgService.addImage(EntityType.Collection, ImageType.CollectionBackDrop, tmov.getId(), imgName, stream);
								tmov.setBackdrop_pathId(img.getId());	
							}
							else {
								tmov.setBackdrop_pathId(null);
							}
								
						}
						
						
					
				 
			 }
			 catch( Exception e)
			 {
				 throw new ImporterException("Exception in Adding Supporting Details to TMDBMovie ERROR:"+e.getMessage());
			 }
			
		}
		

	 	
	 	
		
		
	/**
	 * Get Total PersonImport Pages as per BatchSize
	 * @return
	 */
	private Page getTotalMovieCollectionImportPage()
	{
		int pageNo = 0;
		int size = env.getPerson_import_size();
		Pageable requestedPage = PageRequest.of(pageNo, size);
		
		Page<MovieCollectionImport> page = mcImpRep.findAllByisProcessedIsFalse(requestedPage);
	
		return page;
	}
	
	@Async
	private TmdbCollection getMovieCollectionFromMovieDB(MovieCollectionImport pri) throws ImporterException
	{
		TmdbCollection comp =null;
		 try {
			 final String url = env.getMovieDB_URL()+"collection/{id}";
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
		    
		  //   HttpEntity<String> entity = new HttpEntity<String>(headers);
		     
		     comp = restTemplate.getForObject(uri, TmdbCollection.class);
		     
		 }
		 catch(Exception e)
		 {
			throw new ImporterException("Exception in getMovieFrom MDB with ID :"+pri.getId()+", Msg:"+e.getMessage(), e); 
		 }
		     
	     return comp;
	}

	

	 /**
		 * Save ImportMovieCollection Entity to DB by Processing JSON File
		 * @param path
		 * @return
		 * @throws ImporterException
		 */
		private int saveImportMovieCollectionEntity(String path) throws ImporterException
		{
			int result = 0;
			int batch_size = env.getPerson_import_size();
			List<MovieCollectionImport> cilist = new ArrayList<MovieCollectionImport>();
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
					  
					  MovieCollectionImport ci =   helper.convertMovieCollectionImportToObject(compTxt);
					 cilist.add(ci);
			         i++;
			         result++;
			         
			         if(i >= batch_size)
			         {
			        	 saveMovieCollectionImport(cilist);
			        	 //Reset Counter
			        	 i = 0;
			         }
			         
			       }
				  if(!cilist.isEmpty())
				  {
					  saveMovieCollectionImport(cilist);
					  
				  }
			}
			catch(IOException e)
			{
				throw new ImporterException(e.getMessage(), e);
			}
			
			
			return result;
		}
		
		private void saveMovieCollectionImport(List<MovieCollectionImport> cilist)
		{
			// Save all Unprocess Records to DB
			mcImpRep.saveAll(cilist);
			// Clear the List
			cilist.clear();
		}

		
		
}
