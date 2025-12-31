package com.anchor.app.importer.service;

import com.anchor.app.exception.ImporterException;
import com.anchor.app.media.model.Person;

public interface PersonImporterService {
	
	/**
	 * Import Person Data from MovieDB Extracted Person Json File
	 * @param localPath
	 * @throws ImporterException
	 */
	public int importPersonData() throws ImporterException;
	
	/**
	 * Import Genre Data from MovieDB
	 * @return
	 * @throws ImporterException
	 */
	public int importGenreData()throws ImporterException;
	
	/**
	 * Import Person from TMDB for Given ID
	 * @param id
	 * @return
	 * @throws ImporterException
	 */
	public Person importPerson(int id)throws ImporterException;

}
