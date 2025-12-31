package com.anchor.app.msg.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anchor.app.action.service.ActionLogService;
import com.anchor.app.action.util.ActionLogUtil;
import com.anchor.app.action.vo.ActionLogReq;
import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.event.model.EventLog;
import com.anchor.app.event.service.EventLogService;
import com.anchor.app.event.service.EventLogUtil;
import com.anchor.app.exception.ConversionException;
import com.anchor.app.exception.UserServiceException;
import com.anchor.app.media.model.Media;
import com.anchor.app.msg.enums.EventActionType;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.MsgActionType;
import com.anchor.app.msg.enums.MsgType;
import com.anchor.app.msg.enums.PermissionType;
import com.anchor.app.msg.enums.UserActionStatusType;
import com.anchor.app.msg.enums.UserActionType;
import com.anchor.app.msg.enums.UserConnectionStatusType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.enums.VisibilityType;
import com.anchor.app.msg.exceptions.MsgServiceException;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.model.EventNotification;
import com.anchor.app.msg.model.UserConnection;
import com.anchor.app.msg.repository.ChannelParticipantRepository;
import com.anchor.app.msg.repository.ChannelRepository;
import com.anchor.app.msg.repository.EventNotificationRep;
import com.anchor.app.msg.repository.UserConnectionRep;
import com.anchor.app.msg.vo.MessageActionVo;
import com.anchor.app.msg.vo.SearchResp;
import com.anchor.app.msg.vo.SearchUserVo;
import com.anchor.app.msg.vo.SearchVo;
import com.anchor.app.msg.vo.UserActionVo;
import com.anchor.app.msg.vo.UserNotificationResp;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.oauth.service.UserService;
import com.anchor.app.oauth.vo.AuthReq;
import com.anchor.app.util.HelperBean;
import com.anchor.app.vo.ErrorMsg;


