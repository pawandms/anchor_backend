package com.anchor.app.msg.dao;

import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import com.anchor.app.msg.exceptions.MessageDaoException;
import com.anchor.app.msg.vo.MsgAggregateVo;

@Component
public class MsgBoxRelationDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MongoTemplate mongoTemplate;

	
	public List<MsgAggregateVo> getRecentMessageForUserForChannels(String userID, Collection channelIds) throws MessageDaoException
	{
		List<MsgAggregateVo> result = null;
		try {
			

			
		     MatchOperation match = new MatchOperation(Criteria.where("userID").is(userID).and("chnlID").in(channelIds));
		     
		     Sort createdDateSort = Sort.by(Direction.DESC, "createdOn");
		     
		     SortOperation sort = new SortOperation(createdDateSort);
		     
		     GroupOperation groupByChnlId = Aggregation.group("userID", "chnlID")
		    		 .first("$$ROOT").as("msgBoxRelation");
		    
		     
		     LookupOperation msgLookup = LookupOperation.newLookup().
		             from("Message").
		             localField("msgBoxRelation.msgID").
		             foreignField("_id").
		             as("msgList");
		     
		     LookupOperation parentMsgLookup = LookupOperation.newLookup().
		             from("Message").
		             localField("msgBoxRelation.parentMsgID").
		             foreignField("_id").
		             as("parentMsgList");
		     
		     
		     
	          AggregationOptions option = AggregationOptions.builder()
	        		  .allowDiskUse(true).cursorBatchSize(100).build();

	            Aggregation aggregate = Aggregation.newAggregation(match,sort, groupByChnlId, msgLookup, parentMsgLookup);
	            		     		
	            AggregationResults<MsgAggregateVo> msgAggResult = mongoTemplate.aggregate(aggregate,
	                    "MsgBoxRelation", MsgAggregateVo.class);
	           
	          result = msgAggResult.getMappedResults();
	            
	            logger.info("Aggregated Results :"+msgAggResult.getMappedResults().size());

		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MessageDaoException(e.getMessage(), e);
		}
		
		return result;
	}

	

}
