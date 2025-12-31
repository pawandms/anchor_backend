package com.anchor.app.importer.service;

import com.anchor.app.exception.HlsProcessingException;
import com.anchor.app.importer.model.ImportMediaRequest;
import com.anchor.app.importer.model.YouTubeMedia;

public interface HlsConversionService {
	
	
	/**
	 * Create HLS Stream for Given youtube Media
	 * @param ymedia
	 * @return
	 * @throws HlsProcessingException
	 */
	public void createHlsStramForMedia(YouTubeMedia ymedia) throws HlsProcessingException;

	/**
	 * Upload HLS PlayList to MongoDB
	 * Assumption : All HLS Segment and child PlayList in Same Folder where MasterPlayList is Present
	 * @param playListPath
	 * @throws HlsProcessingException
	 */
	public void uploadHlsStream(String playListPath) throws HlsProcessingException;

	
	/**
	 * Create HLS Stream for Given User Imported Media
	 * @param ymedia
	 * @return
	 * @throws HlsProcessingException
	 */
	public void createHlsStramForImportMedia(ImportMediaRequest imr) throws HlsProcessingException;

	

}
