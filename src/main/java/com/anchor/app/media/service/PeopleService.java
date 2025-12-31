package com.anchor.app.media.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anchor.app.exception.PeopleException;
import com.anchor.app.media.model.Person;

public interface PeopleService {

	
	/**
	 * Add People to System where People Json is in the form of Txt
	 * @param people
	 */
	public Person addPeople(Person person, boolean allowDuplicate) throws PeopleException;
	
	/**
	 * Find People with Pagination
	 * @param name
	 * @param pageable
	 * @return
	 * @throws PeopleException
	 */
	Page<Person> findByName(String name, Pageable pageable) throws PeopleException;
	
	
	
}
