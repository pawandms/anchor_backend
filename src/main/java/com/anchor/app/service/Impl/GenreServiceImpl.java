package com.anchor.app.service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.exception.GenreException;
import com.anchor.app.model.Genre;
import com.anchor.app.repository.GenreRepository;
import com.anchor.app.service.GenreService;

@Service
public class GenreServiceImpl implements GenreService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private GenreRepository genreRepo;
	
	@Override
	public Genre addGenre(Genre genre) throws GenreException {
		Genre gen = genreRepo.save(genre);
		return gen;
	}

	@Override
	public List<Genre> getAllGenre() {
		return genreRepo.findAll();
	}

	@Override
	public Genre getGenrebyByName(String name) throws GenreException {
		// TODO Auto-generated method stub
		return genreRepo.findByName(name);
	}

}
