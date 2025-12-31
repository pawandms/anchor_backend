package com.anchor.app.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.exception.ImageException;
import com.anchor.app.exception.ImageServiceException;
import com.anchor.app.exception.PeopleException;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.model.Image;

public interface ImageService {
	
	/**
	 * Add Image to DB
	 * @param entityType
	 * @param imageType
	 * @param image
	 * @return
	 * @throws ImageException
	 */
	public MediaImage addImage(EntityType entityType,  ImageType imageType, String entityId, MultipartFile image) throws ImageServiceException;

	public MediaImage addImage(EntityType entityType,  ImageType imageType,  String entityId, String name, InputStream  stream) throws ImageServiceException;

	public MediaImage addImage(EntityType entityType,  ImageType imageType,  String entityId, String name, int widht, int height, InputStream  stream) throws ImageServiceException;

	
	/**
	 * Remove Image From Database
	 * @param imageId
	 * @throws ImageException
	 */
	public void removeImage(String imageId) throws ImageServiceException;

	
	/**
	 * Get Image by ID from Database
	 * @param imageID
	 * @return
	 * @throws ImageException
	 */
	public InputStream getImage(String imageID)throws ImageServiceException;
}
