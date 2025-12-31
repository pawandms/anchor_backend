package com.anchor.app.importer.service;

import com.anchor.app.exception.ImporterException;
import com.anchor.app.importer.model.MovieTranslationImport;

public interface TranslationImporterService {
	
	
	public MovieTranslationImport getMovieTranslationData(String id) throws ImporterException;

}
