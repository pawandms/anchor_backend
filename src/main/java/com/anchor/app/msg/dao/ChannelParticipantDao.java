package com.anchor.app.msg.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anchor.app.msg.enums.ChannelActionType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.exceptions.ChannelParticipantDaoException;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.repository.ChannelParticipantRepository;
import com.anchor.app.util.HelperBean;
import com.mongodb.client.result.UpdateResult;

@Component
@CacheConfig(cacheNames={"ChannelParticipantCache"})   
public class ChannelParticipantDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private ChannelParticipantRepository cpRep;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private HelperBean helper;

	
	public void saveAll(List<ChannelParticipant> channelParticipants)throws ChannelParticipantDaoException
	{
		try {
			cpRep.saveAll(channelParticipants);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
	}
	
	public void save(ChannelParticipant channelParticipant)throws ChannelParticipantDaoException
	{
		try {
			cpRep.save(channelParticipant);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
	}
	
	
	
	public List<ChannelParticipant> getChannelParticipentByChannel(String chnlID) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.findByChannelID(chnlID);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	
	public List<ChannelParticipant> getChannelParticipentByChannelID(String chnlID, Date today) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.getByChannelIDAndValidToday(chnlID, true, today);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	
	public List<ChannelParticipant> getActiveChannelParticipentForUser(String userID, boolean activeFlag,   Date todaysDate) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.findByUserIDActiveAndValidToday(userID, activeFlag, todaysDate);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	
	
	public List<ChannelParticipant> getActiveChannelParticipentForUser(String userID, boolean activeFlag,Date todaysDate, ChannelType channelType) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.findByUserIDActiveAndValidToday(userID, activeFlag, todaysDate, channelType);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	
	
	public List<ChannelParticipant> getActiveChannelParticipentForUser(String userID) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.findByUserID(userID);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	
	
	public List<ChannelParticipant> getActiveChannelParticipentForUser(String userID, ChannelType channelType) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.findByUserIDAndChannelType(userID, channelType);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	
	public List<ChannelParticipant> getActiveChannelParticipentForUser(String channelID, Collection userID, Date todaysDate) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.findByUserIDsAndChannelID(channelID, userID, todaysDate);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}

	public List<ChannelParticipant> getActiveChannelParticipentForUser(String channelID, String userID, Date todaysDate) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.findByUserIDAndChannelID(channelID, userID, todaysDate);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	

	
	public void closeUserAccessonChannel(String reqUserID, String userID, String channelID, Date validToDate )throws ChannelParticipantDaoException
	{
		try {
			Date startT = new Date();
		
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(
			Criteria.where("channelID").is(channelID),
			Criteria.where("userID").is(userID),
			Criteria.where("active").is(true))
			);
			
			Update update = new Update().set("active", false)
					.set("validTo", validToDate)
					.set("modifiedBy", reqUserID)
					.set("modifiedOn", startT);
			
			
			mongoOperations.updateMulti(query, update, ChannelParticipant.class);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
	}
	

	public void removeUserAccessonChannel(String userID, String channelID)throws ChannelParticipantDaoException
	{
		try {
		
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(
					Criteria.where("channelID").is(channelID),
					Criteria.where("userID").is(userID),
					Criteria.where("active").is(true))
					);
					
			mongoOperations.remove(query, ChannelParticipant.class);
			
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
	}
	
	public List<ChannelParticipant> getActiveChannelParticipentForChannel(String channelID, boolean activeFlag,Date todaysDate) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			result = cpRep.getByChannelIDAndValidToday(channelID, activeFlag, todaysDate);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}

	
	public void updateChnlParticipentReadStatus(String reqUserID, Collection userIDs, String channelID, ChannelActionType action, int count, Date validToDate )throws ChannelParticipantDaoException
	{
		try {
		
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(
			Criteria.where("channelID").is(channelID),
			Criteria.where("userID").in(userIDs),
			Criteria.where("active").is(true))
			);
			
			Update update = new Update();
			if(action.equals(ChannelActionType.AddUnread))
			{
				
						update.inc("unReadCount", count)
						.set("modifiedBy", reqUserID)
						.set("modifiedOn", validToDate);
				
			}
			else if (action.equals(ChannelActionType.SetUnread))
			{
				update.set("unReadCount", count)
				.set("modifiedBy", reqUserID)
				.set("modifiedOn", validToDate);
		
			}
			
			mongoOperations.updateMulti(query, update, ChannelParticipant.class);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
	}
	public void updateChnlParticipentFirstUnreadMsgIDAndDate(String reqUserID, Collection userIDs, String channelID,
			String msgID, Date msgDate )throws ChannelParticipantDaoException
	{
		try {
		
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(
			Criteria.where("channelID").is(channelID),
			Criteria.where("userID").in(userIDs),
			Criteria.where("active").is(true),
			Criteria.where("unReadDate").is(null))
			);
			
			Update update = new Update();
						update.set("unReadMsgID", msgID)
				.set("unReadDate", msgDate);
				UpdateResult result = 	mongoOperations.updateMulti(query, update, ChannelParticipant.class);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
	}
	
	public List<ChannelParticipant> getUserEnrolmentDetailsForChannel(String chnlID, String userID) throws ChannelParticipantDaoException
	{
		List<ChannelParticipant> result = null;
		try {
			
			result = cpRep.findByChannelIDAndUserID(chnlID, userID);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	
	public int removeUnreadStatuOfChnlParticipent(String reqUserID, String userID, String channelID, Date tr_Date)throws ChannelParticipantDaoException
	{
		int resultCnt = 0;
		try {
		
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(
			Criteria.where("channelID").is(channelID),
			Criteria.where("userID").is(userID)));
			
			Update update = new Update();
				
						update.set("unReadMsgID", null)
				.set("unReadDate", null)
				.set("unReadCount", 0)
				.set("modifiedBy", reqUserID)
				.set("modifiedOn", tr_Date);
		
				;
				UpdateResult result = 	mongoOperations.updateMulti(query, update, ChannelParticipant.class);
				resultCnt = (int) result.getModifiedCount();
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelParticipantDaoException(e.getMessage(), e);
		}
		
		return resultCnt;
	}

}
