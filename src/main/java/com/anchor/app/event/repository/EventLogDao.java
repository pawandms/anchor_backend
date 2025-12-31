package com.anchor.app.event.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Term;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.anchor.app.event.model.EventLog;
import com.anchor.app.exception.UserServiceException;
import com.anchor.app.msg.enums.ChannelActionType;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.VisibilityType;
import com.anchor.app.msg.model.ChannelParticipant;

@Repository
public class EventLogDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MongoOperations mongoOperations;

	
	public void updateUserProfileType(String userID, VisibilityType profileType) throws UserServiceException
	{
		try {
			
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(
			Criteria.where("srcType").is(EventEntityType.User),
			Criteria.where("srcKey").is(userID),
			Criteria.where("trgType").is(EventEntityType.Meta_Data))
					
			);
			
			Update update = new Update();
						
						update.set("trgValue.visibility", profileType.name())
						.set("modDate", new Date());
			
			mongoOperations.updateMulti(query, update, EventLog.class);
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
	}
	
	public Page<EventLog> searchUsersAndMsgChnls(String reqUserID, String searchKey, Pageable pageable) throws UserServiceException
	{
		Page<EventLog> resultPage = null;
		try {
			
    		Term term = new Term(searchKey);
			TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(term).caseSensitive(false);
			
			
			Collection<String> srcTypes = new ArrayList<>();
			srcTypes.add(EventEntityType.User.name());
			srcTypes.add(EventEntityType.Msg_Chnl.name());
			
			Criteria userNameCrt = Criteria.where("srcType").in(srcTypes);

			Criteria visiblityCrt = Criteria.where("trgValue.visibility").ne(VisibilityType.Protected.name());
			
			Query query = new Query();
			query.addCriteria(textCriteria);
			query.addCriteria(userNameCrt);
			query.addCriteria(visiblityCrt);
			
			query.with(pageable);
			
			long count = mongoOperations.count(query, EventLog.class);
				List<EventLog> result = mongoOperations.find(query, EventLog.class);
				
				resultPage =  PageableExecutionUtils.getPage(result, pageable, ()-> count);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage(), e);	
		}
		
		return resultPage;
	}


}
