package com.anchor.app.action.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anchor.app.action.model.ActionLog;
import com.anchor.app.action.vo.ActionLogReq;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.event.exceptions.EventLogServiceException;
import com.anchor.app.event.model.EventLog;
import com.anchor.app.event.model.EventLogReq;
import com.anchor.app.event.model.Meta;
import com.anchor.app.msg.enums.*;
import com.anchor.app.util.HelperBean;

@Component
public class ActionLogUtil {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HelperBean helper;

    public ActionLogReq createUserCreateEventLogs(String reqUserID, String userID, String firstName, String lastName, VisibilityType visibility) throws EventLogServiceException
    {
        ActionLogReq result = null;
        try {

            ActionLog crUserLog = new ActionLog();
            String eventLogId = helper.getSequanceNo(SequenceType.ActionLog);
            crUserLog.setId(eventLogId);

            crUserLog.setActionType(UserActionType.Create_User);
            crUserLog.setSrcType(EventEntityType.User);
            crUserLog.setSrcKey(userID);
            crUserLog.setTrgType(EventEntityType.User);
            crUserLog.setTrgUserID(userID);
            crUserLog.setUserNotification(false);

            Meta meta = new Meta();
            meta.setUserId(userID);
            meta.setName(firstName);
            meta.setLastName(lastName);
            meta.setVisibility(visibility);
            crUserLog.setAttributes(meta);

            result = new ActionLogReq();
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


    public ActionLogReq createAddUserConnectionEventLogs(String reqUserID, String firstUserID, String secondUserID, UserActionStatusType actStatus) throws EventLogServiceException
    {
        ActionLogReq result = null;
        try {

            result = new ActionLogReq();
            result.setActionType(UserActionType.Add_User_Connection);
            result.setReqUserID(reqUserID);

            if((actStatus.equals(UserActionStatusType.Auto_Approve))
                    ||(actStatus.equals(UserActionStatusType.Approve))
                    )
            {
                ActionLog fLog = createAddConnectionEventLog(firstUserID, secondUserID, actStatus);
                ActionLog sLog = createAddConnectionEventLog(secondUserID, firstUserID, actStatus);

                result.getLogs().add(fLog);
                result.getLogs().add(sLog);


            }
            else  if((actStatus.equals(UserActionStatusType.Block)))
            {
                ActionLog fLog = createAddConnectionEventLog(firstUserID, secondUserID, actStatus);
                result.getLogs().add(fLog);

            }

        }
        catch(Exception e)
        {
            throw new EventLogServiceException(e.getMessage(), e);
        }

        return result;
    }

    private ActionLog createAddConnectionEventLog(String fromUserID, String toUserID, UserActionStatusType actStatus) throws EventLogServiceException
    {
        ActionLog result = null;
        try {
                result = new ActionLog();
                String eventLogId = helper.getSequanceNo(SequenceType.ActionLog);
                result.setId(eventLogId);
                result.setActionType(UserActionType.Add_User_Connection);
                result.setSrcType(EventEntityType.User);
                result.setSrcKey(fromUserID);
                result.setTrgType(EventEntityType.User_Connection);
                result.setTrgUserID(toUserID);

                Meta meta = new Meta();
                meta.setUserId(toUserID);
                meta.setActionStatus(actStatus);
                result.setAttributes(meta);
                result.setUserNotification(true);
                result.setRead(false);

        }
        catch(Exception e)
        {
            throw new EventLogServiceException(e.getMessage(), e);
        }

        return result;
    }


    public ActionLogReq createAddUserConnectionReqLogs(String reqUserID, String firstUserID, String secondUserID) throws EventLogServiceException
    {
        ActionLogReq result = null;
        try {

            result = new ActionLogReq();
            result.setActionType(UserActionType.Add_Friend_Request);
            result.setReqUserID(reqUserID);

                ActionLog fLog = createAddConnectionReqLog(firstUserID, secondUserID, UserActionType.Add_Friend_Request, UserActionStatusType.Pending);

                // Add Source Action ID as a Reference
                result.getLogs().add(fLog);


        }
        catch(Exception e)
        {
            throw new EventLogServiceException(e.getMessage(), e);
        }

        return result;
    }

    private ActionLog createAddConnectionReqLog(String fromUserID, String toUserID, UserActionType actionType, UserActionStatusType actStatus) throws EventLogServiceException
    {
        ActionLog result = null;
        try {
            result = new ActionLog();
            String eventLogId = helper.getSequanceNo(SequenceType.ActionLog);
            result.setId(eventLogId);
            result.setActionType(actionType);
            result.setSrcType(EventEntityType.User);
            result.setSrcKey(fromUserID);
            result.setTrgType(EventEntityType.User);
            result.setTrgUserID(toUserID);

            Meta meta = new Meta();
            meta.setUserId(toUserID);
            meta.setActionStatus(actStatus);
            result.setAttributes(meta);
            result.setUserNotification(true);
            result.setRead(false);

        }
        catch(Exception e)
        {
            throw new EventLogServiceException(e.getMessage(), e);
        }

        return result;
    }

    public ActionLogReq createAddMsgReactionReqLog(String reqUserID, String userID, String chnlId, String msgID, String reactionStr) throws EventLogServiceException
    {
        ActionLogReq result = null;
        try {
            UserActionType actionType = UserActionType.Add_Msg_Reaction;
            ActionLog fLog = createAddMsgReactionEventLog(reqUserID, userID,  chnlId, msgID, reactionStr, actionType);
            result = new ActionLogReq();
            result.setActionType(actionType);
            result.setReqUserID(reqUserID);
            result.getLogs().add(fLog);

        }
        catch(Exception e)
        {
            throw new EventLogServiceException(e.getMessage(), e);
        }

        return result;
    }

    private ActionLog createAddMsgReactionEventLog(String reqUserID,String userID, String chnlId, String msgID, String reactionStr, UserActionType actionType) throws EventLogServiceException
    {
        ActionLog result = null;
        try {
            result = new ActionLog();
            String eventLogId = helper.getSequanceNo(SequenceType.ActionLog);
            result.setId(eventLogId);
            result.setActionType(actionType);
            result.setSrcType(EventEntityType.User);
            result.setSrcKey(reqUserID);
            result.setTrgType(EventEntityType.Message);
            result.setTrgUserID(userID);
            result.setTrgChnlID(chnlId);
            result.setTrgMsgID(msgID);

            Meta meta = new Meta();
            meta.setChnlId(chnlId);
            meta.setMsgID(msgID);
            meta.setValue(reactionStr);

            result.setAttributes(meta);

            result.setUserNotification(true);
            result.setRead(false);


        }
        catch(Exception e)
        {
            throw new EventLogServiceException(e.getMessage(), e);
        }

        return result;
    }

}
