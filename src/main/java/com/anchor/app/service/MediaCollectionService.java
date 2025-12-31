package com.anchor.app.service;

import com.anchor.app.exception.CollectionException;
import com.anchor.app.model.MovieCollection;

public interface MediaCollectionService {
	
	/**
	 * Add Collection to DB
	 * @param col
	 * @return
	 * @throws CollectionException
	 */
	public MovieCollection addCollection(MovieCollection col) throws CollectionException; 

}
