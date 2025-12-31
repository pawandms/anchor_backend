package com.anchor.app.importer.service;

import com.anchor.app.exception.ImporterException;
import com.anchor.app.importer.model.YouTubeImportRequest;

public interface YouTubeImporterService {

	public YouTubeImportRequest importYouTubeMedia(YouTubeImportRequest yimpReq) throws ImporterException;
	
	/**
	 * Process YouTubeImport Request to verify if video is already download 
	 * @return
	 * @throws ImporterException
	 */
	public int processYouTubeImportRequest()throws ImporterException;
	
	public int processImportedYouTubeMedia() throws ImporterException;
	
	
}
