package com.anchor.app.msg.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.anchor.app.enums.SequenceType;
import com.anchor.app.exception.MinioServiceException;
import com.anchor.app.minio.MinioService;
import com.anchor.app.msg.enums.MsgAttachmentType;
import com.anchor.app.msg.exceptions.ContentServiceException;
import com.anchor.app.msg.model.Attachment;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class ContentService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MinioService minioService;

	@Autowired
	private HelperBean helper;
	
	@Autowired
	private EnvProp env;

	@Autowired
	private MsgService msgService;
	
    public static final String VIDEO = "/video";

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int CHUNK_SIZE = 314700;
    public static final int BYTE_RANGE = 1024;

	
	
	public List<Attachment> saveMsgAttachments(String msgID, String userID, Date modDate, List<MultipartFile> attachments)
			throws ContentServiceException {
		
		List<Attachment> resultList = new ArrayList<>();
		
		try {
			
				if(!attachments.isEmpty())
				{
					attachments.stream().forEach(mfile -> {
						try {
							
							String name = helper.getFileNameOnly(mfile.getOriginalFilename());
							String extension = helper.getFileNameExtension(mfile.getOriginalFilename());
							
							if( (null != name) && (!helper.isEmptyString(name)) && (null != extension) && (!helper.isEmptyString(extension)))
							{
								
								Attachment attachment = new Attachment();
								String id = helper.getSequanceNo(SequenceType.MsgAttachment);
								attachment.setId(id);
								attachment.setName(name);
								attachment.setExtension(extension);
								
								// Identify Media Type of Attachment
								
								MsgAttachmentType type = helper.getAttachmentTypeByName(extension);
								attachment.setType(type);
								if(type == MsgAttachmentType.Invalid)
								{
									attachment.setValid(false);
								}
								else {
									attachment.setValid(true);
								}
								
								attachment.setCreatedBy(userID);
								attachment.setCreatedOn(modDate);
								attachment.setModifiedBy(userID);
								attachment.setModifiedOn(modDate);
							
								if(attachment.isValid())
								{
									String msgBucketName = env.getMsgBucketName();
									String contentID = attachment.getId()+"."+attachment.getExtension().toLowerCase();
									attachment.setContentID(contentID);
									attachment.setBucketName(msgBucketName);
									attachment.setSizeInBytes(mfile.getSize());
									minioService.putObjectStream(contentID, msgBucketName, mfile.getInputStream());	
									resultList.add(attachment);
								}
								
								
							}
					
								
						}
						catch(Exception e)
						{
							throw new RuntimeException(e.getMessage(), e);
						}
						
						
					});
				}
			
			
					
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ContentServiceException(e.getMessage(), e);
		}
		
		return resultList;

	}

	
	public InputStream getContent(String contentID) throws ContentServiceException {
		
		InputStream imgStream = null;
		 try {
		        
	        	if( null == contentID)
	        	{
	        		throw new ContentServiceException("Image ID can not be null");
	        	}
	        	
	        	imgStream = minioService.getAttachment(contentID);
	          
	        } catch (  MinioServiceException e) {
	          
	        	throw new ContentServiceException(e.getMessage(), e);
	        }
		 	
		 return imgStream;
		
	}


	public ResponseEntity<?> getVideoContent(String contentID, String extension, long cntLength, String range) throws ContentServiceException {
		
		ResponseEntity<?> response = null;
		
		 try {
		        
	        	if(( null == contentID))
	        	{
	        		throw new ContentServiceException("Message or Image ID can not be null");
	        	}
	        
	        	/*
	        	Attachment atch = msgService.getAttachmentById(msgId, contentID);
	        	
	        	if( null == atch)
	        	{
	        		throw new ContentServiceException("Invalid Message or Content ID:"+msgId+", "+contentID);
	        	}
	        	*/
	        	long rangeStart = 0;
		        long rangeEnd = 0;
		        long CHUNK_SIZE = 314700;
		     	long fileSize = cntLength;
	        	if( range != null)
	        	{
	        		String[] ranges = range.split("-");
	                rangeStart = Long.parseLong(ranges[0].substring(6));
	                if (ranges.length > 1) {
	                    rangeEnd = Long.parseLong(ranges[1]);
	                } else {
	                    rangeEnd = fileSize-1;
	                    
	                }
	                if (fileSize < rangeEnd) {
	                    rangeEnd = fileSize - 1;
	                }
	      	        
	      	       
	        	}
	        	else {
	        			rangeEnd = fileSize;	
	        		
	        	}
	        	
	        	  String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
	        	  
	        	  InputStream stream = minioService.getVideoAttachment(contentID, rangeStart, rangeEnd);
	        	  
	        //	  byte[] bytes = IOUtils.toByteArray(stream);
	        	  
	        	  InputStreamResource  streamResource = new InputStreamResource(stream);
			   
	        	response = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
			  	        .cacheControl(CacheControl.maxAge(10000,TimeUnit.SECONDS ))
			            .header(env.CONTENT_TYPE, env.VIDEO_CONTENT +extension)
		                .header(env.ACCEPT_RANGES, env.BYTES)
		                .header(env.CONTENT_LENGTH, contentLength)
		                .header(env.CONTENT_RANGE, env.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
		                .body(streamResource);
	          
			  	logger.info("Sending Video for ContentID :"+contentID+" With Rangestart:"+rangeStart+" , End:"+rangeEnd+", Total:"+fileSize);
				  	
			  	
	        } catch (Exception e) {
	          
	        	throw new ContentServiceException(e.getMessage(), e);
	        }
		 	
		 return response;
		
	}
	
	
	/**
	 * StreamingReponse Body Test API
	 * @param msgId
	 * @param contentID
	 * @param range
	 * @return
	 * @throws ContentServiceException
	 */
	public ResponseEntity<StreamingResponseBody> getVideoContentStreaming(String msgId, String contentID, String range) throws ContentServiceException {
		
		ResponseEntity<StreamingResponseBody> response = null;
		
		 try {
		        
	        	if((null == msgId) ||( null == contentID))
	        	{
	        		throw new ContentServiceException("Message or Image ID can not be null");
	        	}
	        	
	        	Attachment atch = msgService.getAttachmentById(msgId, contentID);
	        	
	        	if( null == atch)
	        	{
	        		throw new ContentServiceException("Invalid Message or Content ID:"+msgId+", "+contentID);
	        	}
	        	long rangeStart = 0;
		        long rangeEnd = 0;
		        long CHUNK_SIZE = 314700;
		     	long fileSize = atch.getSizeInBytes();
	        	if( range != null)
	        	{
	        		String[] ranges = range.split("-");
	                rangeStart = Long.parseLong(ranges[0].substring(6));
	                if (ranges.length > 1) {
	                    rangeEnd = Long.parseLong(ranges[1]);
	                } else {
	                    rangeEnd = fileSize-1;
	                    
	                }
	                if (fileSize < rangeEnd) {
	                    rangeEnd = fileSize - 1;
	                }
	      	        
	      	       
	        	}
	        	else {
	        			rangeEnd = fileSize;	
	        		
	        	}
	        	
	        	  String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
	        	  
	        	  InputStream stream = minioService.getVideoAttachment(contentID, rangeStart, rangeEnd);
	        	  
	        //	  byte[] bytes = IOUtils.toByteArray(stream);
	        	  
	        	  //InputStreamResource  streamResource = new InputStreamResource(stream);
	        	  final HttpHeaders responseHeaders = new HttpHeaders();
	        	  responseHeaders.add(env.CONTENT_TYPE, env.VIDEO_CONTENT + atch.getExtension());
	        	  responseHeaders.add(env.ACCEPT_RANGES, env.BYTES);
	        	  responseHeaders.add(env.CONTENT_LENGTH, contentLength);
	        	  responseHeaders.add(env.CONTENT_RANGE, env.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize);
	    

	        	    StreamingResponseBody responseBody = outputStream -> {

	        	        int numberOfBytesToWrite;
	        	        byte[] data = new byte[1024];
	        	        while ((numberOfBytesToWrite = stream.read(data, 0, data.length)) != -1) {
	        	            System.out.println("Writing some bytes..");
	        	            outputStream.write(data, 0, numberOfBytesToWrite);
	        	        }

	        	        stream.close();
	        	        outputStream.flush();
	        	    };
	    
	        
	        	response = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
			  	        .cacheControl(CacheControl.maxAge(10000,TimeUnit.SECONDS ))
			            .header(env.CONTENT_TYPE, "video/mp4")
		                .header(env.ACCEPT_RANGES, env.BYTES)
		                .header(env.CONTENT_LENGTH, contentLength)
		                .header(env.CONTENT_RANGE, env.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
		                .body(responseBody);
	          
			  	logger.info("Response content for ContentID :"+contentID+" With Range Request start:"+rangeStart+" , End:"+rangeEnd);
				  	
			  	
	        } catch (Exception e) {
	          
	        	throw new ContentServiceException(e.getMessage(), e);
	        }
		 	
		 return response;
		
	}

	
	public Attachment saveUserProfile(String userID, String existingContentID,  Date modDate, MultipartFile mfile)
			throws ContentServiceException {
		
		Attachment result = null;
		
		try {
			
				if(!mfile.isEmpty())
				{
					String name = helper.getFileNameOnly(mfile.getOriginalFilename());
					String extension = helper.getFileNameExtension(mfile.getOriginalFilename());
					
					if(( null == name )|| (null == extension))
					{
						throw new ContentServiceException("Invalid Attachment with ID:"+mfile.getName());
					}
					result = new Attachment();
					String id = null;
					if( null != existingContentID)
					{
						id = existingContentID;
						result.setContentID(existingContentID);
					}
					else {
						id = helper.getSequanceNo(SequenceType.ProfileImage);	
					}
					result.setId(id);
					result.setName(name);
					result.setExtension(extension);
					
					MsgAttachmentType type = helper.getAttachmentTypeByName(extension);
					result.setType(type);
					if(type == MsgAttachmentType.Invalid)
					{
						result.setValid(false);
					}
					else {
						result.setValid(true);
					}
					
					result.setCreatedBy(userID);
					result.setCreatedOn(modDate);
					result.setModifiedBy(userID);
					result.setModifiedOn(modDate);
					
					if(result.isValid())
					{
						String profileBucketName = env.getUserProfileBucketName();
						if( null == result.getContentID())
						{
							String contentID = result.getId()+"."+result.getExtension().toLowerCase();
							result.setContentID(contentID);
								
						}
						result.setBucketName(profileBucketName);
						result.setSizeInBytes(mfile.getSize());
						minioService.putObjectStream(result.getContentID(), profileBucketName, mfile.getInputStream());	
					}
					
					
				}
			
					
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ContentServiceException(e.getMessage(), e);
		}
		
		return result;

	}
	
	public InputStream getProfileImage(String contentID) throws ContentServiceException {
		
		InputStream imgStream = null;
		 try {
		        
	        	if( null == contentID)
	        	{
	        		throw new ContentServiceException("Image ID can not be null");
	        	}
	        	
	        	imgStream = minioService.getProfileImage(contentID);
	          
	        } catch (  MinioServiceException e) {
	          
	        	throw new ContentServiceException(e.getMessage(), e);
	        }
		 	
		 return imgStream;
		
	}

}
