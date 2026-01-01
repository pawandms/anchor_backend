package com.anchor.app.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.anchor.app.exceptions.UserException;
import com.anchor.app.sequencer.exceptions.SequencerException;
import com.anchor.app.sequencer.service.Sequencer;
import com.anchor.app.util.enums.SequenceType;

@Component
@Scope("singleton")
public class HelperBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    PasswordEncoder passwordEncoder;


    	    public String getSequanceNo(SequenceType type) throws SequencerException
	    {
	    	String seqStr = null;
	    	Sequencer seq = Sequencer.getInstance(type.name());
	    	
	    	Long seqNo = seq.next();
	    	if (seqNo.equals(Long.valueOf(0)))
			{
	    		seqNo = seq.next();
			}
	    	
	    	int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH);
	    	seqStr = year+"-"+month+"-"+String.valueOf(seqNo);
	    	
	    	return seqStr;
	    }

		
		public String encriptPassword(String plainTxt) throws UserException
		{
			String encriptedTxt = null;
			if( null == plainTxt)
			{
				throw new UserException("Empty Password not allowed");
				
			}
			encriptedTxt = 	passwordEncoder.encode(plainTxt);
			
			return encriptedTxt;
		}

		public UUID getUUIDFromString(String value)
		{
			UUID result = null;
			try {
				result =  UUID.nameUUIDFromBytes(value.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
			
				result = UUID.randomUUID();
			}
			
			return result;
		}

}
