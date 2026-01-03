package com.anchor.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anchor.app.util.EnvProp;

import io.minio.MinioClient;

@Configuration
public class MinioConfig {

	@Autowired
	private EnvProp env;
	
	  @Bean
	    public MinioClient generateMinioClient() {
	        try {
	        	
	        	String minioRegion =env.getMinioRegion();
	        	MinioClient minioClient = null;
	        	
	        	if( null != minioRegion)
	        	{
	        		minioClient =
					          MinioClient.builder()
					              .endpoint(env.getMinioUrl())
					              .credentials(env.getMinioAccessKey(), env.getMinioSecretKey())
					              .region(minioRegion)
					              .build();
	        	}
	        	else {
	        		String accessKey = env.getMinioAccessKey();
	        		String secretKey = env.getMinioSecretKey();
	        		minioClient =
					          MinioClient.builder()
					              .endpoint(env.getMinioUrl())
					              .credentials(accessKey, secretKey)
					            //  .region(minioRegion)
					              .build();
	        	}
	        	
			      

	            return minioClient;
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	        }

	    }
}
