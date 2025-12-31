package com.anchor.app.msg.dao;

import java.util.Date;
import java.util.function.LongSupplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import com.anchor.app.media.model.Media;
import com.anchor.app.msg.exceptions.ChannelParticipantDaoException;
import com.anchor.app.msg.exceptions.MessageDaoException;
import com.anchor.app.msg.model.ChannelMsgRelation;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.vo.MsgAggregateVo;
import com.anchor.app.msg.vo.UserChnlMsgVo;
import com.mongodb.client.result.UpdateResult;

@Component
public class MessageDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoTemplate mongoTemplate;

	
	public Page<Message> getUserMessagesForChannel(UserChnlMsgVo req, Pageable pageable)throws MessageDaoException
	{
		Page<Message> result = null;
		try {
			
			//Criteria chnlCrt = Criteria.where("mediaType").is(mediaType); 
			Query query = new Query();
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MessageDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	

	@Deprecated
	public Page<MsgAggregateVo> getMessagesForUserForChannel(String chnlID, String userID, Pageable pageable) throws MessageDaoException
	{
		Page<MsgAggregateVo> result = null;
		try {
			
		//	Criteria chnlCrt = Criteria.where("chnlID").is(chnlID);
		//	Criteria usereCrt = Criteria.where("userID").is(userID);

			
		     MatchOperation match = new MatchOperation(Criteria.where("chnlID").is(chnlID).and("userID").is(userID));
		     
		     LookupOperation msgLookup = LookupOperation.newLookup().
		             from("Message").
		             localField("msgID").
		             foreignField("_id").
		             as("msgList");
		     
		     LookupOperation parentMsgLookup = LookupOperation.newLookup().
		             from("Message").
		             localField("parentMsgID").
		             foreignField("_id").
		             as("parentMsgList");
		     
		     
		     
	          AggregationOptions option = AggregationOptions.builder()
	        		  .allowDiskUse(true).cursorBatchSize(100).build();

	            Aggregation aggregate = Aggregation.newAggregation(match,msgLookup, parentMsgLookup,
	            		Aggregation.skip(pageable.getPageNumber() * pageable.getPageSize()),
                        Aggregation.limit(pageable.getPageSize()));
	            		     		
	            		     		
	            		
	            		
	           //         .withOptions(option)
	            
	            /*AggregationResults<UserReading> orderAggregate = mongoTemplate.aggregate(aggregate,
	                    "userReading", UserReading.class);
	*/
	            AggregationResults<MsgAggregateVo> msgAggResult = mongoTemplate.aggregate(aggregate,
	                    "MsgBoxRelation", MsgAggregateVo.class);
	           
	            LongSupplier
	            total
	            = () -> (int)(3);
	            
	        	result  = PageableExecutionUtils.getPage(
	        			msgAggResult.getMappedResults(),
	    		        pageable,total);
	            
	            logger.info("Aggregated Results :"+msgAggResult.getMappedResults().size());

		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MessageDaoException(e.getMessage(), e);
		}
		
		return result;
	}
	
	public int addUpdateMsgReactionByUser(String msgID, String chnlID, String userID, Date tr_date, String msgReaction) throws MessageDaoException
	{
		int resultCnt = 0;
		try {
		
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(
			Criteria.where("channelID").is(chnlID),
			Criteria.where("msgId").is(msgID)));
			
			Update update = new Update();
				
						update.set("attribute.userReaction."+userID, msgReaction)
				.set("modifiedBy", userID)
				.set("modifiedOn", tr_date);
		
				;
				UpdateResult result = 	mongoTemplate.updateFirst(query, update, ChannelMsgRelation.class);
				resultCnt = (int) result.getModifiedCount();
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MessageDaoException(e.getMessage(), e);
		}
		
		return resultCnt;
	}
	
}
