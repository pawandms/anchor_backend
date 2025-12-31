package com.anchor.app.importer.service.Impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ExternalSystemType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.enums.ImportStatusType;
import com.anchor.app.exception.HlsProcessingException;
import com.anchor.app.exception.ImageException;
import com.anchor.app.exception.ImporterException;
import com.anchor.app.exception.MediaException;
import com.anchor.app.exception.MediaServiceException;
import com.anchor.app.exception.YouTubeDownloadException;
import com.anchor.app.importer.model.ImportMediaIds;
import com.anchor.app.importer.model.YouTubeImportRequest;
import com.anchor.app.importer.model.YouTubeMedia;
import com.anchor.app.importer.repository.ImportMediaIdsRepository;
import com.anchor.app.importer.repository.YouTubeImportRequestRepository;
import com.anchor.app.importer.repository.YouTubeMediaRepository;
import com.anchor.app.importer.service.HlsConversionService;
import com.anchor.app.importer.service.HlsUploaderService;
import com.anchor.app.importer.service.ImageImporterService;
import com.anchor.app.importer.service.YouTubeDownloadService;
import com.anchor.app.importer.service.YouTubeImporterService;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.media.service.MediaService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class YouTubeImporterServiceImpl implements YouTubeImporterService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private HelperBean helper;
	
	@Autowired
	private YouTubeMediaRepository ytmRep;

	@Autowired
	private YouTubeDownloadService tydService;

	@Autowired
	private HlsConversionService hlsconverionService;

	@Autowired
	private HlsUploaderService hlsuploadService;

	@Autowired
	private ImageImporterService imgImportService;

	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private YouTubeImportRequestRepository ytImpRep;

	@Autowired
	private ImportMediaIdsRepository importMediaIdsRep;
	
	@Override
	public YouTubeImportRequest importYouTubeMedia(YouTubeImportRequest yimpReq) throws ImporterException {
		
		try {
			
			Date modDate = new Date();
			helper.prepareYouTubeMediaRequest(yimpReq, modDate);
			yimpReq = ytImpRep.save(yimpReq);
			
			
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
			
		return yimpReq;
		
	}

	@Override
	public int processImportedYouTubeMedia() throws ImporterException {
		
		int result;
		try {
			
			result = importYouTubeData();
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
		
		return result;
	}
	
	private int importYouTubeData() throws ImporterException
	{
		int result = 0;
		try {
		
			result = processImportedYouTubeItem();
			
			/*
			// Get Total Pages to Process
			Page page =  getTotalYouTubeImportPage();
			
			int startPageIndex = 0;
			int endPageIndex = page.getTotalPages(); 
			int itemCount = page.getNumberOfElements();
			int totalCount = (int) page.getTotalElements();
			
			// Process by item
			if (totalCount <= 5)
			{
				result = processImportedYouTubeItem();
			}
			// Process by Page
			else {
				// Process PersonImport Collection
				for (int i = 0 ; i<= endPageIndex ; i++)
				{
					
					int batch_result = processImportedYouTubeItem(i);	
					result = result + batch_result;
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
	
	private Page getTotalYouTubeImportPage()
	{
		int pageNo = 0;
		int size = env.getYouTube_download_size();
		Pageable requestedPage = PageRequest.of(pageNo, size);
		
		Page<YouTubeMedia> page = ytmRep.findAllByisProcessedIsFalse(requestedPage);
	
		return page;
	}

	private int processImportedYouTubeItem(int pageIndex) throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			int pageNo = pageIndex;
			int size = env.getPerson_import_size();
			Pageable requestedPage = PageRequest.of(pageNo, size);
			
			Page<YouTubeMedia> pri = ytmRep.findAllByisProcessedIsFalse(requestedPage);
			
			List<YouTubeMedia> priList = pri.getContent();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(env.getYouTube_download_size());
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
					processYouTubeImport(i);
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("YouTube media Import Error for :"+i.getId(), e);
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

	
	private int processImportedYouTubeItem() throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			List<YouTubeMedia> priList = ytmRep.findByisProcessedIsFalse();
			
			for (YouTubeMedia ytm : priList)
			{
				try {
					processYouTubeImport(ytm);
					logger.info("YouTube Media Process with ID:"+ytm.getId());
					
				}
				catch(Exception e)
				{
					// Swallowing Parallel Stream exception
					logger.error("YouTube Media Import Error for :"+ytm.getId(), e);
				}
				
				
			}
			
		
			/*
			ForkJoinPool customThreadPool = new ForkJoinPool(env.getYouTube_download_size());
				
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
				
					processYouTubeImport(i);
					logger.info("YouTube Media Process with ID:"+i.getId());
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("YouTube Media Import Error for :"+i.getId(), e);
				}
			})).get();
			
			*/
			
			result = priList.size();
			
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);
		}
		
		return result;
	
	}
	
	
	
	@Async
	private void processYouTubeImport(YouTubeMedia ytm) throws YouTubeDownloadException, HlsProcessingException, MediaException, ImageException, MediaServiceException
	{
		// Step 1 : Download YouTub Media with the help of youtube-dl utility
		
		if(!ytm.isVideoDownloaded())
		{
			tydService.downloadYouTubeMedia(ytm);
		
			if(!ytm.isVideoDownloaded())
			{
				throw new YouTubeDownloadException(" Failed to Download youtubeMedia"+ytm.getId());
			}
			
			// Save Status to DB as Video Imported from YouTube
			ytmRep.save(ytm);
		
		}
		
		// Step 2 : Create  HLS Steam for yotubeMedia
		
		if(!ytm.isHlsStreamCreated())
		{
			logger.info("Create HLS Stream Started for :"+ytm.getId());
			// Save Default MasterPlayListName from Configuration
			ytm.setMasterPlayListName(env.getHlsMasterPlayListName());
			
			hlsconverionService.createHlsStramForMedia(ytm);
			
			if(!ytm.isHlsStreamCreated())
			{
				throw new HlsProcessingException(" Failed to Create HLS Stream for YouTubeMedia:"+ytm.getId());
			}
			
			// Save Updated Hls Stream Created Status to DB
			ytmRep.save(ytm);
	
		}
		
		// Create Media Object to Get ID to attach with HLS Stream
	// Step 3 : Convert YouTubeMedia to Media Object
	Media media = helper.convertYouTubeMediaToMedia(ytm);
	ytm.setMediaId(media.getId());
	
	// Store Images Relate to YoutubeMedia
	/*
	if( !ytm.isImagesPersisted())
	{
		if( !ytm.getYtmInfo().getThumbnails().isEmpty())
		{
			List<MediaImage> miList = imgImportService.getMediaImageFromList(EntityType.MusicVideo, ImageType.MediaPoster, media.getId(), ytm.getYtmInfo().getThumbnails());
			
				if(!miList.isEmpty())
				{
					ytm.getImageList().addAll(miList);
						
				}
				ytm.setImagesPersisted(true);
				
				// Save Updated ImagePersisted Status to DB
				ytmRep.save(ytm);
			
		}
		
	}

	*/
	
		if(!ytm.isHlsStreamPersisted())
		{
			hlsuploadService.uploadYouTubeGeneratedHls(ytm);
			
			
			if(ytm.isHlsStreamPersisted())
			{
				// If Stream Uploaded Successfully to DB 
				
				// Save Updated ImagePersisted Status to DB
				ytmRep.save(ytm);
		
			}
		}
		
		// Finally Save Media as All Requried Workflow for YouTubeMedia is completed Now
		if(!ytm.isMediaPersisted())
		{
			
			// Add Images to Media if Any 
			media.getImageList().addAll(ytm.getImageList());
			saveProcessMediaDetails(ytm, media);
			//helper.deleteDirectory(ytm.getHlsMediaPath());
		}
		
	}
	
	
	@Transactional
	private void saveProcessMediaDetails(YouTubeMedia ytm, Media media)
	{
		try {
			media.setVideoPresent(true);
			Optional<ImportMediaIds> imdOp =  importMediaIdsRep.findById(ytm.getYoutubeId());
			
			if(imdOp.isPresent())
			{
				ImportMediaIds imd = imdOp.get();
				imd.setImportStatus(ImportStatusType.Completed);
				
				importMediaIdsRep.save(imd);
				
				
				
			}
			
			media.setVideoPresent(true);
			media.setBucketName(ytm.getBucketName());
			media.setContentID(ytm.getContentID());
			//media.setActive(true);
			mediaService.saveMedia(media);
			
			ytm.setMediaPersisted(true);
			ytm.setProcessed(true);
			
			// Save Updated MediaPersisted Status to DB
			ytmRep.save(ytm);
		
		}
		catch(Exception e)
		{
			logger.error("Error Saving Final Status of YouTube Media:"+ytm.getId());
		}

		
	}

	@Override
	public int processYouTubeImportRequest() throws ImporterException {

		int result;
		try {
			
			result = processImportRequestData();
		}
		catch(Exception e)
		{
			throw new ImporterException(e.getMessage(), e);	
		}
		
		return result;
	}

	
	private int processImportRequestData() throws ImporterException
	{
		int result = 0;
		try {
			
			// Get Total Pages to Process
			Page page =  getTotalYouTubeImportRequestPage();
			
			int startPageIndex = 0;
			int endPageIndex = page.getTotalPages(); 
			int itemCount = page.getNumberOfElements();
			int totalCount = (int) page.getTotalElements();
			
			
			
			// Process by item
			if( (totalCount > 0) && (totalCount <= 2000))
			{
				result = processYouTubeImportRequestItem();
			}
			// Process by Page
			else if (totalCount > 2000)
			{
				// Process PersonImport Collection
				for (int i = 0 ; i<= endPageIndex ; i++)
				{
					
					int batch_result = processYouTubeImportRequestItem(i);	
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
	
	private Page getTotalYouTubeImportRequestPage()
	{
		int pageNo = 0;
		int size = env.getYouTube_download_size();
		Pageable requestedPage = PageRequest.of(pageNo, size);
		
		Page<YouTubeImportRequest> page = ytImpRep.findAllByisProcessedIsFalse(requestedPage);
	
		return page;
	}
	
	
	private int processYouTubeImportRequestItem() throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			List<YouTubeImportRequest> priList = ytImpRep.findByisProcessedIsFalse();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(env.getYouTube_download_size());
				
			customThreadPool.submit(() -> priList.parallelStream().forEach(i -> {
				try {
				
					processYouTubeImportRequest(i);
					logger.info("YouTube Media Process with ID:"+i.getId());
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("YouTube Media Import Error for :"+i.getId(), e);
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
	
	
	@Async
	private void processYouTubeImportRequest(YouTubeImportRequest ytReq) throws YouTubeDownloadException 
	{
		// Step 1 : Download YouTub Media InfoJson with the help of youtube-dl utility
		
			tydService.downlaodYouTubeRequestData(ytReq);
			
		
			if(ytReq.getTotalVideoCount() <= 0)
			{
				throw new YouTubeDownloadException("Unable to Extract ID from Requested URL");
			}
			
			// Check if Ids are Already Present into System
				
		List<ImportMediaIds> existingIdList = 	importMediaIdsRep.findAllByIdsIn(ytReq.getYouTubeIdMap().keySet());	
		
		if(!existingIdList.isEmpty())
		{	
			for ( ImportMediaIds mi : existingIdList)
			{
				ytReq.getYouTubeIdMap().remove(mi.getId());
				ytReq.setDownloadedVideoCount(ytReq.getDownloadedVideoCount()-1);
			}
		}
		
		// Prepare Importmedia ID List and Store into DB
		populateImportMediaandYouTuebRequest(ytReq);
		
		saveProcessRecrodsToDb(ytReq);
		
	}
	
	
	@Transactional
	private void saveProcessRecrodsToDb(YouTubeImportRequest ytReq)
	{
		importMediaIdsRep.saveAll(ytReq.getImportMediaIdsList());
		ytmRep.saveAll(ytReq.getYouttubeMediaList());
	
		ytReq.setImportStatus(ImportStatusType.InProgress);
		ytReq.setProcessed(true);
		ytImpRep.save(ytReq);
		
	}
	
	private void populateImportMediaandYouTuebRequest(YouTubeImportRequest ytReq)
	{
		ytReq.getYouTubeIdMap().keySet().stream().forEach(yid -> {
			try {
			
				createandPopulateMediaImportObject(yid, ytReq);
				
			} catch (Exception e) {
				// Swallowing Parallel Stream exception
				logger.error("Import Error for :"+yid, e);
			}
		});
		
		
	}
	
	private void createandPopulateMediaImportObject(String ytubeMediaid, YouTubeImportRequest ytReq)
	{
		ImportMediaIds imi = new ImportMediaIds();
		imi.setId(ytubeMediaid);
		imi.setName(null);
		imi.setImportStatus(ImportStatusType.Open);
		imi.setSourceSystem(ExternalSystemType.YOUTUBE);
		imi.setRequestId(ytReq.getId());
		imi.setMediaId(null);
		imi.setMediaType(ytReq.getMediaType());
		imi.setCreatedBy(ytReq.getCreatedBy());
		imi.setCreatedOn(ytReq.getCreatedOn());
		imi.setModifiedBy(ytReq.getModifiedBy());
		imi.setModifiedOn(ytReq.getModifiedOn());
		
		ytReq.getImportMediaIdsList().add(imi);
		
		YouTubeMedia ytm = new YouTubeMedia();
		ytm.setId(ytubeMediaid);
		ytm.setRequestId(ytReq.getId());
		ytm.setYoutubeId(ytubeMediaid);
		ytm.setMediaType(ytReq.getMediaType());
		ytm.setCreatedBy(ytReq.getCreatedBy());
		ytm.setCreatedOn(ytReq.getCreatedOn());
		ytm.setModifiedBy(ytReq.getModifiedBy());
		ytm.setModifiedOn(ytReq.getModifiedOn());
		
		ytReq.getYouttubeMediaList().add(ytm);
		
	}
	
	private int processYouTubeImportRequestItem(int pageIndex) throws ImporterException 
	{
		int result = 0;
		Date processDate = new Date();
		try {
			int pageNo = pageIndex;
			int size = env.getPerson_import_size();
			Pageable requestedPage = PageRequest.of(pageNo, size);
			
			Page <YouTubeImportRequest>  pri = ytImpRep.findAllByisProcessedIsFalse(requestedPage);
			
			List<YouTubeImportRequest> priList = pri.getContent();
			
			ForkJoinPool customThreadPool = new ForkJoinPool(env.getYouTube_download_size());
			
			customThreadPool.submit(() -> priList.parallelStream().forEach(ytReq -> {
				try {
					 processYouTubeImportRequest(ytReq);
					 ytReq.setProcessed(true);
					 ytImpRep.save(ytReq);
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("YouTube ImportRequest Error for :"+ytReq.getId(), e);
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
	

}
