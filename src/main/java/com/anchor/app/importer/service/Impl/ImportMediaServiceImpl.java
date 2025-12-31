package com.anchor.app.importer.service.Impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.enums.MediaType;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.exception.HlsProcessingException;
import com.anchor.app.exception.ImageException;
import com.anchor.app.exception.ImporterException;
import com.anchor.app.exception.MediaException;
import com.anchor.app.exception.MediaServiceException;
import com.anchor.app.exception.ValidationException;
import com.anchor.app.exception.YouTubeDownloadException;
import com.anchor.app.importer.exception.ImportMediaServiceException;
import com.anchor.app.importer.model.ImportMediaRequest;
import com.anchor.app.importer.model.YouTubeMedia;
import com.anchor.app.importer.repository.ImportMediaRequestRepository;
import com.anchor.app.importer.service.HlsConversionService;
import com.anchor.app.importer.service.HlsUploaderService;
import com.anchor.app.importer.service.ImportMediaService;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.media.service.MediaService;
import com.anchor.app.minio.MinioService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class ImportMediaServiceImpl implements ImportMediaService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MinioService minioService;

	@Autowired
	private HelperBean helper;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private EnvProp env;

	@Autowired
	private ImportMediaRequestRepository impMediaRep;

	@Autowired
	private HlsConversionService hlsconverionService;

	@Autowired
	private HlsUploaderService hlsuploadService;


	
	@Override
	public Media importMedia(String name, String title, MediaType mediaType, String createdBy, MultipartFile mediaFile, 
			String mediaFileServerPath, boolean isServerFile)
			throws ImportMediaServiceException {
	
		ImportMediaRequest importRequest = null;
		Media media = null;
		Date startT = new Date();
		
		try {
			
			
			validateImportMediaRequest(name, title, mediaType, createdBy, mediaFile,mediaFileServerPath, isServerFile );

			importRequest = new ImportMediaRequest();
			
			importRequest.setId(helper.getSequanceNo(SequenceType.MEDIA));
			importRequest.setMediaId(importRequest.getId());
			importRequest.setRequestId(importRequest.getId());
		    importRequest.setName(name);
		    importRequest.setTitle(title);
		    importRequest.setMediaType(mediaType);
		    importRequest.setCreatedBy(createdBy);
		    importRequest.setCreatedOn(startT);
		    importRequest.setModifiedBy(createdBy);
		    importRequest.setModifiedOn(startT);
		    
		    // If Media Uploaded as a File With Request
		    if(!isServerFile)
		    {
		    	 // Save Imported Media to Staging Location
			    saveImportMediaToStaging(importRequest, mediaFile);
			    	
		    }
		    else if ( null != mediaFileServerPath)
		    {
		    	// Save Already Present Media File From Server Location to Staging Location
		    	saveImportMediaToStaging(importRequest, mediaFileServerPath);
		    	
		    }
		    
		    // Prepare InActive Media Object which can be Populated by Other details once HLS Conversion is done
		    // Save ImportMediaStatus to DB for Further processing in Batch
	
		    media = new Media();
		    media.setId(importRequest.getId());
		    media.setName(importRequest.getName());
		    media.setTitle(importRequest.getTitle());
		    media.setMediaType(importRequest.getMediaType());
		    media.setCreatedBy(importRequest.getCreatedBy());
		    media.setCreatedOn(importRequest.getCreatedOn());
		    media.setActive(false);
		    media.setVideoPresent(false);
		    
		    
		    impMediaRep.save(importRequest);
		    
			mediaService.saveMedia(media);		
		}
		catch(Exception e)
		{
			throw new ImportMediaServiceException(e.getMessage(), e);
		}
		
		return media;
	}
	
	
	
	private void validateImportMediaRequest(String name, String title, MediaType mediaType, String createdBy, MultipartFile mediaFile, String mediaFileServerPath, boolean isServerFile) throws ValidationException
	{
		if( null == name)
		{
			throw new ValidationException("Invalid Media name");
		}
		
		if( null == title)
		{
			throw new ValidationException("Invalid Media title");
		}
		
		if( null == mediaType)
		{
			throw new ValidationException("Invalid Media type");
		}
		
		if( null == createdBy)
		{
			throw new ValidationException("Invalid CreatedBy user name");
		}
		
		if( isServerFile)
		{
			if( null == mediaFileServerPath)
			{
				throw new ValidationException("No Media File Present at Server Location: "+mediaFileServerPath);
			}
		}
		else {
			
			
			if((null == mediaFile) || (mediaFile.getSize() == 0))
			{
				throw new ValidationException("No Media File Present at Request");
			}
				
		}
		
		
	}

	/**
	 * Save Uploaded MediaFile to Staging Location
	 * @param importRequest
	 * @param mediaFile
	 * @throws ImportMediaServiceException
	 */
	private void saveImportMediaToStaging(ImportMediaRequest importRequest, MultipartFile mediaFile) throws ImportMediaServiceException 
	{
		try {
			
			String mediaFileName = mediaFile.getOriginalFilename();
			
			if( null == mediaFileName)
			{
				throw new ImportMediaServiceException("Invalid Upload File");
			}
			String extension = helper.getExtensionofFileName(mediaFileName);
		
			
			String objectFileName = importRequest.getId()+"."+extension;
			
			// Copy InputSteam to Media Stagging Location
			Path mediaStagingLocation = env.getImportMedia_StaggingLocation();
			Path tempMediaPath = Paths.get(mediaStagingLocation.toString(), importRequest.getId(), objectFileName);
			
			File targetFile = new File(tempMediaPath.toString());
			
			FileUtils.copyInputStreamToFile(mediaFile.getInputStream(), targetFile);
			
			 if(!Files.exists(tempMediaPath, LinkOption.NOFOLLOW_LINKS))
				{
				 throw new ImportMediaServiceException("Unable to upload file"+mediaFileName);
						
				}
			 
			 Path importedPath = Paths.get(mediaStagingLocation.toString(), importRequest.getId());
			 importRequest.setImportedPath(importedPath.toString());
			 importRequest.setImportFileName(objectFileName);
			 importRequest.setVideoDownloaded(true);
			
		}
		catch(Exception e)
		{
			throw new ImportMediaServiceException(e.getMessage(), e);
		}


	}


	/**
	 * Move Already Uploaded Server File to Staging Location
	 * @param importRequest
	 * @param mediaFileServerPath
	 * @throws ImportMediaServiceException
	 */
	private void saveImportMediaToStaging(ImportMediaRequest importRequest, String mediaFileServerPath) throws ImportMediaServiceException 
	{
		try {
			
			String mediaFileName = helper.getNameofFileName(mediaFileServerPath);
			
			if( null == mediaFileName)
			{
				throw new ImportMediaServiceException("Invalid Upload File");
			}
			
			String extension = helper.getExtensionofFileName(mediaFileName);
		
			// Verify requested Media File Present on Server Path
			
			Path serverMediaPath = Paths.get(mediaFileServerPath);
			
			 if(!Files.exists(serverMediaPath, LinkOption.NOFOLLOW_LINKS))
				{
				 throw new ImportMediaServiceException("Unable to find uploaded Server Path File at Location"+mediaFileServerPath);
						
				}
		
			File serverMediafile = new File(mediaFileServerPath);
			
			String objectFileName = importRequest.getId()+"."+extension;
			
			// Copy InputSteam to Media Stagging Location
			Path mediaStagingLocation = env.getImportMedia_StaggingLocation();
			Path tempMediaPath = Paths.get(mediaStagingLocation.toString(), importRequest.getId(), objectFileName);
			
			File targetFile = new File(tempMediaPath.toString());
			
			FileUtils.copyFile(serverMediaPath.toFile(), targetFile);
			
			 if(!Files.exists(tempMediaPath, LinkOption.NOFOLLOW_LINKS))
				{
				 throw new ImportMediaServiceException("Unable to upload file"+mediaFileName);
						
				}
			 
			 Path importedPath = Paths.get(mediaStagingLocation.toString(), importRequest.getId());
			 importRequest.setImportedPath(importedPath.toString());
			 importRequest.setImportFileName(objectFileName);
			 importRequest.setVideoDownloaded(true);
			
		}
		catch(Exception e)
		{
			throw new ImportMediaServiceException(e.getMessage(), e);
		}


	}


	
	@Override
	public int processImportedMedia() throws ImportMediaServiceException {
		
		int result = 0;
		
		try {
			
			result = importMedia();
		}
		catch(Exception e)
		{
			throw new ImportMediaServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	
	private int importMedia() throws ImporterException
	{
		int result = 0;
		try {
		
			result = processImportedMediaItem();
			
			/*
			// Get Total Pages to Process
			Page page =  getTotalMediaImportPage();
			
			int startPageIndex = 0;
			int endPageIndex = page.getTotalPages(); 
			int itemCount = page.getNumberOfElements();
			int totalCount = (int) page.getTotalElements();
			
			// Process by item
			if (totalCount <= 2000)
			{
				result = processImportedMediaItem();
			}
			// Process by Page
			else {
				// Process PersonImport Collection
				for (int i = 0 ; i<= endPageIndex ; i++)
				{
						// Note : Open to Put method for Import Media by Page wise
					int batch_result = processImportedMediaItem(i);	
					//result = result + batch_result;
					int remining = totalCount - result;
					logger.info("Movie Processing Batch:"+i+" , Total Process:"+result+" , Remaining:"+remining);
				}
					
			}
			
			*/
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
		
		return result;
			
	}

	
	private Page getTotalMediaImportPage()
	{
		int pageNo = 0;
		int size = env.getYouTube_download_size();
		Pageable requestedPage = PageRequest.of(pageNo, size);
		
		Page<ImportMediaRequest> page = impMediaRep.findAllByisProcessedIsFalse(requestedPage);
	
		return page;
	}

	
	private int processImportedMediaItem() throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			List<ImportMediaRequest> priList = impMediaRep.findByisProcessedIsFalse();
			
			for (ImportMediaRequest impr : priList)
			{
				try {
					processMediaImport(impr);
					logger.info("Import Media Process with ID:"+impr.getId());	
				}
				catch(Exception e)
				{
					//Swalow Individual Media Processing Exception
					
					logger.error("Import Media Item Exception For ID"+impr.getId(), e);
				}
				
			
			}
			
			/*
			ForkJoinPool customThreadPool = new ForkJoinPool(env.getYouTube_download_size());
				
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
				
					processMediaImport(i);
					logger.info("Import Media Process with ID:"+i.getId());
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Media Iteam Threded Processing Exception :"+i.getId(), e);
				}
			})).get();
			
			*/
			
			result = priList.size();
			
		}
		catch(Exception e)
		{
			logger.error("Import Media Item Thred Processing Execution Exception", e);
			throw new ImporterException(e.getMessage(), e);
		}
		
		return result;
	
	}
	
	
	private int processImportedMediaItem(int pageIndex) throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			int pageNo = pageIndex;
			int size = env.getPerson_import_size();
			Pageable requestedPage = PageRequest.of(pageNo, size);
			
			Page<ImportMediaRequest> pri = impMediaRep.findAllByisProcessedIsFalse(requestedPage);
			
			List<ImportMediaRequest> priList = pri.getContent();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(env.getYouTube_download_size());
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					processMediaImport(i);
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import media Import Error for :"+i.getId(), e);
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

	
	private int processMediaImport(ImportMediaRequest imr) throws ImporterException
	{
		
		int result = 0;
		// Step 2 : Create  HLS Steam for Media
		
		try {
	
			if(!imr.isHlsStreamCreated())
			{
				// Save Default MasterPlayListName from Configuration
				imr.setMasterPlayListName(env.getHlsMasterPlayListName());
				
				hlsconverionService.createHlsStramForImportMedia(imr);
				
				if(!imr.isHlsStreamCreated())
				{
					throw new HlsProcessingException(" Failed to Create HLS Stream for YouTubeMedia:"+imr.getId());
				}
				
				// Save Updated Hls Stream Created Status to DB
				imr.setHlsStreamCreated(true);
				impMediaRep.save(imr);
		
			}
			
			
			if(!imr.isHlsStreamPersisted())
			{
				hlsuploadService.uploadImportMediaGeneratedHls(imr);
				
				
				if(imr.isHlsStreamPersisted())
				{
						
					// Save Updated ImagePersisted Status to DB
					impMediaRep.save(imr);
			
				}
			}
			
			// Finally Save Media as All Requried Workflow for YouTubeMedia is completed Now
			if(!imr.isMediaPersisted())
			{
				
				Media media = mediaService.getMedia(imr.getMediaId());
				
				if( null == media)
				{
					throw new ImporterException("Media not Found for ImportMedia Request with ID:"+imr.getMediaId());
				}
				// Mark media as Active
			
				//media.setActive(true);
				media.setVideoPresent(true);
				media.setRuntime(imr.getDurationInSecond());
				media.setContentID(imr.getContentID());
				media.setBucketName(imr.getBucketName());
				mediaService.saveMedia(media);
				
				// Mark Import Media Request Processing as Completed
				imr.setProcessed(true);
				imr.setMediaPersisted(true);
				imr.setVideoDownloaded(true);
				imr.setVideoProcessed(true);
				
				// Save Updated ImagePersisted Status to DB
				impMediaRep.save(imr);
				
				helper.deleteDirectory(imr.getHlsMediaPath());
			}


		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ImporterException(e.getMessage(), e);
		}
		
			
		
		return result;
	}
	

}
