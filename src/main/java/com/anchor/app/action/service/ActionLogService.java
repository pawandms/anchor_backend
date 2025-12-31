package com.anchor.app.action.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.action.exceptions.ActionLogServiceException;
import com.anchor.app.action.rep.ActionLogRep;
import com.anchor.app.action.vo.ActionLogReq;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.msg.enums.UserActionType;
import com.anchor.app.util.HelperBean;

@Service
public class ActionLogService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ActionLogRep actionLogRep;

  @Autowired
  private HelperBean helper;

  public void processActionLogRequest(ActionLogReq req) throws ActionLogServiceException {
      try{
            if(null == req.getActionType())
            {
                throw new ActionLogServiceException("Invalid UserAction Type");
            }

            if(req.getActionType().equals(UserActionType.Create_User))
            {
                // Save Logs to DB
                saveActionLogToDb(req);

            }
            else if(req.getActionType().equals(UserActionType.Add_User_Connection))
            {
                // Save Logs to DB
                saveActionLogToDb(req);

                //  Step 2 : Send Push Notification to Respective User
            }
            else if(req.getActionType().equals(UserActionType.Add_Friend_Request))
            {
                // Save Logs to DB
                saveActionLogToDb(req);

                //  Step 2 : Send Push Notification to Respective User
            }
            else if(req.getActionType().equals(UserActionType.Add_Msg_Reaction))
            {
                // Save Logs to DB
                saveActionLogToDb(req);

                //  Step 2 : Send Push Notification to Respective User
            }


      }
      catch (Exception e)
      {
        logger.error(e.getMessage(), e);
        throw new ActionLogServiceException(e.getMessage(), e);
      }
  }

  private void saveActionLogToDb(ActionLogReq req) throws ActionLogServiceException {
      try{
            actionLogRep.saveAll(req.getLogs());
      }
      catch (Exception e)
      {
          logger.error(e.getMessage(), e);
          throw new ActionLogServiceException(e.getMessage(), e);
      }
  }

}
