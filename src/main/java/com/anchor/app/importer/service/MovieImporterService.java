package com.anchor.app.importer.service;

import com.anchor.app.exception.ImporterException;

public interface MovieImporterService {

	/**
	 * Import Person Data from MovieDB Extracted Person Json File
	 * @param localPath
	 * @throws ImporterException
	 */
	public int importMovieData(String localPath) throws ImporterException;
	
	

}
