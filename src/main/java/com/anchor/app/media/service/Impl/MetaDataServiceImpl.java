package com.anchor.app.media.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.anchor.app.enums.SequenceType;
import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.exception.MetaDataServiceException;
import com.anchor.app.exception.SequencerException;
import com.anchor.app.exception.ValidationException;
import com.anchor.app.importer.model.vo.MetaDataEntryVo;
import com.anchor.app.importer.model.vo.MetaDataVo;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaMetaData;
import com.anchor.app.media.repository.MediaMetaDataRepository;
import com.anchor.app.media.repository.MediaRepository;
import com.anchor.app.media.service.MetaDataService;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.util.HelperBean;
import com.anchor.app.vo.ErrorMsg;

@Service
public class MetaDataServiceImpl implements MetaDataService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MediaMetaDataRepository mediaMetaDataRep;
	
	@Autowired
	private MediaRepository mediaRep;

	@Autowired
	private HelperBean helper;

	@Autowired
    private IAuthenticationFacade authenticationFacade;

	
	@Override
	public void saveMetaData(MetaDataVo request) throws MetaDataServiceException {
		
		try {
			Date startT = new Date();
			Authentication authentication = authenticationFacade.getAuthentication();
			
			String userId = authentication.getName();
			
			if( null != userId)
			{
				request.setCreatedBy(userId);
				request.setCreatedOn(startT);
			}
			
			
			// Request Validation
			metaDataRequestValidation(request);
			
			// Persist MetaData
			 persistMetaData(request);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MetaDataServiceException(e.getMessage(), e);
		}
		
	}
	
	private void persistMetaData(MetaDataVo request) throws MetaDataServiceException
	{
		try {
			
			// prepare MediaMetadata Objects
			List<MediaMetaData> metadataList = prepareMetaDataSaveRequest(request);
			
			mediaMetaDataRep.saveAll(metadataList); 
			
		}
		catch(Exception e)
		{
			throw new MetaDataServiceException(e.getMessage(), e);
		}
		
	}
	
	private List<MediaMetaData> prepareMetaDataSaveRequest(MetaDataVo request) throws SequencerException
	{
		List<MediaMetaData> metadataList = new ArrayList<>();
		
		if(!request.getEntryList().isEmpty())
		{
			for (MetaDataEntryVo mdev : request.getEntryList())
			{
				MediaMetaData mmd = new MediaMetaData();
				mmd.setId(helper.getSequanceNo(SequenceType.METADATA));
				mmd.setMediaId(request.getMediaId());
				mmd.setMediaType(request.getMediaType());
				mmd.setRelationType(mdev.getRelationType());
				mmd.setRelationKey(mdev.getRelationKey());
				mmd.setRelationValue(mdev.getRelationValue());
				mmd.setDescription(mdev.getDescription());
				mmd.setCreatedBy(request.getCreatedBy());
				mmd.setCreatedOn(request.getCreatedOn());
				
				metadataList.add(mmd);
			}
		
		}
		
		return metadataList;
	}
	
	private void metaDataRequestValidation(MetaDataVo request) throws ValidationException
	{
		
		if(helper.isEmptyString(request.getMediaId()))
		{
			request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Media_ID.name(), ValidationErrorType.Invalid_Media_ID.getValue()));
			
		}
		
		if(request.getEntryList().isEmpty())
		{
			request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_MetaData_Key.name(), ValidationErrorType.Invalid_MetaData_Key.getValue()));
			
			
		}
		
		// Validate MetaDataVo Entry List
		validateMetaDataEntryList(request);
		
		if(!request.isValid())
		{
			throw new ValidationException("Invalid Request");
		}
		
		
		
	}
	
	private void validateMetaDataEntryList(MetaDataVo request)
	{
		int i = 1; 
		for (MetaDataEntryVo mdev : request.getEntryList())
		{
			mdev.setMediaId(request.getMediaId());
		
			if(null == mdev.getRelationType())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_MetaData_RelationShip.name(), 
						 "Record", String.valueOf(i)));
				
			}
			
			if(helper.isEmptyString(mdev.getRelationKey()))
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_MetaData_Key.name(), 
						 "Record", String.valueOf(i)));
				
			}
			if(helper.isEmptyString(mdev.getRelationValue()))
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_MetaData_Value.name(), 
						"Record", String.valueOf(i)));
				
			}
			
			i++;
			
			
		}
	}

	@Override
	public Page<Media> searchMediabyText(String txt, Pageable pageable) throws MetaDataServiceException {
		
		
		Page<Media> mediaPage = null;
		String[] searchArray = txt.split("[^a-zA-Z]+");
		
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(searchArray);
		Page<MediaMetaData> metaPage = mediaMetaDataRep.findAllBy(criteria, pageable);
		
		if(metaPage.hasContent())
		{
			List<String> mediaIds = metaPage.getContent().stream()
				    .map(MediaMetaData::getMediaId)
				    .collect(Collectors.toList());
			
			mediaPage = mediaRep.findAllByIdsIn(mediaIds, pageable);
		}
		
		
		return mediaPage;
	}
	
	

	@Override
	public List<MediaMetaData> getMediaMetaData(String mediaId) throws MetaDataServiceException {

		List<MediaMetaData> metaDataList = null;
		
		try {
			metaDataList = mediaMetaDataRep.findByMediaId(mediaId);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MetaDataServiceException(e.getMessage(), e);
		}
		
		return metaDataList;
	}
	

}
