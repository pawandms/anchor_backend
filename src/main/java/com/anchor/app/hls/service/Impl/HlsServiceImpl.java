package com.anchor.app.hls.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anchor.app.exception.HlsServiceException;
import com.anchor.app.hls.model.Hls_PlayList;
import com.anchor.app.hls.model.Hls_Segment;
import com.anchor.app.hls.repository.Hls_PlayListRepository;
import com.anchor.app.hls.repository.Hls_SegmentRepository;
import com.anchor.app.hls.service.HlsService;
import com.anchor.app.model.hls.Hls_MediaRepresentation;

@Service
public class HlsServiceImpl implements HlsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Hls_PlayListRepository hlsPlayListRepo;
	
	@Autowired
	private Hls_SegmentRepository hlsSegmentRepo;
	
	
	@Override
	public void saveHlsStreamData(Hls_MediaRepresentation hlm) throws HlsServiceException {
		
		try {
			
			// Savr HLS PlayList and Segment to Database
			saveHlsStreamToDb(hlm);
		}
		catch(Exception e)
		{
			throw new HlsServiceException(e.getMessage(), e);
		}
	}

	private void saveHlsStreamToDb(Hls_MediaRepresentation hlm) throws HlsServiceException
	{
		saveHlsSegmentToDb(hlm);
		saveHlsPlayListToDb(hlm);
		
		System.out.println("HLS Stream Persisted to DB");
	}
	
	@Transactional
	private void saveHlsPlayListToDb(Hls_MediaRepresentation hlm)
	{
		
			if(!hlm.getHlsPlayList().isEmpty())
			{
				hlsPlayListRepo.saveAll(hlm.getHlsPlayList());
			}	
		
			
			hlm.setPersistedPlayListCount(hlm.getHlsPlayList().size());
			hlm.setIsplayListPersisted(true);
		
	}
	
	private void saveHlsSegmentToDb(Hls_MediaRepresentation hlm) throws HlsServiceException
	{
		try {
			if(!hlm.getHlsSegmentList().isEmpty())
			{
				hlm.getHlsSegmentList().stream().forEach(segment -> {
					try {
					
						File segFile =segment.getSegPath().toFile();
						
						 segment.setData(new Binary(BsonBinarySubType.BINARY, FileUtils.readFileToByteArray(segFile)));
						 hlsSegmentRepo.save(segment);
						
						 logger.info("Segment Save to DB with ID:"+segment.getId());
						 hlm.setPersistedSegmentCount(hlm.getPersistedSegmentCount()+1);	
						
						
					} catch (Exception e) {
						logger.error("Save Segment Error for :"+segment.getId(), e);
						throw new RuntimeException(e.getMessage(), e);
					}
				});	
				
				if(hlm.getPersistedSegmentCount() == hlm.getHlsSegmentList().size())
				{
					hlm.setIssegmentsPersisted(true);
				}
			
				if(!hlm.isIssegmentsPersisted())
				{
					throw new HlsServiceException(hlm.getPersistedSegmentCount()+ " out of "+hlm.getHlsSegmentList().size()+" only Persisted");
				}
				
			}	
		}
		catch(Exception e)
		{
			// Delete Partially Inserted Segment from DB
			deleteHlsSegmentData(hlm.getMediaId());
			
			throw new HlsServiceException(e.getMessage(), e);
		}
		
		
		
	}

	@Override
	public String getMediaPlayList(String mediaId, String playListId) throws HlsServiceException {
		
		String result = null;	
		
		if(( null != mediaId) && (null != playListId))
		{
			String id = mediaId+"_"+playListId;
			Optional<Hls_PlayList> optmstPlayList = 	hlsPlayListRepo.findById(id);
			
			if(optmstPlayList.isPresent())
			{
				Hls_PlayList playlist = optmstPlayList.get();
				
				result = playlist.getPlayListTxt();
				
			}
		}
		
		return result;
	}

	@Override
	public InputStream getSegmentStream(String mediaId, String segmentLocation, String segmentName)
			throws HlsServiceException {
		
		InputStream stream = null;
		 try {
		        
	        	if(( null != mediaId) && (null != segmentLocation ) && ( null != segmentName))
	        			
	        	{
	        		String segId = mediaId+"_"+segmentLocation+"_"+segmentName;
	        		
	        		Optional<Hls_Segment> seg = hlsSegmentRepo.findById(segId);
    	        	
		        	if(!seg.isPresent())
		        	{
		        		throw new HlsServiceException("Segment not present for ID:"+segId);
		        	}
		        	
		        	 stream = new ByteArrayInputStream(seg.get().getData().getData());	
		  	       
	        	}
	        	
	          
	        } catch ( HlsServiceException e) {
	          
	        	throw e;
	        }
		 	
		 return stream;

	}

	@Override
	public void deleteHlsSegmentData(String mediaId) throws HlsServiceException {
			
		hlsSegmentRepo.deleteByMediaId(mediaId);
	}
}
