package com.anchor.app.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.enums.SequenceType;
import com.anchor.app.exception.CollectionException;
import com.anchor.app.exception.SequencerException;
import com.anchor.app.model.MovieCollection;
import com.anchor.app.repository.MovieCollectionRepository;
import com.anchor.app.service.MediaCollectionService;
import com.anchor.app.util.HelperBean;

@Service
public class MediaCollectionServiceImpl implements MediaCollectionService {

	@Autowired
	private MovieCollectionRepository collectionRep;
	
	@Autowired
	private HelperBean helper;
	
	@Override
	public MovieCollection addCollection(MovieCollection col) throws CollectionException {
		MovieCollection result = null;
		
		try {
			col.setId(helper.getSequanceNo(SequenceType.COLLECTION));
			collectionRep.save(col);	
		}
		catch(SequencerException e)
		{
			throw new CollectionException(e.getMessage(), e);
		}
		
		return result;
	}
	
	

}
