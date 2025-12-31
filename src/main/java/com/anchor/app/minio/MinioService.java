package com.anchor.app.minio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.anchor.app.exception.MinioServiceException;
import com.anchor.app.hls.model.Hls_PlayList;
import com.anchor.app.hls.model.Hls_Segment;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.model.Image;
import com.anchor.app.model.hls.Hls_MediaRepresentation;
import com.anchor.app.util.EnvProp;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;

@Service
public class MinioService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Autowired
	MinioClient minioClient;
	  
	@Autowired
	private EnvProp env;

	public static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");
		
	
	
	public String getMediaPlayList(String mediaId, String playListId) throws MinioServiceException 
	{
		String result = null;
		
		try {
			String minioRegion =  env.getMinioRegion();
			String objectLink = mediaId+"/"+playListId;
			GetObjectArgs objReq = null;
			if( null != minioRegion)
			{
				objReq =  GetObjectArgs.builder().bucket(env.getMediaBucketName())
					//	.region(minioRegion)
						.object(objectLink).build();
				
			}
			else {
				objReq =  GetObjectArgs.builder().bucket(env.getMediaBucketName())
						//.region(objectLink)
						.object(objectLink).build();
				
			}
			/*
			 * GetObjectResponse objResp = minioClient.getObject(objReq);
			 * System.out.println("Bucket Name:"+objResp.bucket());
			 * System.out.println("Available Bytes:"+objResp.available());
			 */
		
		result = new BufferedReader(new InputStreamReader(minioClient.getObject(objReq)))
				  .lines().collect(Collectors.joining("\n"));
		
				
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MinioServiceException(e.getMessage(), e);
			
		}
		
		return result;
	}



	
	public InputStream getSegmentStream(String mediaId, String segmentLocation, String segmentName)
			throws MinioServiceException {
		InputStream stream = null;
		 try {
		        
	        	if(( null != mediaId) && (null != segmentLocation ) && ( null != segmentName))
	        			
	        	{
	        		String objectLink = mediaId+"/"+segmentLocation+"/"+segmentName;
	        		
	        		GetObjectArgs objReq =  GetObjectArgs.builder().bucket(env.getMediaBucketName()).object(objectLink).build();
	        		
	        		stream =minioClient.getObject(objReq);
	        		
		  	       
	        	}
	        	
	          
	        } catch ( Exception e) {
	          
	        	throw new MinioServiceException(e.getMessage(), e);
	        }
		 	
		 return stream;
	}



	
	public void saveHlsStreamData(Hls_MediaRepresentation hlm) throws MinioServiceException {
		
		try {
			String bucketName = env.getMediaBucketName();
			hlm.setBucketName(bucketName);
				
			// Savr HLS PlayList and Segment to Database
			saveHlsStreamToDb(hlm, bucketName);
		
		}
		catch(Exception e)
		{
			throw new MinioServiceException(e.getMessage(), e);
		}

		
	}
	
	
	private void saveHlsStreamToDb(Hls_MediaRepresentation hlm, String bucketName) throws MinioServiceException
	{
		saveHlsPlayListToDb(hlm,bucketName);
		saveHlsSegmentToDb(hlm,bucketName);
		
		System.out.println("HLS Stream Persisted to DB");
	}
	
	private void saveHlsPlayListToDb(Hls_MediaRepresentation hlm,String bucketName) throws MinioServiceException
	{
		try {
			
			if(!hlm.getHlsPlayList().isEmpty())
			{
				for (Hls_PlayList playList : hlm.getHlsPlayList())
				{
					String objectName = playList.getMediaId()+"/"+playList.getPlayListName();
					
					putObject(playList.getPlayListPath(), objectName, bucketName);
					hlm.setPersistedPlayListCount(hlm.getPersistedPlayListCount()+1);
						
				//	logger.info("Storing PlayList objName"+objectName+" , from Path:"+playList.getPlayListPath()+", To Bucket:"+bucketName+" ... Done");
		
				}
				
				/*
				
				hlm.getHlsPlayList().stream().forEach(playList -> {
						try {
					
						String objectName = playList.getMediaId()+"/"+playList.getPlayListName();
						
						putObject(playList.getPlayListPath(), objectName, bucketName);
						hlm.setPersistedPlayListCount(hlm.getPersistedPlayListCount()+1);
							
						logger.info("Storing PlayList objName"+objectName+" , from Path:"+playList.getPlayListPath()+", To Bucket:"+bucketName+" ... Done");
						
						
						} catch (Exception e) {
							// Swallowing Parallel Stream exception
							logger.error("Import Error for :"+playList.getPlayListName(), e);
						}
					});
					
					*/
			}	
		
			
			if(hlm.getPersistedPlayListCount() == hlm.getHlsPlayList().size())
			{
				hlm.setIssegmentsPersisted(true);
			}
		
			if(!hlm.isIssegmentsPersisted())
			{
				logger.error(hlm.getPersistedPlayListCount()+ " out of "+hlm.getHlsPlayList().size()+" only Persisted");
				
				throw new MinioServiceException(hlm.getPersistedPlayListCount()+ " out of "+hlm.getHlsPlayList().size()+" only Persisted");
			}
		}
		catch(Exception e)
		{
			logger.error("HLS PlayList Save to Db Error for Media ID:"+hlm.getMediaId(), e);
		}
		
			
			
	}
	
	private void saveHlsSegmentToDb(Hls_MediaRepresentation hlm,String bucketName) throws MinioServiceException
	{
		try {
			if(!hlm.getHlsSegmentList().isEmpty())
			{
				
				for (Hls_Segment segment : hlm.getHlsSegmentList() )
				{
					String objectName = segment.getMediaId()+"/"+segment.getSegmentLocation()+"/"+segment.getSegmentName();
					
					putObject(segment.getSegPath(), objectName, bucketName);
					
					 hlm.setPersistedSegmentCount(hlm.getPersistedSegmentCount()+1);	
						
					
					//logger.info("Storing PlayList objName"+objectName+" , from Path:"+segment.getSegPath()+", To Bucket:"+bucketName+" ... Done");
				
				
				}
				
				/*
				hlm.getHlsSegmentList().stream().forEach(segment -> {
					try {
					
						String objectName = segment.getMediaId()+"/"+segment.getSegmentLocation()+"/"+segment.getSegmentName();
						
						putObject(segment.getSegPath(), objectName, bucketName);
						
						 hlm.setPersistedSegmentCount(hlm.getPersistedSegmentCount()+1);	
							
						
						logger.info("Storing PlayList objName"+objectName+" , from Path:"+segment.getSegPath()+", To Bucket:"+bucketName+" ... Done");
					
						
					} catch (Exception e) {
						logger.error("Save Segment Error for :"+segment.getId(), e);
						throw new RuntimeException(e.getMessage(), e);
					}
				});	
				
				*/
				
				if(hlm.getPersistedSegmentCount() == hlm.getHlsSegmentList().size())
				{
					hlm.setIssegmentsPersisted(true);
				}
			
				if(!hlm.isIssegmentsPersisted())
				{
					logger.error(hlm.getPersistedSegmentCount()+ " out of "+hlm.getHlsSegmentList().size()+" only Persisted");
					throw new MinioServiceException(hlm.getPersistedSegmentCount()+ " out of "+hlm.getHlsSegmentList().size()+" only Persisted");
				}
				
				
				
			}	
		}
		catch(Exception e)
		{
			// Delete Partially Inserted Segment from DB
		//	deleteHlsSegmentData(hlm.getMediaId());
			
			logger.error("HLS Segement Save to DB Error for ID:"+hlm.getMediaId(), e);
			throw new MinioServiceException(e.getMessage(), e);
		}
		
		
		
	}
	
	/**
	 * put Object to Minio Object Store
	 * @param filePath
	 * @param fileName
	 * @param objectName
	 * @param bucketName
	 */
	private void putObject(Path filePath,String objectName, String bucketName)
	{
		
		    try {
		    	
		    	createBucketIfNotExists(bucketName);
		    	UploadObjectArgs.Builder builder = UploadObjectArgs.builder().bucket(bucketName)
		    			  .object(objectName).filename(filePath.toString());
		    	 
		    	 minioClient.uploadObject(builder.build());
		    	
	        } catch (Exception e) {
	           throw new RuntimeException(e.getMessage());
	        }
		
	}


	private void putObject(String objectName, String bucketName, InputStream stream)
	{
		
		    try {
		    	
		    	createBucketIfNotExists(bucketName);
		    	
		    	Path tempFile = Files.createTempFile("temp", ".jpg");
		        
		    	Files.copy(stream, tempFile, StandardCopyOption.REPLACE_EXISTING);
		        
		    	putObject(tempFile,objectName, bucketName);
		    	
		    	/*
		    	PutObjectArgs.Builder builder = PutObjectArgs.builder().bucket(bucketName)
		    			  .object(objectName)
		    			  .stream(stream, -1, 5);
		    			 
		    	 
		    	 minioClient.putObject(builder.build());
		    	 
		    	 */
		    	
	        } catch (Exception e) {
	           throw new RuntimeException(e.getMessage());
	        }
		
	}
	
	private void createBucketIfNotExists(String bucketName) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException, IOException
	{
		 boolean isExist = minioClient
		          .bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
		      if (!isExist) {
		      
		    	  minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
		      }
		      
	}

	public void addImage(Image img, InputStream stream) throws MinioServiceException {
		
		String objectName = img.getId();
		String ImgbucketName = env.getImgBucketName();
		putObject(objectName,ImgbucketName, stream);
		
	}



	
	public InputStream getImage(String imageId) throws MinioServiceException {

		InputStream stream = null;
		 try {
			
		     GetObjectArgs objReq =  GetObjectArgs.builder().bucket(env.getImgBucketName()).object(imageId).build();
	         stream =minioClient.getObject(objReq);
	        
		 } catch ( Exception e) {
	          
	        	throw new MinioServiceException(e.getMessage(), e);
	      }
		 	
		 return stream;

	
	}



	public String addMedia(String mediaID,String fileName,String bucketName, Path mediaLocation) throws MinioServiceException {
		
		String result = null;
		try {

			String objectName = mediaID+"/"+fileName;
			
			putObject(mediaLocation,objectName, bucketName);
			
			result = objectName;
			
		} catch (Exception e) {
			logger.error("Save Media Error for :"+mediaID, e);
			throw new MinioServiceException(e.getMessage(), e);
		}

		return result;
		
	}



	
	public ResponseEntity<?> getMedia(String contentID, String bucketName, long fileSize, String range) throws MinioServiceException {
		
		ResponseEntity<?> response = null;
		 long rangeStart = 0;
	        long rangeEnd = fileSize;
	        byte[] data;
	      
	        try {

	        	if( range != null)
	        	{
	        		/*
	        		 Matcher matcher = RANGE_PATTERN.matcher(range);
	        		
	        		 
	     	        if (matcher.matches()) {
	      	          String startGroup = matcher.group("start");
	      	          rangeStart = startGroup.isEmpty() ? rangeStart : Integer.valueOf(startGroup);
	      	          rangeStart = rangeStart < 0 ? 0 : rangeStart;

	      	          String endGroup = matcher.group("end");
	      	          rangeEnd = endGroup.isEmpty() ? fileSize : Integer.valueOf(endGroup);
	      	          rangeEnd = rangeEnd > fileSize - 1 ? fileSize - 1 : rangeEnd;
	      	        }
	      	        */
	        		
	        		String[] ranges = range.split("-");
	                rangeStart = Long.parseLong(ranges[0].substring(6));
	                if (ranges.length > 1) {
	                    rangeEnd = Long.parseLong(ranges[1]);
	                } else {
	                    rangeEnd = fileSize - 1;
	                    
	                }
	                if (fileSize < rangeEnd) {
	                    rangeEnd = fileSize - 1;
	                }
	      	        
	      	       
	        	}
	        	else {
	        		rangeEnd = fileSize;
	        	}
	        	
	        	  String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
	            
			     GetObjectArgs objReq =  GetObjectArgs.builder().bucket(bucketName)
			    		 				.object(contentID)
			    		 				.offset(rangeStart)
			    		 				.length(rangeEnd)
			    		 				.build();
		      //  GetObjectResponse resp  =minioClient.getObject(objReq);
			     InputStream  stream = minioClient.getObject(objReq);
			  	InputStreamResource  streamResource = new InputStreamResource(stream);
			   
			  	
			  	response = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
			  	        .cacheControl(CacheControl.maxAge(10000,TimeUnit.SECONDS ))
			            .header(env.CONTENT_TYPE, "video/vnd.dlna.mpeg-tts")
			            .header(env.CONTENT_TYPE, env.VIDEO_CONTENT + contentID)
		                .header(env.ACCEPT_RANGES, env.BYTES)
		                .header(env.CONTENT_LENGTH, contentLength)
		                .header(env.CONTENT_RANGE, env.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
		                .body(streamResource);

			     
	        }
	        catch(Exception e )
	        {
	        	
	        	response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exception:"+e.getMessage());
	    			  	
	        }
	  
	        return response;
	        	
	}



	
	public void deleteMediaContent(String contentID, String bucketName) throws MinioServiceException {
		try {
			
			
			// Step 1 : Get All object list for content ID
			Iterable<Result<Item>> getResult = minioClient.listObjects(
				    ListObjectsArgs.builder().bucket(bucketName).prefix(contentID).recursive(true).build());
			
			List<DeleteObject> delobjList =  createDeleteObjectList(getResult);
			
			if(!delobjList.isEmpty())
			{
				Iterable<Result<DeleteError>> results = minioClient.removeObjects(
				        RemoveObjectsArgs.builder().bucket(bucketName).objects(delobjList).build());
				
				
				StringBuilder sb = new StringBuilder();
				sb.append("Delete Media Error for ContentID:"+contentID);
				
				int errorCount = 0;
				
				for (Result<DeleteError> result : results) {
				  DeleteError error = result.get();
				  
				  sb.append(error.objectName()+",");
				  errorCount = errorCount+1;
				 
				  
				}
				
				if(errorCount > 0)
				{
					throw new MinioServiceException("Delete Object Error"+sb.toString());
				}
				
			}
			 
			//int itemCount = printIterableCount(results);
			
			//logger.info("Deleting folder with ID:"+contentID+" , Object Count:"+itemCount);
			
			
			
		}
		catch(Exception e)
		{
			logger.error("Error Deleting Content for ID:"+contentID, e);
		}
		
	}
	
	
	private List<DeleteObject> createDeleteObjectList(Iterable<Result<Item>> results)
	{
		List<DeleteObject> deleteObjectList = new LinkedList<>();
		
	    try {
	    	 for (Result<Item> item : results) {
	 	        DeleteObject delobj = new DeleteObject(item.get().objectName());
	 	        deleteObjectList.add(delobj);
	 	    }
	    }
	    catch(Exception e)
	    {
	    	logger.error("Error Creating Delete Object List");
	    }
	   
	    
	    return deleteObjectList;
	}


	private List<DeleteObject> createImageDeleteObjectList(List<MediaImage> imageList)
	{
		List<DeleteObject> deleteObjectList = new LinkedList<>();
		
	    try {
	    	 for (MediaImage item : imageList) {
	 	        DeleteObject delobj = new DeleteObject(item.getId());
	 	        deleteObjectList.add(delobj);
	 	    }
	    }
	    catch(Exception e)
	    {
	    	logger.error("Error Creating Delete Object List");
	    }
	   
	    
	    return deleteObjectList;
	}

	
	public void deleteImage(List<MediaImage> imageList) throws MinioServiceException {
		
		try {
			
			if ( ( null != imageList) && (!imageList.isEmpty()))
			{
				String bucketName = env.getImgBucketName();
				List<DeleteObject> delobjList =  createImageDeleteObjectList(imageList);
				
				if(!delobjList.isEmpty())
				{
					Iterable<Result<DeleteError>> results = minioClient.removeObjects(
					        RemoveObjectsArgs.builder().bucket(bucketName).objects(delobjList).build());
					
					
					StringBuilder sb = new StringBuilder();
					int errorCount = 0;
					
					for (Result<DeleteError> result : results) {
					  DeleteError error = result.get();
					  
					  sb.append(error.objectName()+",");
					  errorCount = errorCount+1;
					 
					  
					}
					
					if(errorCount > 0)
					{
						throw new MinioServiceException("Delete Object Error"+sb.toString());
					}
					
				}
				 
				
			}

			//int itemCount = printIterableCount(results);
			
			//logger.info("Deleting folder with ID:"+contentID+" , Object Count:"+itemCount);
			
			
			
		}
		catch(Exception e)
		{
			logger.error("Error Deleting Image for ID", e);
		}
		

		
	}



	public void putObjectStream(String objectName, String bucketName, InputStream stream) throws MinioServiceException {
		try {
	    	
	    	createBucketIfNotExists(bucketName);
	    	
	    	
	    	PutObjectArgs.Builder builder = PutObjectArgs.builder().bucket(bucketName)
	    			  .object(objectName)
	    			  .stream(stream, -1, 10485760);
	    			 
	    	 
	    	 minioClient.putObject(builder.build());
	    	 
	    	 
	    	
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
           throw new MinioServiceException(e.getMessage(), e);
        }
		
	}



	public InputStream getAttachment(String attachmentID) throws MinioServiceException {
		
		InputStream stream = null;
		 try {
			
		     GetObjectArgs objReq =  GetObjectArgs.builder().bucket(env.getMsgBucketName()).object(attachmentID).build();
	         stream =minioClient.getObject(objReq);
	        
		 } catch ( Exception e) {
	          
	        	throw new MinioServiceException(e.getMessage(), e);
	      }
		 	
		 return stream;

	}

	public InputStream getVideoAttachment(String attachmentID, long rangeStart,long rangeEnd) throws MinioServiceException 
	{
		
		InputStream stream = null;
		 try {
			
		     //GetObjectArgs objReq =  GetObjectArgs.builder().bucket(env.getMsgBucketName()).object(attachmentID).build();
		     GetObjectArgs objReq =  GetObjectArgs.builder().bucket(env.getMsgBucketName())
		 				.object(attachmentID)
		 				.offset(rangeStart)
		 				.length(rangeEnd)
		 				.build();

		     
	         stream =minioClient.getObject(objReq);
	         
	        
		 } catch ( Exception e) {
	          
	        	throw new MinioServiceException(e.getMessage(), e);
	      }
		 	
		 return stream;

	}
	
	public InputStream getProfileImage(String contentID) throws MinioServiceException {
		
		InputStream stream = null;
		 try {
			
		     GetObjectArgs objReq =  GetObjectArgs.builder().bucket(env.getUserProfileBucketName()).object(contentID).build();
	         stream =minioClient.getObject(objReq);
	        
		 } catch ( Exception e) {
	          
	        	throw new MinioServiceException(e.getMessage(), e);
	      }
		 	
		 return stream;

	}
	


}
