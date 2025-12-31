package com.anchor.app.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateDeserializer extends JsonDeserializer<Date> {

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Date result = null;	
		String dateStr = p.getText();
		try {
			result = formatter.parse(dateStr);
		}
		catch (Exception e) 
		{
		
		}
		
		return result;
	}

}
