package com.anchor.app.media.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anchor.app.exception.PeopleException;
import com.anchor.app.media.model.Person;
import com.anchor.app.media.service.PeopleService;
import com.anchor.app.repository.ImageRepository;
import com.anchor.app.repository.PeopleRepository;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@Service
public class PeopleServiceImpl implements PeopleService {
	
	
	
	@Autowired
	private PeopleRepository pepRep;

	@Autowired
	private ImageRepository imgRep;

	@Autowired
	private EnvProp env;

	
	@Autowired
	private HelperBean helper;
	
	@Override
	@Transactional
	public Person addPeople(Person person, boolean allowDuplicate) throws PeopleException {
		
		
		try {
		
			if(!allowDuplicate)
			{
				//Check Person is exists with Same Name
				Person pep = pepRep.findByName(person.getName());
				
				if(null != pep)
				{
					throw new PeopleException("Person with Name:"+pep.getName()+" Exists into system with ID:"+pep.getId());
				}
					
			}
			
			String imgUrl = env.getImageUrl();
			person = pepRep.save(person);
			
				
		}
		catch(PeopleException  e )
		{
			throw new PeopleException(e.getMessage(), e);
		}
		
		return person;
	}
	
	

	@Override
	public Page<Person> findByName(String name, Pageable pageable) throws PeopleException {
		Page<Person> pepPage = null;
		if( null == name)
		{
			pepPage =	pepRep.findAll(pageable);
		}
		else {
			pepPage =	pepRep.findByNameLikeOrOriginalnameLike(name, name, pageable);
		}
		
		return pepPage;
	}

	

}
