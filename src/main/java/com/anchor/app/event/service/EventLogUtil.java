package com.anchor.app.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.anchor.app.event.exceptions.EventLogServiceException;
import com.anchor.app.event.model.EventLog;
import com.anchor.app.event.model.EventLogReq;
import com.anchor.app.event.model.Meta;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.EventLogSubType;
import com.anchor.app.msg.enums.EventLogType;
import com.anchor.app.msg.enums.UserActionType;
import com.anchor.app.msg.enums.VisibilityType;

@Service
public class EventLogUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public EventLogReq createAddUserConnectionEventLogs(String reqUserID, String firstUserID, String secondUserID) throws EventLogServiceException
	{
		EventLogReq result = null;
		try {
			
			EventLog fLog = createAddConnectionEventLog(firstUserID, secondUserID);
			EventLog sLog = createAddConnectionEventLog(secondUserID, firstUserID);
			
			result = new EventLogReq();
			
			result.setActionType(UserActionType.Add_Friend_Request);
			result.setReqUserID(reqUserID);
			result.getLogs().add(sLog);
			result.getLogs().add(fLog);
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	private EventLog createAddConnectionEventLog(String fromUserID, String toUserID) throws EventLogServiceException
	{
		EventLog result = null;
		try {
			result = new EventLog();
			result.setSrcType(EventEntityType.User);
			result.setSrcKey(fromUserID);
			result.setTrgType(EventEntityType.User);
			
			Meta meta = new Meta();
			meta.setUserId(toUserID);
			
			result.setTrgValue(meta);
			result.setLogType(EventLogType.UC);
			result.setLogSubType(EventLogSubType.UC_ADD);
			
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	
	public EventLogReq createRemoveUserConnectionEventLogs(String reqUserID, String firstUserID, String secondUserID) throws EventLogServiceException
	{
		EventLogReq result = null;
		try {
			
			EventLog fLog = createRemoveConnectionEventLog(firstUserID, secondUserID);
			result = new EventLogReq();
			result.setActionType(UserActionType.Remove_Friend_Request);
			result.setReqUserID(reqUserID);
			result.getLogs().add(fLog);
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	private EventLog createRemoveConnectionEventLog(String fromUserID, String toUserID) throws EventLogServiceException
	{
		EventLog result = null;
		try {
			result = new EventLog();
			result.setSrcType(EventEntityType.User);
			result.setSrcKey(fromUserID);
			result.setTrgType(EventEntityType.User);
			
			Meta meta = new Meta();
			meta.setUserId(toUserID);
			
			result.setTrgValue(meta);
			result.setLogType(EventLogType.UC);
			result.setLogSubType(EventLogSubType.UC_Remove);
			
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	
	public EventLogReq createBlockUserConnectionEventLogs(String reqUserID, String firstUserID, String secondUserID) throws EventLogServiceException
	{
		EventLogReq result = null;
		try {
			
			EventLog fLog = createBlockConnectionEventLog(firstUserID, secondUserID);
			
			result = new EventLogReq();
			
			result.setActionType(UserActionType.Block_User_Request);
			result.setReqUserID(reqUserID);
			result.getLogs().add(fLog);
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	private EventLog createBlockConnectionEventLog(String fromUserID, String toUserID) throws EventLogServiceException
	{
		EventLog result = null;
		try {
			result = new EventLog();
			result.setSrcType(EventEntityType.User);
			result.setSrcKey(fromUserID);
			result.setTrgType(EventEntityType.User);
			
			Meta meta = new Meta();
			meta.setUserId(toUserID);
			
			result.setTrgValue(meta);
			result.setLogType(EventLogType.UC);
			result.setLogSubType(EventLogSubType.UC_Block);
			
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	
	public EventLogReq createUserCreateEventLogs(String reqUserID, String userID, String firstName, String lastName, VisibilityType visibility) throws EventLogServiceException
	{
		EventLogReq result = null;
		try {
			
			EventLog crUserLog = new EventLog();
			crUserLog.setSrcType(EventEntityType.User);
			crUserLog.setSrcKey(userID);
			crUserLog.setTrgType(EventEntityType.Meta_Data);
			crUserLog.setLogType(EventLogType.Meta);
			crUserLog.setLogSubType(EventLogSubType.Meta_Data);
	
			Meta meta = new Meta();
			meta.setUserId(userID);
			meta.setName(firstName);
			meta.setLastName(lastName);
			meta.setVisibility(visibility);
			crUserLog.setTrgValue(meta);
			
			result = new EventLogReq();
			result.setActionType(UserActionType.Create_User);
			result.setReqUserID(reqUserID);
			result.getLogs().add(crUserLog);
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	
	
	public EventLogReq createChangeUserProfileEventLogs(String reqUserID, String userID, VisibilityType profileType) throws EventLogServiceException
	{
		EventLogReq result = null;
		try {
			
			EventLog fLog = createChangeUserProfileEventLog(userID, profileType);
			result = new EventLogReq();
			result.setActionType(UserActionType.Change_ProfileType);
			result.setReqUserID(reqUserID);
			result.getLogs().add(fLog);
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	private EventLog createChangeUserProfileEventLog(String userID, VisibilityType profileType) throws EventLogServiceException
	{
		EventLog result = null;
		try {
			result = new EventLog();
			result.setSrcType(EventEntityType.User);
			result.setSrcKey(userID);
			result.setTrgType(EventEntityType.Meta_Data);
			
			Meta meta = new Meta();
			meta.setUserId(userID);
			meta.setVisibility(profileType);
			
			result.setTrgValue(meta);
			result.setLogType(EventLogType.Meta);
			result.setLogSubType(EventLogSubType.Meta_Data);
			
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	public EventLogReq createMsgAddReactionEventLogs(String reqUserID, String userID, String chnlId, String msgID, String reactionStr) throws EventLogServiceException
	{
		EventLogReq result = null;
		try {
			
			EventLog fLog = createMsgAddReactionEventLog(reqUserID, chnlId, msgID, reactionStr);
			result = new EventLogReq();
			result.setActionType(UserActionType.Add_Msg_Reaction);
			result.setReqUserID(reqUserID);
			result.getLogs().add(fLog);
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	private EventLog createMsgAddReactionEventLog(String reqUserID,String chnlId, String msgID, String reactionStr) throws EventLogServiceException
	{
		EventLog result = null;
		try {
			result = new EventLog();
			result.setSrcType(EventEntityType.User);
			result.setSrcKey(reqUserID);
			result.setTrgType(EventEntityType.Message);
			
			Meta meta = new Meta();
			meta.setChnlId(chnlId);
			meta.setMsgID(msgID);
			meta.setValue(reactionStr);
			
			result.setTrgValue(meta);
			result.setLogType(EventLogType.Meta);
			result.setLogSubType(EventLogSubType.MSG_ADD_REC);
			
			
		}
		catch(Exception e)
		{
			throw new EventLogServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	
	public String getNatsSubject(UserActionType action, EventLog elog)
	{
		String result = null;
		
		StringBuilder sb = new StringBuilder();
		
		switch (action) {
		
		case Add_Msg_Reaction:
				sb.append("elog");
				sb.append(".");
				sb.append("chnl");
				sb.append(".");
				sb.append(elog.getTrgValue().getChnlId());
				sb.append(".");
				sb.append("msg");
				sb.append(".");
				sb.append(elog.getTrgValue().getMsgID());
				result = sb.toString();
				break;
				
	
				
		}
		
		return result;
	}
	

}
