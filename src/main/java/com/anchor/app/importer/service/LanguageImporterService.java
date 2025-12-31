package com.anchor.app.importer.service;

import com.anchor.app.exception.ImporterException;

public interface LanguageImporterService {
	
	/**
	 * Import Language Data from Temp MongoDB Table import_language
	 * @return
	 * @throws ImporterException
	 */
	public int importLanguageData() throws ImporterException;

}
