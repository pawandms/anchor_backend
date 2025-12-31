package com.anchor.app.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static JsonUtil instance;
	
	private ObjectMapper mapper;
	
	public static synchronized JsonUtil getInstance()
    {
		  if (instance == null)
		  {
			  instance = new JsonUtil();
		  }
		  return instance;
    }
	 
	 private JsonUtil()
	  {
		 mapper = new ObjectMapper();
		 
	  }

	
		public <T> String pojo2Json(T pojoObject) 
		{
			String result = null;
			try {
				result = mapper.writeValueAsString(pojoObject);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

			return result;

		}
		
		public <T> Object json2Pojo(String jsonString, T className) throws IOException {

	        Object destinationObject = null;

	        try {
	            destinationObject = mapper.readValue(jsonString, className.getClass());
	        }catch (Exception e) {
	           
	            throw new RuntimeException(e.getMessage());
	        }

	        return destinationObject;

	    }

	 
}