@Service
public class AppUserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AuthService authServce;

	@Autowired
	private UserService userService;
	
	@Autowired
	private HelperBean helperBean;

	@Autowired
	private UserConnectionRep userConnectionRep;
	
	@Autowired
	private EventNotificationRep eventNotificationRep;
	
	/*
	@Autowired
	private ChannelRepository chnlRepository;
	
	@Autowired
	private ChannelParticipantRepository cpRepository;
	*/
	
	@Autowired
	private ChannelService chnlService;
	
	@Autowired
	private EventLogService eventLogService;

	@Autowired
	private ActionLogUtil actionLogUtil;

	@Autowired
	private ActionLogService actionLogService;

	public void performUserAction(UserActionVo req) throws UserServiceException
	{
		try {
			
			// Validate Req
			validateUserActionReq(req);
			
			if(!req.isValid())
			{
				throw new UserServiceException("Invalid request");
			}
			
			if(req.getActionType().equals(UserActionType.Change_ProfileType))
			{
				changeUserProfileType(req);
			}
			else if(req.getActionType().equals(UserActionType.Add_Friend_Request))
			{
				addUserAsFriend(req);
			}
			else if (req.getActionType().equals(UserActionType.Remove_Friend_Request))
			{
				removeUserAsFriend(req);
			}
			else if (req.getActionType().equals(UserActionType.Event_Action_Response))
			{
				perform_Event_Action_Operation(req);		
			}
			
			
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);	
		}
		
	}
	
	private void validateUserActionReq(UserActionVo req) throws UserServiceException
	{
		try {
			req.setValid(true);
			
			userActionStructuralValidation(req);
			
			if(req.isValid())
			{
				userActionDBValidation(req);
			}
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);	
		}
		
	}
	
	private void userActionStructuralValidation(UserActionVo request) throws UserServiceException
	{
		try {
			
			if( null == request.getReqUserID())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Action_User.name(), 
						ValidationErrorType.Invalid_Action_User.getValue()));
			}
			if ( null == request.getSrcUserID())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
						ValidationErrorType.Invalid_Request.getValue()));
			}
			
			if( null == request.getActionType())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
						ValidationErrorType.Invalid_Request.getValue()));
			}
			
			 if(request.getActionType().equals(UserActionType.Change_ProfileType))
			 {
				 if( null == request.getProfileType())
				 {
					 request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
								ValidationErrorType.Invalid_Request.getValue()));
				 }
			 }
			 else if((request.getActionType().equals(UserActionType.Add_Friend_Request))
					 || (request.getActionType().equals(UserActionType.Remove_Friend_Request))
					 )
			{
				 
				 if( null == request.getTrgUserID())
				 {
					 request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
								ValidationErrorType.Invalid_Request.getValue()));
				 }
				
			}
			
						
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
	}

	private void userActionDBValidation(UserActionVo request) throws UserServiceException
	{
		try {
			
			 // Get User Details
			User srcUser =  userService.getUserForUserID(request.getSrcUserID());
			
			if( null == srcUser)
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Action_User.name(), 
							ValidationErrorType.Invalid_Action_User.getValue()));
			}
			else {
				request.setSrcUser(srcUser);
			}
				
			
			 if(request.getActionType().equals(UserActionType.Add_Friend_Request))
				{
				 
					User trgUser =  userService.getUserForUserID(request.getTrgUserID());
					
					if( null == trgUser)
					{
						request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
									ValidationErrorType.Invalid_Action_User.getValue()));
					}
					else {
						request.setTrgUser(trgUser);
					}
					
					// Check if There is already Pending Request for Same Pair of Users
					List<EventNotification> pendingEvents = eventNotificationRep.findBySrcUserIDAndTrgUserIDAndSrcActionAndProcessFlag(request.getSrcUserID(), request.getTrgUserID(), 
							request.getActionType(), false);
					
					if(!pendingEvents.isEmpty())
					{
						request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
									ValidationErrorType.Invalid_Request.getValue()));
					}
					
					if(request.isValid())
					{
						// Setup ConnectionID between two Entities 
						request.setUserConnectionId(request.getSrcUserID()+"_"+request.getTrgUserID());
						
						// Check Both are already Friend 
					List<UserConnection> srcUserConnections = userConnectionRep.findBySrcUserIdAndTrgUserId(request.getSrcUserID(), request.getTrgUserID());
					
					if(srcUserConnections.size() > 1)
					{
						throw new UserServiceException("More than one UserConnection found between :"+request.getSrcUserID()+" and :"+request.getTrgUserID());
					}
					
					if(!srcUserConnections.isEmpty())
					{
						if(srcUserConnections.get(0).getStatus().equals(UserConnectionStatusType.Add))
						{
							request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
										ValidationErrorType.Invalid_Action_User.getValue()));	
						}
						else if(srcUserConnections.get(0).getStatus().equals(UserConnectionStatusType.Block))
						{
							request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Add_User_Block.name(), 
										ValidationErrorType.Add_User_Block.getValue()));	
						} 
						
					}
					
					// Check Target User Already Block this User
					
					List<UserConnection> trgUserConnections = userConnectionRep.findBySrcUserIdAndTrgUserId(request.getTrgUserID(), request.getSrcUserID());
					if(!trgUserConnections.isEmpty())
					{
						
						if(trgUserConnections.get(0).getStatus().equals(UserConnectionStatusType.Block))
						{
							request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Add_User_Block.name(), 
										ValidationErrorType.Add_User_Block.getValue()));	
						}
						
					}
					
					
					}
					
					
				}
			 else  if(request.getActionType().equals(UserActionType.Remove_Friend_Request))
				{
				 
					User trgUser =  userService.getUserForUserID(request.getTrgUserID());
					
					if( null == trgUser)
					{
						request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
									ValidationErrorType.Invalid_Action_User.getValue()));
					}
					else {
						request.setTrgUser(trgUser);
					}
				
					
					if(request.isValid())
					{
						// Check Both are already Friend 
					List<UserConnection> userConnections = userConnectionRep.findBySrcUserIdAndTrgUserIdAndAndStatus(request.getSrcUserID(), request.getTrgUserID(), UserConnectionStatusType.Add);
				
					if(userConnections.size() > 1)
					{
						throw new UserServiceException("More than one UserConnection found between :"+request.getSrcUserID()+" and :"+request.getTrgUserID());
					}
				
					
					if(userConnections.isEmpty())
					{
						request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
									ValidationErrorType.Invalid_Action_User.getValue()));
					}
					
					request.setUserConnectionId(userConnections.get(0).getConnectionId());
					}
					
				}
			 else  if(request.getActionType().equals(UserActionType.Event_Action_Response))
				{
				 
				 	// Check if Event is valid for Processing
				 Optional<EventNotification> eventOpt =  eventNotificationRep.findById(request.getActionId());
				 
				 if(!eventOpt.isPresent())
				 {
					 		request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
										ValidationErrorType.Invalid_Request.getValue()));
						
				 }
				 else 
				 {
					 
					 EventNotification event = eventOpt.get();
					 
					 // Verify Event is meant for requested User 
					 if(!event.getTrgUserID().equals(request.getSrcUserID()))
					 {
						 request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
										ValidationErrorType.Invalid_Action_User.getValue()));
					 }
					 
					 if(event.isProcessFlag())
					 {
							request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
										ValidationErrorType.Invalid_Request.getValue()));
						 
					 }
					 else {
						 request.setUserConnectionId(event.getEntityId());
						 request.setEvent(event);
					 }
				 }
					
				}
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage(), e);
		}
	}

	
	private void changeUserProfileType(UserActionVo req ) throws UserServiceException
	{
		Date startT = new Date();
		 try {
			 
			 
				// perform Authorization
			 List<UserRoleType> reqUserRoles = new ArrayList<>();
			 AuthReq authReq = new AuthReq(req.getReqUserID(), req.getSrcUserID(), null,  PermissionType.UsrEdit,  reqUserRoles);
			 
			 boolean hasPermission = authServce.hasPersmission(authReq);
			 
			 if(!hasPermission)
			 {
				 throw new MsgServiceException("Invalid Perssion");
			 }
		
			 // Get User Details
			User user =  userService.getUserForUserID(req.getSrcUserID());
			
			int updFlag = 0;
			if(( null != user ) && (null == user.getProfileType()))
			{
				userService.updateUserProfileType(req.getReqUserID(), req.getSrcUserID(), req.getProfileType(), startT);
				updFlag = 1;
			}
			else if((null != user.getProfileType()) && (!req.getProfileType().equals(user.getProfileType())))
			{
				userService.updateUserProfileType(req.getReqUserID(), req.getSrcUserID(), req.getProfileType(), startT);
				updFlag = 1;
			}
			
			// Update EventLog for Profile Change Event
			
			if(updFlag > 0)
			{
				eventLogService.createChangeUserProfileEvent(req.getReqUserID(),req.getSrcUserID(), req.getProfileType());	
			}
			
			req.setActionRespType(EventActionType.Change_ProfileType);
			
		 }
		 catch(Exception e)
		 {
			 throw new UserServiceException(e.getMessage(), e);
		 }
	
	}
	
	
	
	public void addUserAsFriend(UserActionVo req) throws UserServiceException
	{
		 try {
			
			
			if((null == req.getTrgUser().getProfileType()) ||(req.getTrgUser().getProfileType().equals(VisibilityType.Public)))
			{
				// Perform Add Auto User as Friend Operation
				perform_Auto_AddFriend_Operation(req);
				
			}
			else {
				// Send Notification to Target User for Add Friend Request
				perform_Request_AddFriend_Operation(req);
			}
			
		 }
		 catch(Exception e)
		 {
			 throw new UserServiceException(e.getMessage(), e);
		 }
		
		
	}
	
	private void perform_Request_AddFriend_Operation(UserActionVo req) throws UserServiceException
	{
		try {
			// Step 1 : Prepare Event Notification Entity Object
			EventNotification event = helperBean.prepareEventNotification(req);
			
			event.setProcessFlag(false);
			
			// Step 2 : Prepare UserFriend Entity Object
			List<UserConnection> ucList = new ArrayList<>();
			
			// Step 3 : Persist Data to DB
			saveAddFriendReqToDb(event,ucList, null, null);
			req.setActionRespType(EventActionType.Add_Friend_Req_Sent);

			// Step 4 : Add Action Log for Notificaiotn
			ActionLogReq actionLogReq =  actionLogUtil.createAddUserConnectionReqLogs(req.getReqUserID(), req.getSrcUserID(), req.getTrgUserID());
			actionLogService.processActionLogRequest(actionLogReq);

		}
		catch(Exception e)
		{
			 throw new UserServiceException(e.getMessage(), e);
		}
	}
	
	private void perform_Auto_AddFriend_Operation(UserActionVo req) throws UserServiceException
	{
		try {
			// Step 1 : Prepare Event Notification Entity Object
			
			EventNotification event = helperBean.prepareEventNotification(req);
			
			// Add Event Completion Details 
			event.setTrgAction(UserActionType.Add_Friend_Response);
			event.setTrgActionStatus(UserActionStatusType.Auto_Approve);
			event.setResponseDate(req.getToday());
			event.setProcessFlag(true);
			
			// Step 2 : Prepare UserFriend Entity Object
			List<UserConnection> ucList  = helperBean.prepareUserConnections(req.getUserConnectionId(),UserConnectionStatusType.Add,  req.getSrcUserID(), req.getTrgUserID(), req.getReqUserID(), req.getToday());
			
			// Prepare OneToOne Channel for Both Participant 
			Channel chnl = helperBean.prepareOneToOneMsgChannel(req.getReqUserID(), req.getToday());
			
			// Prepare Chnl Participant for both users 
			
			List<ChannelParticipant> cpList = helperBean.prepareChannelParticipants(chnl.getId(), chnl.getType(),req.getSrcUserID(),req.getTrgUserID(),req.getReqUserID(), req.getToday());
			
			// Step 3 : Persist Data to DB
			saveAddFriendReqToDb(event,ucList, chnl, cpList);
			
			
			// Send EventLog to EventLogging Service
			eventLogService.createAddUserConnectionEvent(req.getReqUserID(), req.getSrcUserID(), req.getTrgUserID(), UserActionStatusType.Auto_Approve );

		}
		catch(Exception e)
		{
			 throw new UserServiceException(e.getMessage(), e);
		}
	}
	
	@Transactional
	public void saveAddFriendReqToDb(EventNotification event,List<UserConnection> ucList, Channel chnl, List<ChannelParticipant> cpList ) throws UserServiceException
	{
		try {
			
			if( null == ucList)
			{
				ucList = new ArrayList<>(); 	
			}
			
			if( null == cpList)
			{
				cpList = new ArrayList<>();
			}
			
				if(!ucList.isEmpty())
				{
					userConnectionRep.saveAll(ucList);	
				}
				
				if( null != event)
				{
					eventNotificationRep.save(event);	
				}
				
				if( null != chnl)
				{
					chnlService.createChannel(chnl);
				
					
				}
				
				if(!cpList.isEmpty())
				{
					chnlService.saveChannelParticipents(cpList);
					
				}
		}
		catch(Exception e)
		{
			 throw new UserServiceException(e.getMessage(), e);
		}
	}
	
	
	public void removeUserAsFriend(UserActionVo req) throws UserServiceException
	{
		
		 try {
			
				perform_Remove_Friend_Operation(req);
					 }
		 catch(Exception e)
		 {
			 throw new UserServiceException(e.getMessage(), e);
		 }
		
		
	}
	
	
	private void perform_Remove_Friend_Operation(UserActionVo req) throws UserServiceException
	{
		try {
			// Step 1 : Prepare Event Notification Entity Object
			EventNotification event = helperBean.prepareEventNotification(req);
			
			// Add Event Completion Details 
			event.setTrgAction(UserActionType.Remove_Friend_Request);
			event.setTrgActionStatus(UserActionStatusType.Auto_Approve);
			event.setResponseDate(req.getToday());
			event.setProcessFlag(true);
			
			
			// Step 3 : Persist Data to DB
			saveRemoveFriendReqToDb(event);
		}
		catch(Exception e)
		{
			 throw new UserServiceException(e.getMessage(), e);
		}
	}
	
	
	@Transactional
	public void saveRemoveFriendReqToDb(EventNotification event) throws UserServiceException
	{
		try {
				// Delete U1 =?
			//userConnectionRep.deleteByConnectionId(event.getEntityId());
			
				// Remove only One Way Connection not for both the users 
				userConnectionRep.deleteBySrcUserIdAndTrgUserIdAndStatus(event.getSrcUserID(), event.getTrgUserID(), UserConnectionStatusType.Add);
				eventNotificationRep.save(event);
				eventLogService.createRemoveUserConnectionEvent(event.getSrcUserID(), event.getSrcUserID(), event.getTrgUserID());
		}
		catch(Exception e)
		{
			 throw new UserServiceException(e.getMessage(), e);
		}
	}
	

	private void perform_Event_Action_Operation(UserActionVo req) throws UserServiceException
	{
		try {
			// Step 1 : Prepare Event Notification Entity Object
			EventNotification event = req.getEvent();
			
			if(event.getSrcAction().equals(UserActionType.Add_Friend_Request))
			{
				perform_Accept_FriendRequest_Operation(req);
			}
			
			
		}
		catch(Exception e)
		{
			 throw new UserServiceException(e.getMessage(), e);
		}
	}
	
	private void perform_Accept_FriendRequest_Operation(UserActionVo req) throws UserServiceException
	{
		List<UserConnection> ucList = new ArrayList<>();
		Channel chnl = null;
		List<ChannelParticipant> cpList = null;
		try {
			// Step 1 : Prepare Event Notification Entity Object
			EventNotification event = req.getEvent();
			
			req.setTrgUserID(event.getTrgUserID());
			
			// Add Event Completion Details 
			event.setTrgAction(UserActionType.Add_Friend_Response);
			event.setTrgActionStatus(req.getActionStatus());
			event.setResponseDate(req.getToday());
			event.setProcessFlag(true);
			
			// Step 2 : Prepare UserFriend Entity Object
			
			if(req.getActionStatus().equals(UserActionStatusType.Approve))
			{
				ucList = helperBean.prepareUserConnections(event.getEntityId(),UserConnectionStatusType.Add,event.getSrcUserID(), event.getTrgUserID(), req.getReqUserID(), req.getToday());
				
				// Prepare OneToOne Channel for Both Participant 
				chnl = helperBean.prepareOneToOneMsgChannel(req.getReqUserID(), req.getToday());
				
				// Prepare Chnl Participant for both users 
				cpList = helperBean.prepareChannelParticipants(chnl.getId(), chnl.getType(),event.getSrcUserID(),event.getTrgUserID(),req.getReqUserID(), req.getToday());
				
				
				
			}
			else if(req.getActionStatus().equals(UserActionStatusType.Block))
			{
				UserConnection uc = helperBean.prepareUserConnection(event.getEntityId(),UserConnectionStatusType.Block, event.getTrgUserID(), event.getSrcUserID(), req.getReqUserID(), req.getToday());	
				ucList.add(uc);
			}
			
			// Step 3 : Persist Data to DB
			saveAddFriendReqToDb(event,ucList,chnl,cpList);
			
			// Send EventLog to EventLogging Service
			if(req.getActionStatus().equals(UserActionStatusType.Approve))
			{
				eventLogService.createAddUserConnectionEvent(event.getSrcUserID(), event.getSrcUserID(), event.getTrgUserID(), req.getActionStatus() );
			}
			else if(req.getActionStatus().equals(UserActionStatusType.Block))
			{
				eventLogService.createBlockUserConnectionEvent(req.getReqUserID(), event.getTrgUserID(), event.getSrcUserID(), req.getActionStatus() );
			}
			
		}
		catch(Exception e)
		{
			 throw new UserServiceException(e.getMessage(), e);
		}
	}
	

	public void updateUserLoginStatus(String userID) throws UserServiceException
	{
		try {
			
			userService.updateUserLoginStatus(userID);
		}
		catch(Exception e)
		{
			 throw new UserServiceException(e.getMessage(), e);
		}
	}
	
	public Page<SearchUserVo> searchUsersAndMsgChnls(String reqUserID, String searchKey, int page, int size) throws UserServiceException
	{
		Page<SearchUserVo> result = null;
		try {
			
    		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
    		Pageable pageable = PageRequest.of(page, size, sort);
    		
    		result = userService.getUserBySearchString(searchKey, pageable);
    		

			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage(), e);	
		}
		
		return result;
	}
	
	public SearchResp getSearchResponSearchByUsersAndMsgChnls(String reqUserID, String searchKey, int page, int size) throws UserServiceException
	{
		SearchResp result = null;
		try {
			
			result = new SearchResp();
			result.setSearchKey(searchKey);
			result.setReqUserId(reqUserID);
			
			Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
    		Pageable pageable = PageRequest.of(page, size, sort);
    	
			
			Page<EventLog> resultPage = eventLogService.searchUsersAndMsgChnls(reqUserID, searchKey, pageable);
			
    		transformSearchResult(result, resultPage);
    			
   		
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage(), e);	
		}
		
		return result;
	}
	
	
	private void processSearchResult(SearchResp result, List<EventLog> searchResult) throws UserServiceException
	{
		try {
			
			searchResult.stream().forEach(log -> {
				if(log.getSrcType().equals(EventEntityType.User))
				{
					result.getSearchUserIds().add(log.getSrcKey());
				}
				else if(log.getSrcType().equals(EventEntityType.Msg_Chnl))
				{
					result.getSearchMsgChnlIds().add(log.getSrcKey());
				}
			});
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
	}
	
	private void transformSearchResult(SearchResp result, Page<EventLog> searchPage) throws UserServiceException
	{
		try {
			
			// Step 1: Extract UserID and MsgChnlIDs from searchPag
			
			processSearchResult(result, searchPage.getContent());
			
			List<SearchVo> resultList = new ArrayList<SearchVo>();
			
			if(!result.getSearchUserIds().isEmpty())
			{
				List<User> users = userService.getUserForUserIds(result.getSearchUserIds());
				
				
				users.stream().forEach(user-> {
					try {
						SearchVo userVo = helperBean.convertUserToSearchResultUser(user);
						resultList.add(userVo);
					} catch (Exception e) {
						throw new RuntimeException(e.getMessage(), e);
					}
				});
			}
			
			if(!result.getSearchMsgChnlIds().isEmpty())
			{
				List<Channel> chnls = chnlService.getChannelByIds(result.getSearchMsgChnlIds());
				
				chnls.stream().forEach(chnl-> {
					try {
						
						SearchVo chnlVo = helperBean.convertMsgChannelToSearchResult(chnl);
						resultList.add(chnlVo);
					}
					catch(Exception e)
					{
						throw new RuntimeException(e.getMessage(), e);
					}
				});
				
			}
			
			
			Page<SearchVo> resultPage = 	PageableExecutionUtils.getPage(resultList, searchPage.getPageable(), ()-> searchPage.getTotalElements());
			result.setResult(resultPage);
			
			
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
	}
	
	

	
	
}
