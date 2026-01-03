package com.anchor.app.media.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.util.EnvProp;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Service
public class StorageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
	MinioClient minioClient;
	  
	@Autowired
	private EnvProp env;

    public void putObject(String objectName, String bucketName, InputStream stream)
	{
		
		    try {
		    	
		    	createBucketIfNotExists(bucketName);
		    	
		    	Path tempFile = Files.createTempFile("temp", ".tmp");
		        
		    	Files.copy(stream, tempFile, StandardCopyOption.REPLACE_EXISTING);
		        
		    	putObject(tempFile,objectName, bucketName);
		    	
	        } catch (Exception e) {
	           throw new RuntimeException(e.getMessage());
	        }
		
	}

    private void putObject(Path filePath,String objectName, String bucketName)
	{
		
		    try {
		    	
		    	UploadObjectArgs.Builder builder = UploadObjectArgs.builder().bucket(bucketName)
		    			  .object(objectName).filename(filePath.toString());
		    	 
		    	 minioClient.uploadObject(builder.build());
		    	
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

}
