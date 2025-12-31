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
import com.anchor.app.importer.model.MovieImport;
import com.anchor.app.importer.model.MovieTranslationImport;
import com.anchor.app.importer.model.TmdbMovie;
import com.anchor.app.importer.repository.MovieImporterRepository;
import com.anchor.app.importer.repository.TmdbMovieRepository;
import com.anchor.app.importer.service.ImageImporterService;
import com.anchor.app.importer.service.MovieImporterService;
import com.anchor.app.importer.service.TranslationImporterService;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.model.Company;
import com.anchor.app.model.Image;
import com.anchor.app.model.MediaLanguage;
import com.anchor.app.model.Movie;
import com.anchor.app.model.ParentCompany;
import com.anchor.app.model.Production_Country;
import com.anchor.app.model.Translation;
import com.anchor.app.repository.CompanyRepository;
import com.anchor.app.repository.MovieRepository;
import com.anchor.app.repository.TranslationRepository;
import com.anchor.app.service.ImageService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class MovieImporterServiceImpl implements MovieImporterService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private HelperBean helper;

	@Autowired
	private MovieImporterRepository mpvImpRep;
	
	@Autowired
	private MovieRepository movRep;

	@Autowired
	private TmdbMovieRepository tmoRep;

	@Autowired
	private CompanyRepository compRep;
	
	@Autowired
	private ImageImporterService imgImportService;

	
	@Autowired
	private ImageService imgService;

	@Autowired
	private TranslationImporterService tranService;
	
	@Autowired
	private TranslationRepository tranRep;
	
	@Override
	public int importMovieData(String localPath) throws ImporterException {
	
		int result = 0;
		try {
			//Save Json File Record to DB before Processing
			//saveImportMovieEntity(localPath);
			
			// Import CompanyData 
			result =  importMovieData(); 
			
			
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
			
		
	
		
		return result;

		
	}
	
	private int importMovieData() throws ImporterException
	{
		int result = 0;
		try {
			
			// Get Total Pages to Process
			Page page =  getTotalMovieImportPage();
			
			int startPageIndex = 0;
			int endPageIndex = page.getTotalPages(); 
			int itemCount = page.getNumberOfElements();
			int totalCount = (int) page.getTotalElements();
			
			// Process by item
			if (totalCount <= 2000)
			{
				result = processImportedMovieItem();
			}
			// Process by Page
			else {
				// Process PersonImport Collection
				for (int i = 0 ; i<= endPageIndex ; i++)
				{
					
					int batch_result = processImportedMovie(i);	
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

	private int processImportedMovie(int pageIndex) throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			int pageNo = pageIndex;
			int size = env.getPerson_import_size();
			Pageable requestedPage = PageRequest.of(pageNo, size);
			
			Page<MovieImport> pri = mpvImpRep.findAllByisProcessedIsFalse(requestedPage);
			
			List<MovieImport> priList = pri.getContent();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					TmdbMovie tmov = processImportMovie(i);
					storeMovieToDb(tmov, processDate);
					i.setProcessed(true);
					mpvImpRep.save(i);
					
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

	
	private int processImportedMovieItem() throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			List<MovieImport> priList = mpvImpRep.findByisProcessedIsFalse();
			ForkJoinPool customThreadPool = new ForkJoinPool(10);
				
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					TmdbMovie tmov = processImportMovie(i);
					storeMovieToDb(tmov, processDate);
					i.setProcessed(true);
					mpvImpRep.save(i);
					
					logger.info("Movie Process with ID:"+tmov.getTmdbId());
					
					
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
	private void storeMovieToDb(TmdbMovie tmov, Date processDate) throws SequencerException
	{
		Movie movie = helper.convertTmdbMovie_to_Movie(tmov, processDate);
		
		// setup Translation Details for Movie
    	
    	if(null != tmov.getTranslation())
    	{
    		tmov.getTranslation().setId(movie.getId());
    		tmov.getTranslation().setEntityId(movie.getId());
    		tranRep.save(tmov.getTranslation());
    		
    	}

    	movRep.save(movie);
		
		
	}
	
	@Transactional
	private void storeMovieToDb( List<TmdbMovie> tmovList, List<MovieImport> priList)
	{
		Date processDate = new Date();
		if( !tmovList.isEmpty())
		{
			List<Movie> movieList = new ArrayList<Movie>();
			
			tmovList.stream().forEach(tmov -> {
					try {
					
						Movie movie = helper.convertTmdbMovie_to_Movie(tmov, processDate);
						movieList.add(movie);
						
					} catch (Exception e) {
						// Swallowing Parallel Stream exception
						logger.error("Import Error for :"+tmov.getTmdbId(), e);
					}
				});
			
			movRep.saveAll(movieList);
			mpvImpRep.saveAll(priList);
	
		}
				
	}

	
	private TmdbMovie processImportMovie(MovieImport pri) throws ImporterException, SequencerException
	{
	// Get Respective Person Details from TMDB Rest API
		TmdbMovie tmov =  getMovieFromMovieDB(pri);	
		tmov.setId(helper.getSequanceNo(SequenceType.MEDIA));
		addSupportingDetailstoTmdbMovie(tmov);	
		return tmov;
		
		
	}
	
	
	
	 	@Transactional
	 	private void addSupportingDetailstoTmdbMovie(TmdbMovie tmov) throws ImporterException
		{
			 try {
			
				 if(null == tmov)
					{
					 throw new ImporterException ("Tmove Can not be null for Adding Supprting Details");
					}
						// Populate Genre System ID 
						if(!tmov.getGenres().isEmpty())
						{
							populateMovieGenreId(tmov);	
						}
						
						// Populate System Company ID
						if( !tmov.getProduction_companies().isEmpty())
						{
							populateProductionCompanyId(tmov);
						}
						
						// Populate System ProductionCompany ID
						if (!tmov.getProduction_countries().isEmpty())
						{
							populateProductionCountryId(tmov);
						}
						
						//Populate Original Language Details
						if(null != tmov.getOriginal_language())
						{
							MediaLanguage mlan = helper.getMediaLanguagebyisoCode1(tmov.getOriginal_language());
							
							if( null != mlan)
							{
								tmov.setOrgMediaLanguage(mlan);
							}
						}
						//Populate Spoken Languages
						if ( !tmov.getSpoken_languages().isEmpty())
						{
							populateSpokeLanguage(tmov);
						}
						
						// Populate Movie Collection Details
						if( null != tmov.getBelongs_to_collection())
						{
						// Not Yet Implemented can be done post importing collection only	
						}
					 
						// Populate Movie Translation for Respective Movie
						 populateMovieTranslationDetails(tmov); 
						
						
						if( null != tmov.getPoster_path())
						{
							String imgName = tmov.getPoster_path().replace("/", "");
							InputStream stream = imgImportService.getImageStreamFromMovieDB(tmov.getPoster_path());
							if( null != stream)
							{
								MediaImage img = imgService.addImage(EntityType.Movie, ImageType.MediaPoster, tmov.getId(), imgName, stream);
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
								MediaImage img = imgService.addImage(EntityType.Movie, ImageType.MediaBackDrop,tmov.getId(), imgName, stream);
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
	 * Populate DB ID for Respective Genre Fetched from Tmdb API 	
	 * @param tmov
	 */
	@Async 	
	private void populateMovieGenreId(TmdbMovie tmov)
	{
		if(!tmov.getGenres().isEmpty())
		{
			tmov.getGenres().stream().forEach(gen -> {
				try {
					
					String id = helper.getGenereId_forTmdbID(gen.getId());
					if( null != id)
					{
						gen.setTmdbid(gen.getId());
						gen.setId(id);		
					}
				
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Movie Genere Error for :"+gen.getId()+", ERROR:"+e.getMessage());
				}
			});	
		}
		
	}

	/**
	 * Populate DB ID for Respective Production Company System ID Fetched from Tmdb API 	
	 * @param tmov
	 */
	@Async 	
	private void populateProductionCompanyId(TmdbMovie tmov)
	{
		if((null != tmov.getProduction_companies()) && (!tmov.getProduction_companies().isEmpty()))
		{
			tmov.getProduction_companies().stream().forEach(pc -> {
				try {
					if( null != pc.getId())
					{
						Company comp = compRep.findByTmdbid(pc.getId());
						if ( null != comp)
						{
						pc.setTmdbId(pc.getId());
						pc.setId(comp.getId());
						
						if(null !=  comp.getImage_id())
						{
							pc.setLogId(comp.getImage_id());
						}
						
						}	
					}
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error Production Company for :"+pc.getId()+", ERROR:"+e.getMessage());
				}
			});	
		}
		
	}
	
	@Async 	
	private void populateProductionCountryId(TmdbMovie tmov)
	{
		if(!tmov.getProduction_countries().isEmpty())
		{
			tmov.getProduction_countries().stream().forEach(pc -> {
				try {
					
					Production_Country  spc = helper.getProductionCountrybyisoCode1(pc.getIsoCode1());
					if ( null != spc)
					{
						pc.setId(spc.getId());
						pc.setIsoCode2(spc.getIsoCode2());
						pc.setName(spc.getName());
					
					}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Production Country Error for :"+pc.getId()+", ERROR:"+e.getMessage());
				}
			});	
		}
		
	}
	 
	@Async 	
	private void populateSpokeLanguage(TmdbMovie tmov)
	{
		if(!tmov.getSpoken_languages().isEmpty())
		{
			tmov.getSpoken_languages().stream().forEach(sl -> {
				try {
					
					if ( null != sl.getIsoCode())
					{
						MediaLanguage  mlan = helper.getMediaLanguagebyisoCode1(sl.getIsoCode());
						if ( null != mlan)
						{
						
							sl.setId(mlan.getId());
							sl.setNativeName(mlan.getNativeName());
						
						}	
					}
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Language Error for :"+sl.getName()+", ERROR:"+e.getMessage());
				}
			});	
		}
		
	}
	
	/**
	 * Populate DB ID for Respective Genre Fetched from Tmdb API 	
	 * @param tmov
	 * @throws ImporterException 
	 */
	@Async 	
	private void populateMovieTranslationDetails(TmdbMovie tmov) throws ImporterException
	{
		
		try {
			MovieTranslationImport mti = tranService.getMovieTranslationData(tmov.getTmdbId());
			
			if ( null != mti)
			{
				Translation tran = helper.convertMovieTranslationImporttoTranslation(mti);
				
				if ( null != tran)
				{
					tmov.setTranslation(tran);
				}	
			}
			
		}
		catch(Exception e)
		{
			
		}
			
		
		
	}
	

	/**
	 * Populate Movie Collection System ID from System for List of TmdbMovie
	 * @param tmovieList
	 */
	private void populateMovieCollectionDetails(List<TmdbMovie> tmovieList)
	{
		// Right now Collection is Not Part of System whcih will be Loaded later on .
		// Hence Right now only Update Tmdb ID and Set id = null
	
		//List<String> colList = getCollectionID(tmovieList);

		updateMovieCollectionTmdbId(tmovieList);
	}
	
	private void updateMovieCollectionTmdbId(List<TmdbMovie> tmovieList)
	{
		tmovieList.stream().forEach(tmov -> {
				try {
				if(null != tmov.getBelongs_to_collection())
				{
					tmov.getBelongs_to_collection().setTmdbId(tmov.getBelongs_to_collection().getId());
					tmov.getBelongs_to_collection().setId(null);
					
				}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+tmov.getTmdbId(), e);
				}
			});
		
		
	}
	
	/**
	  * Get List of tmdb id of Parent Company
	  * @param compList
	  * @return
	  */
	 private List<String> getCollectionID(List<TmdbMovie> tmovieList) {
		 List<String> prlist = tmovieList.stream()
		    		.filter(tmov -> tmov.getBelongs_to_collection() != null)
		    		.map(tmov -> tmov.getBelongs_to_collection().getTmdbId())
		    		.collect(Collectors.toList());
		    return prlist;
		}
	


	
	/**
	 * Get Total PersonImport Pages as per BatchSize
	 * @return
	 */
	private Page getTotalMovieImportPage()
	{
		int pageNo = 0;
		int size = env.getPerson_import_size();
		Pageable requestedPage = PageRequest.of(pageNo, size);
		
		Page<MovieImport> page = mpvImpRep.findAllByisProcessedIsFalse(requestedPage);
	
		return page;
	}
	
	@Async
	private TmdbMovie getMovieFromMovieDB(MovieImport pri) throws ImporterException
	{
		TmdbMovie comp =null;
		 try {
			 final String url = env.getMovieDB_URL()+"movie/{id}";
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
		     
		     comp = restTemplate.getForObject(uri, TmdbMovie.class);
		     
		 }
		 catch(Exception e)
		 {
			throw new ImporterException("Exception in getMovieFrom MDB with ID :"+pri.getId()+", Msg:"+e.getMessage(), e); 
		 }
		     
	     return comp;
	}

	
	/**
	 * Update Parement Compnay Details for Imported Companies
	 */
	
	/*
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
	
	*/
	
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
		private int saveImportMovieEntity(String path) throws ImporterException
		{
			int result = 0;
			int batch_size = env.getPerson_import_size();
			List<MovieImport> cilist = new ArrayList<MovieImport>();
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
					  
					 MovieImport ci =   helper.convertMovieImportToObject(compTxt);
					 cilist.add(ci);
			         i++;
			         result++;
			         
			         if(i >= batch_size)
			         {
			        	 saveMovieImport(cilist);
			        	 //Reset Counter
			        	 i = 0;
			         }
			         
			       }
				  if(!cilist.isEmpty())
				  {
					  saveMovieImport(cilist);
					  
				  }
			}
			catch(IOException e)
			{
				throw new ImporterException(e.getMessage(), e);
			}
			
			
			return result;
		}
		
		private void saveMovieImport(List<MovieImport> cilist)
		{
			// Save all Unprocess Records to DB
			mpvImpRep.saveAll(cilist);
			// Clear the List
			cilist.clear();
		}

		
		
}
