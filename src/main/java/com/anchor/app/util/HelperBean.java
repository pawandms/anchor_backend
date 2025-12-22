package com.anchor.app.util;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.GenderType;
import com.anchor.app.enums.MsgAttachmentType;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.exceptions.SequencerException;
import com.anchor.app.exceptions.UserException;
import com.anchor.app.exceptions.ValidationException;
import com.anchor.app.oauth.exceptions.ConversionException;
import com.anchor.app.oauth.model.User;
import com.anchor.app.sequencer.service.Sequencer;
import com.anchor.app.vo.MediaImage;
import com.anchor.app.vo.SearchUserVo;
import com.anchor.app.vo.UserVo;

@Component
@Scope("singleton")
public class HelperBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    PasswordEncoder passwordEncoder;


	public SearchUserVo convertUserToSearchUser(User u) throws ConversionException
			{
				SearchUserVo result = null;
				try {
					
					if( null != u)
					{
						result = new SearchUserVo();
						result.setUserID(u.getUid());
						result.setFirstName(u.getFirstName());
						result.setLastName(u.getLastName());

						if( null != u.getSrcface())
						{
							MediaImage img = new MediaImage(u.getSrcface(), EntityType.UserProfile, u.getSrcface());
							
							result.setProfileImage(img);	
						}
						if(null != u.getGender())
						{
							result.setGender(GenderType.getType(u.getGender()));	
						}
						else {
							result.setGender(GenderType.Unknown);
						}

						if ( null != u.getProfileType())
						{
							result.setProfileType(u.getProfileType());
						}
						
						result.setLastLogin(u.getLastLogin());
						
					}
				}
				catch(Exception e )
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				return result;
				
			}

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
	    	seqStr = String.format("%d-%015d", year, seqNo);
	    	
	    	return seqStr;
	    }


		public Long getLongSequanceNo(SequenceType type) throws SequencerException
	    {
			Long result = null;
	    	Sequencer seq = Sequencer.getInstance(type.name());
	    	
	    	Long seqNo = seq.next();
	    	if (seqNo.equals(Long.valueOf(0)))
			{
	    		seqNo = seq.next();
			}

			result = seqNo;
	    	
	    	return result;
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
		
		
		/**
		 * Get UUID by Appending current time so it will be change every time
		 * @param value
		 * @return
		 */
		public UUID getEncryptedUUIDFromString(String value)
		{
			UUID result = null;
			try {
				Date now = new Date();
				String encValue = value+"_"+now.getTime();
				result =  UUID.nameUUIDFromBytes(encValue.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
			
				result = UUID.randomUUID();
			}
			
			return result;
		}

		public boolean isEmptyString(String string) {
		    return string == null || string.isEmpty();
		}
		
		public boolean validateEmailAddress(String email)
			{
				boolean result = false ;
					
				try {
					String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(email);
					result =  matcher.matches();
				}
				catch(Exception e)
				{
					
				}
				
				return result;
			}

			public void populateUserDetailsToUserVo(User user, UserVo userVo) throws ValidationException
		{
		
			try {
				if( null == user)
				{
					throw new ValidationException("User Object can not be null for conversion");
				}	
				userVo.setEmail(user.getEmail());
				userVo.setFirstName(user.getFirstName());
				userVo.setGender(GenderType.getType(user.getGender()));
				userVo.setId(user.getId());
				userVo.setLastName(user.getLastName());
				userVo.setUserName(user.getUserName());
				
			}
			catch(Exception e)
			{
				throw new ValidationException(e.getMessage(), e);
			}
			
			
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
		  
		  public MsgAttachmentType getAttachmentTypeByName(String extension) throws ValidationException
		  {
			  MsgAttachmentType type = null;
			  try {
				  	if((extension.equalsIgnoreCase("jpg"))
				  		||(extension.equalsIgnoreCase("jpeg"))	
				  		||(extension.equalsIgnoreCase("png"))
				    )
				  	{
				  		type = MsgAttachmentType.Image;
				  	}
				  	else if((extension.equalsIgnoreCase("mp4"))
					  	)
				  	{
				  		type = MsgAttachmentType.Video;
				  	}
				  	else if((extension.equalsIgnoreCase("hls"))
						  	)
					  	{
					  		type = MsgAttachmentType.HlsVideo;
					  	}
				  	else if((extension.equalsIgnoreCase("mp3"))
						  	)
					  	{
					  		type = MsgAttachmentType.Audio;
					  	}
				  	else if((extension.equalsIgnoreCase("doc"))
					  		||(extension.equalsIgnoreCase("docx"))	
					  		||(extension.equalsIgnoreCase("xlsx"))
					  		||(extension.equalsIgnoreCase("pdf"))
					    )
					  	{
					  		type = MsgAttachmentType.Document;
					  	}
				  	else {
				  		type = MsgAttachmentType.Invalid;
				  	}
				  	
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return type;
		  }
			
			
	}
