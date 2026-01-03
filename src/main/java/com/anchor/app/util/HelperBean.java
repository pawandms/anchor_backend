package com.anchor.app.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.anchor.app.exceptions.UserException;
import com.anchor.app.exceptions.ValidationException;
import com.anchor.app.media.dto.ImageInfo;
import com.anchor.app.media.enums.MediaType;
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

		public Long getSequanceNoLong(SequenceType type) throws SequencerException
	    {
	    	Long seqNo = null;
	    	Sequencer seq = Sequencer.getInstance(type.name());
	    	
	    	seqNo = seq.next();
	    	if (seqNo.equals(Long.valueOf(0)))
			{
	    		seqNo = seq.next();
			}
	    	
	    	
	    	return seqNo;
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

		public Date getInfiniteDate()
		{
			Date result = null;
			try {
				String sDate1="31/12/9999";  
				result = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1); 
					
			}
			catch(Exception e)
			{
			// Swallow exception	
			}
			
			return result;
		}

		  public String getFileNameExtension(String fileName) throws ValidationException
		  {
			  String extension = null;
			  try {
				  extension = FilenameUtils.getExtension(fileName);
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return extension;
		  }
		  
		  public String getFileNameOnly(String fileName) throws ValidationException
		  {
			  String name = null;
			  try {
				  name = FilenameUtils.getBaseName(fileName);
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return name;
		  }
		

		  public MediaType getMediaTypeByExtension(String extension) throws ValidationException
		  {
			  MediaType type = null;
			  try {
				  	if((extension.equalsIgnoreCase("jpg"))
				  		||(extension.equalsIgnoreCase("jpeg"))	
				  		||(extension.equalsIgnoreCase("png"))
				    )
				  	{
				  		type = MediaType.Image;
				  	}
				  	else if((extension.equalsIgnoreCase("mp4"))
					  	)
				  	{
				  		type = MediaType.Video;
				  	}
				  	else if((extension.equalsIgnoreCase("hls"))
						  	)
					  	{
					  		type = MediaType.HlsVideo;
					  	}
				  	else if((extension.equalsIgnoreCase("mp3"))
						  	)
					  	{
					  		type = MediaType.Audio;
					  	}
				  	else if((extension.equalsIgnoreCase("doc"))
					  		||(extension.equalsIgnoreCase("docx"))	
					  		||(extension.equalsIgnoreCase("xlsx"))
					  		||(extension.equalsIgnoreCase("pdf"))
					    )
					  	{
					  		type = MediaType.Document;
					  	}
				  	else {
				  		type = MediaType.Invalid;
				  	}
				  	
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return type;
		  }

		  /**
		   * Get image dimensions without loading entire image into memory
		   * This method reads only the image header/metadata for efficient processing
		   * 
		   * @param inputStream InputStream of the image file
		   * @return ImageInfo object containing width and height, or null if unable to read
		   */
		  public ImageInfo getImageInfo(String fullFileName, InputStream inputStream) 
		  {
			ImageInfo imageInfo = new ImageInfo();
			ImageReader reader = null;
			try{
					String name = getFileNameOnly(fullFileName);
					String extension = getFileNameExtension(fullFileName);

					imageInfo.setName(name);
					imageInfo.setExtn(extension);

				ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
				if( null != iis)
				{
				Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
				if(readers.hasNext())
				{
				reader = readers.next();
					reader.setInput(iis);
					int width = reader.getWidth(0);
					int height = reader.getHeight(0);
					imageInfo.setWidth(width);
					imageInfo.setHeight(height);
					
				}		
				}	
			}
			catch(Exception e)
			{

			}
			finally {
				if( null != reader)
				{
					  reader.dispose();
				}	
					
				
		  }
		  return imageInfo;
		}


		public boolean isEmptyString(String string) {
		    return string == null || string.isEmpty();
		}

}
