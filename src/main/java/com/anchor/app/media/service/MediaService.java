package com.anchor.app.media.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.enums.MediaGenreType;
import com.anchor.app.enums.MediaOrderType;
import com.anchor.app.enums.MediaType;
import com.anchor.app.exception.MediaServiceException;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.media.model.MetaData;

public interface MediaService {
	
	/**
	 * Save Media to DB and Return Reference of It.
	 * @param media
	 * @return
	 * @throws MediaServiceException
	 */
	public Media saveMedia(Media media) throws MediaServiceException;

	/**
	 * get Page Result for Matching Query paramaters
	 * @param mediaType
	 * @param genreType
	 * @param mediaOrder
	 * @param pageable
	 * @return
	 */
	Page<Media> getPage(MediaType mediaType,MediaGenreType genreType,MediaOrderType mediaOrder, Pageable pageable);

	Slice<Media> getSlice(MediaType mediaType,MediaGenreType genreType,MediaOrderType mediaOrder, Pageable pageable);
	

	public Media getMedia(String mediaID)throws MediaServiceException;
	
	/**
	 * Get Media Stream for NON HLS Media
	 * @param mediaID
	 * @return
	 * @throws MediaServiceException
	 */
	public ResponseEntity<?> getMediaStream(String mediaID, String range) throws MediaServiceException;
	
	
	/**
	 * Sample Example to perform Aggregation with MongoTemplate
	 * @return
	 * @throws MediaServiceException
	 */
	public List<Media> getMediaAggregationByCreatedUser() throws MediaServiceException; 
	
	/**
	 * Save MetaData for Respective Media
	 * @param metaData
	 * @throws MediaServiceException
	 */
	public void saveMediaMetaData(MetaData metaData) throws MediaServiceException;
	
	/**
	 * Get Media for Matching Search String
	 * @param searchString
	 * @param pageable
	 * @return
	 */
	Page<Media> getMediaBySearchString(String searchString, Pageable pageable)throws MediaServiceException;



	/**
	 * Update Media Details 
	 * @param media
	 * @param modifiedUserName
	 * @return
	 * @throws MediaServiceException
	 */
	public Media updateMedia(Media media, String modifiedUserName)throws MediaServiceException;
	
	/**
	 * Add Image to Media
	 * @param entityType
	 * @param imageType
	 * @param entityId
	 * @param image
	 * @return
	 * @throws MediaServiceException
	 */
	public MediaImage addMediaImage(EntityType entityType, ImageType imageType,  String entityId,MultipartFile image) throws MediaServiceException;
	
	/**
	 * One Time Service to Modify ContentID of Existing Media on Prod Server
	 * @return
	 * @throws MediaServiceException
	 */
	public int updateMediaContentID()throws MediaServiceException;
	
	/**
	 * Update View Count of Respective Media
	 * @param mediaID
	 * @throws MediaServiceException
	 */
	public void updateMediaViewCount(String appUserID, String mediaID) throws MediaServiceException;
	
	/**
	 * Delete Media Along with Content if Any
	 * @param mediaID
	 * @throws MediaServiceException
	 */
	public void deleteMedia(String mediaID) throws MediaServiceException;
}
