package com.anchor.app.msg.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.enums.UserActionType;
import com.anchor.app.msg.model.EventNotification;
import com.anchor.app.msg.model.UserConnection;
import com.anchor.app.msg.model.UserMsgView;

import java.util.List;



@Repository
public interface EventNotificationRep extends MongoRepository<EventNotification, String> {
	
	List<EventNotification> findBySrcUserIDAndTrgUserIDAndSrcActionAndProcessFlag(String srcUserID, String trgUserID, UserActionType srcAction, boolean processFlag);

	@Query(value="{ $or: [{'srcUserID' :?0}, {'trgUserID': ?0 }]}")
	public Page<EventNotification> getUserNotification (String userID, Pageable pageable);

	
}
