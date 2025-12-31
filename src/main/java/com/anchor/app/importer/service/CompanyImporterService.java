package com.anchor.app.importer.service;

import com.anchor.app.exception.ImporterException;

public interface CompanyImporterService {

	/**
	 * Import Person Data from MovieDB Extracted Person Json File
	 * @param localPath
	 * @throws ImporterException
	 */
	public int importCompanyData(String localPath) throws ImporterException;
	
	

}
