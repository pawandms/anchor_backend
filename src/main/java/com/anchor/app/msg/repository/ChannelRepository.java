package com.anchor.app.msg.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.media.model.Media;
import com.anchor.app.msg.model.Channel;

@Repository
public interface ChannelRepository extends MongoRepository<Channel, String> {

	public List<Channel> findByName(String name);
	
	@Query(value = "{ '_id' : {'$in' : ?0 } }")
	public List<Channel> findAllByIdsIn(Iterable<String> chnlIds);


	
	
}
