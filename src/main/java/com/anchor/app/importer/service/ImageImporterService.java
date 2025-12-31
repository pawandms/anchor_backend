package com.anchor.app.importer.service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.exception.ImageException;
import com.anchor.app.importer.model.YouTubeMediaImage;
import com.anchor.app.media.model.MediaImage;

public interface ImageImporterService {

	/**
	 * get Image from TMDB 
	 * @param id
	 * @return
	 */
	public InputStream getImageStreamFromMovieDB(String id); 
	
	/**
	 * Create InputStream of given Valid Image URL
	 * @param url
	 * @return
	 */
	public InputStream getImageStreamFromUrl(String url);
	
	
	public MediaImage getMediaImageFromUrl(EntityType entityType, ImageType imageType, String entityId, String name,String url);
	
	/**
	 * Create MediaImage from Valid Image Path
	 * @param entityType
	 * @param imageType
	 * @param name
	 * @param imgPath
	 * @return
	 */
	public MediaImage getMediaImageFromUrl(EntityType entityType, ImageType imageType, String entityId, String name,Path imgPath);
	
	
	/**
	 * Create MediaImage from YouTube Thumbnail List
	 * @param entityType
	 * @param imageType
	 * @param entityId
	 * @param thumbnails
	 * @return
	 */
	public List<MediaImage> getMediaImageFromList(EntityType entityType, ImageType imageType, String entityId,List<YouTubeMediaImage> thumbnails);
	
}
