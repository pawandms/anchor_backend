package com.anchor.app.config;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.anchor.app.util.EnvProp;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Service
public class FireBaseConfig {
	
	@Autowired
	private EnvProp env;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Bean
	  FirebaseMessaging firebaseMessaging() 
	  {
		  FirebaseMessaging fireBaseService = null;
		  try {
			  	String firebaseConfigFilePath = env.getFireBaseServiceFilePath();
			  	
			  	 InputStream resource = new ClassPathResource(firebaseConfigFilePath).getInputStream();
	            FirebaseOptions options = FirebaseOptions.builder()
	            		.setCredentials(GoogleCredentials.fromStream(resource))
	            		.build();
	            
	            FirebaseApp fireBaseApp =   FirebaseApp.initializeApp(options);
	                logger.info("Firebase application initialized");
	                
	                fireBaseService =  FirebaseMessaging.getInstance(fireBaseApp);
	        
		  }
		  catch(Exception e )
		  {
			  logger.error(e.getMessage(), e);
	            throw new RuntimeException(e.getMessage(), e);
		  }
		  
		
		  return fireBaseService;
	      
	  }

}
