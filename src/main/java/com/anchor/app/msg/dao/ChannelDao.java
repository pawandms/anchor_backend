package com.anchor.app.msg.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anchor.app.msg.enums.ChannelActionType;
import com.anchor.app.msg.exceptions.ChannelDaoException;
import com.anchor.app.msg.exceptions.ChannelParticipantDaoException;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.repository.ChannelRepository;

@Component
@CacheConfig(cacheNames={"ChannelCache"})   
public class ChannelDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ChannelRepository channelRep;

	@Autowired
	private MongoOperations mongoOperations;


    @CachePut
	public void saveChannel(Channel chnl) throws ChannelDaoException
	{
		try {
			
			channelRep.save(chnl);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelDaoException(e.getMessage(), e);
		}
	}
	
	@Cacheable(key="#name")
	public Channel getChannelByName(String name) throws ChannelDaoException
	{
		Channel result = null;
		
		try {
			List<Channel> chnlList = channelRep.findByName(name);
			
			if(!chnlList.isEmpty())
			{
				result = chnlList.get(0);
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelDaoException(e.getMessage(), e);
		}
		
		return result;
		
	}
	

	@Cacheable(key="#id")
	public Channel getChannelByID(String id) throws ChannelDaoException
	{
		Channel result = null;
		
		try {
		
			Optional<Channel> chnl = channelRep.findById(id);
			if(chnl.isPresent())
			{
				result = chnl.get();
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelDaoException(e.getMessage(), e);
		}
		
		return result;
		
	}
	

	@Cacheable
	public List<Channel> getChannelForIds(Collection chnlIds) throws ChannelDaoException {
		List<Channel> channels = null;
		try {
			
			channels = channelRep.findAllByIdsIn(chnlIds);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelDaoException(e.getMessage(), e);
		}
		
		return channels;
	}

	
	@CacheEvict(key="#channelID")
	public void updateChannelLatestMsgIdAndDate(String channelID, String msgID, Date date) throws ChannelDaoException
	{
		try {
			
			Query query = new Query();
			query.addCriteria(new Criteria().where("_id").is(channelID));
			
			Update update = new Update();
			update
			.set("latestMsgId", msgID)
			.set("latestMsgDate", date);
				
			mongoOperations.updateMulti(query, update, Channel.class);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelDaoException(e.getMessage(), e);
		}

	}

	
}
