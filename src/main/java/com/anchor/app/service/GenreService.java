package com.anchor.app.service;

import java.util.List;

import com.anchor.app.exception.GenreException;
import com.anchor.app.model.Genre;

public interface GenreService {
	
	/**
	 * Add Genre to System
	 * @param genre
	 * @throws GenreException
	 */
	public Genre addGenre(Genre genre) throws GenreException;
	
	/**
	 * Get All Genre
	 * @return
	 */
	public List<Genre> getAllGenre();
	
	/**
	 * Get Genre By Name
	 * @param name
	 * @return
	 * @throws GenreException
	 */
	public Genre getGenrebyByName(String name) throws GenreException;
	

}
