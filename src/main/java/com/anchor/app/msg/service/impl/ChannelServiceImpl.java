package com.anchor.app.msg.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.exception.ValidationException;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.msg.dao.ChannelDao;
import com.anchor.app.msg.dao.ChannelParticipantDao;
import com.anchor.app.msg.dao.MsgBoxRelationDao;
import com.anchor.app.msg.enums.ChannelActionType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.PermissionType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.exceptions.ChannelParticipantDaoException;
import com.anchor.app.msg.exceptions.ChannelServiceException;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.repository.ChannelParticipantRepository;
import com.anchor.app.msg.service.ChannelService;
import com.anchor.app.msg.service.MsgService;
import com.anchor.app.msg.vo.ChannelParticipantResponse;
import com.anchor.app.msg.vo.ChannelResponse;
import com.anchor.app.msg.vo.ChannelUser;
import com.anchor.app.msg.vo.ChannelVo;
import com.anchor.app.msg.vo.ChnlUserActionVo;
import com.anchor.app.msg.vo.CreateChannelVo;
import com.anchor.app.msg.vo.MessageVo;
import com.anchor.app.msg.vo.MsgAggregateVo;
import com.anchor.app.msg.vo.UserChannel;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.oauth.service.UserService;
import com.anchor.app.oauth.vo.AuthReq;
import com.anchor.app.util.HelperBean;
import com.anchor.app.vo.ErrorMsg;

@Service
public class ChannelServiceImpl implements ChannelService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HelperBean helper;

	@Autowired
	private AuthService authServce;

	
	@Autowired
	private UserService userService;

	@Autowired
	private MsgService msgService;
	
	@Autowired
	private ChannelDao channelDao;

	@Autowired
	private ChannelParticipantDao cprDao;
	
	@Autowired
	private ChannelParticipantRepository cpRep;


	@Override
	public Channel createChannel(CreateChannelVo request) throws ChannelServiceException {
		
		Channel response = null;
		Date startT = new Date();
		try {
			// Step 1 : Validate Channel Request
			validateChannelRequest(request);
			
			// Step 2: If request is valid then store to DB
			if(!request.isValid())
			{
				throw new ChannelServiceException("Invalid Create Channel request");
			}
			
			// Add Owner User ID to Participent User
			
			helper.parepareCreateChannelVo(request, startT);
			
			// Step 3 : Save Data to DB
			saveCreateChannelVo(request);
			
			response = request.getResponse();
			
		}
		catch(Exception e )
		{
			logger.error(e.getMessage(), e);
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return response;
	}
	
	
	@Transactional
	public void saveCreateChannelVo(CreateChannelVo requestVo) throws ChannelServiceException
	{
		try {
			if( null == requestVo)
			{
				throw new ChannelServiceException("Invalid Create channel request");
			}	
			
			if( null != requestVo.getResponse())
			{
				channelDao.saveChannel(requestVo.getResponse());
			}
			
			if( !requestVo.getChannelUsers().isEmpty())
			{
				cprDao.saveAll(requestVo.getChannelUsers());
			}
			
			
			
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		
	}
	
	private void validateChannelRequest(CreateChannelVo request) throws ValidationException
	{
		request.setValid(true);
		
		// Structural Validation
		chnnaleRequestStructuralValidation(request);
		
		if(request.isValid())
		{
			// DB Validation
			
			chnnaleRequestDBValidation(request);
		}
			
	}
	
	private void chnnaleRequestStructuralValidation(CreateChannelVo request) throws ValidationException
	{
		try {
		
			if( null == request.getType())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_Type.name(), ValidationErrorType.Invalid_Channel_Type.getValue()));
			}
			else {
				if(!request.getType().equals(ChannelType.Messaging))
				{
					if( null == request.getName())
					{
						request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_Name.name(), ValidationErrorType.Invalid_Channel_Name.getValue()));
					}
				}
			}
			
			if( null == request.getVisibility())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_Visibility.name(), ValidationErrorType.Invalid_Channel_Visibility.getValue()));
			}
			
			if( null == request.getSubscriptionType())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_Subscription.name(), ValidationErrorType.Invalid_Channel_Subscription.getValue()));
			}
		
			
			
			if(!request.getChannelUsers().isEmpty())
			{
				
				for (ChannelParticipant cp : request.getChannelUsers())
				{
					if(null == cp.getUserID())
					{
						request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_Participant.name(), ValidationErrorType.Invalid_Channel_Participant.getValue()));
					}
				}
			}
			
		}
		catch (Exception e)
		{
			throw new ValidationException(e.getMessage(), e);
		}
		
	}
	
	private void chnnaleRequestDBValidation(CreateChannelVo request) throws ValidationException
	{
		try {
		
			if( null != request.getName())
			{
				Channel existingChnl = channelDao.getChannelByName(request.getName());
				if( null != existingChnl)
				{
					request.setValid(false);
					request.getErrors().add(new ErrorMsg(ValidationErrorType.Channel_Already_Present.name(), ValidationErrorType.Channel_Already_Present.getValue()));
				}
				
			}
			
			if(!request.getChannelUsers().isEmpty())
			{
				Collection chnlUsers = request.getChannelUsers().stream()
						.map(ChannelParticipant::getUserID)
	                    .collect(Collectors.toList());
				
				List<User> users = userService.getUserForUserIds(chnlUsers);
		
				
					// All Chnl Users are not available in System 
					
					Map<String,User> userMap = users.stream()
												.collect(Collectors.toMap(User::getUid, user -> user));
					
					for (ChannelParticipant cp : request.getChannelUsers())
					{
						if(!userMap.containsKey(cp.getUserID()))
						{
							request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_Participant.name(),cp.getUserID()));
						}
				
					}
				
			}
			
		}
		catch (Exception e)
		{
			throw new ValidationException(e.getMessage(), e);
		}
		
	}


	@Override
	public Channel getChannelByID(String chnlID) throws ChannelServiceException {
		
		Channel result = null;
		try {
			
			result = channelDao.getChannelByID(chnlID);
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return result;
	}


	@Override
	public List<ChannelParticipant> getChannelParticipantForChannelID(String chnlID,Date today) throws ChannelServiceException {
		List<ChannelParticipant> result = null;
		
		try {
			
			result = cprDao.getChannelParticipentByChannelID(chnlID, today);
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return result;
	}


	@Override
	public ChannelResponse getUserChannels(String userID, ChannelType type) throws ChannelServiceException {
		
		ChannelResponse response  = new ChannelResponse() ;
		try {
			
			response.setUserID(userID);
			response.setType(type);
			
			List<ChannelParticipant> chnlParticipants = null;
			boolean activeFlag = true;
			Date today = new Date();
			// Need Data for All Channels
			if(type.equals(ChannelType.All))
			{
				
				chnlParticipants = cprDao.getActiveChannelParticipentForUser(userID);	
			}
			else {
				if(!type.equals(ChannelType.Messaging))
				{
					chnlParticipants = cprDao.getActiveChannelParticipentForUser(userID, activeFlag, today, type);
				}
				else {
					chnlParticipants = cprDao.getActiveChannelParticipentForUser(userID, type);
				}
				
			}
			
			
			if(!chnlParticipants.isEmpty())
			{
				// get Channel Details for Given ChnlParticipent Record and prepare response object
				
				List chnlIDs=chnlParticipants.stream().map(p->p.getChannelID()).collect(Collectors.toList());
		
				// Get Channel 
				List<Channel> chnlList = channelDao.getChannelForIds(chnlIDs);
				
				// Get Latest Chnl Message ID if Any
				Collection latestMsgID = chnlList.stream().filter(chnl -> null != chnl.getLatestMsgId())
											.map(Channel:: getLatestMsgId)
											.collect(Collectors.toList());
				
				// Get List of Latest Message ID
				List<Message>  msgList = msgService.getMessagesByMsgIds(latestMsgID);
						
				List<UserChannel> chnlVos = prepareUserChannels(userID, chnlParticipants,chnlList,msgList, today);
				response.getChannels().addAll(chnlVos);
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return response;
	}

	/*
	private List<ChannelVo> transformChnlDetailsToChannelVo(String userID, List<ChannelParticipant> chnlParticipants, List<Channel> chnlList,
			 Date todaysDate) throws ChannelServiceException
	{
		List<ChannelVo> chnlVos = new ArrayList<>();
		
		try {
			
			if(!chnlList.isEmpty())
			{
				Map<String, Channel> chnlMap  = chnlList.stream()
			            .collect(Collectors.toMap(Channel::getId, ch -> ch));
			
				
				for (ChannelParticipant cp : chnlParticipants)
				{
					ChannelVo cvo = new ChannelVo();
					
					if(chnlMap.containsKey(cp.getChannelID()))
					{
						Channel cnl = chnlMap.get(cp.getChannelID());
						
						List<ChannelParticipant> chnlParticipents = cprDao.getActiveChannelParticipentForChannel(cnl.getId(), true, todaysDate);
						
						
						Collection chnlUsersIds = chnlParticipents.stream()
								.map(ChannelParticipant::getUserID)
			                    .collect(Collectors.toList());
						
						List<User> chnlUsers = userService.getUserForUserIds(chnlUsersIds);
				
						cvo.getChnlUsers().addAll(chnlUsers);
							
						// Get All Channel Participents for Respective Channel Active Today
						cvo.setId(cnl.getId());
						cvo.setType(cnl.getType());
						cvo.setVisibility(cnl.getVisibility());
						cvo.setSubscriptionType(cnl.getSubscriptionType());
						if(null == cnl.getName())
						{
							// Prepare Channel Name by Participants Details
							String chnlName = generateChannelNameForChannel(userID, chnlUsers);
							cvo.setName(chnlName);
						}
						else {
							cvo.setName(cnl.getName());	
						}
						
						cvo.setDescription(cnl.getDescription());
						cvo.setEncriptedName(cnl.getEncriptedName());
						cvo.getImageList().addAll(cnl.getImageList());
						cvo.setActive(cp.isActive());
						cvo.setCreatedBy(cnl.getCreatedBy());
						cvo.setCreatedOn(cnl.getCreatedOn());
						cvo.setModifiedBy(cnl.getModifiedBy());
						cvo.setModifiedOn(cnl.getModifiedOn());
						
						cvo.getUserRoles().addAll(cp.getUserRoles());
						cvo.setValidFrom(cp.getValidFrom());
						cvo.setValidTo(cp.getValidTo());
						
						chnlVos.add(cvo);
						
					}
					
				}
			
			}
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return chnlVos;
	}
	
	*/

	private List<UserChannel> prepareUserChannels(String userID, List<ChannelParticipant> chnlParticipants, List<Channel> chnlList,
			List<Message> msgList, Date todaysDate) throws ChannelServiceException
	{
		List<UserChannel> chnlVos = new ArrayList<>();
		
		
		try {
			
			if(!chnlList.isEmpty())
			{
				Map<String,Message> msgMap = new HashMap();
				
				Map<String, Channel> chnlMap  = chnlList.stream()
			            .collect(Collectors.toMap(Channel::getId, ch -> ch));
			
				
				if(!msgList.isEmpty())
				{
					msgMap = msgList.stream()
							.collect(Collectors.toMap(Message::getId, Function.identity(), (first, second) -> first));
				}
				
				for (ChannelParticipant cp : chnlParticipants)
				{
					UserChannel cvo = new UserChannel();
					cvo.setUnreadCount(cp.getUnReadCount());
					cvo.setUnReadDate(cp.getUnReadDate());
					cvo.setUnReadMsgID(cp.getUnReadMsgID());
					
					if(chnlMap.containsKey(cp.getChannelID()))
					{
						Channel cnl = chnlMap.get(cp.getChannelID());
						
						cvo.setChnlId(cnl.getId());
						
						List<ChannelParticipant> chnlParticipents = cprDao.getActiveChannelParticipentForChannel(cnl.getId(), true, todaysDate);
						
						
						// Get Channel User details
						Collection chnlUsersIds = chnlParticipents.stream()
								.map(ChannelParticipant::getUserID)
								.distinct()
			                    .collect(Collectors.toList());
						
						List<User> chnlUsers = userService.getUserForUserIds(chnlUsersIds);
						List<ChannelUser> chUsers = helper.prepareChannelUserFromParticipent(chnlParticipents,chnlUsers);
						cvo.setChnlUsers(chUsers);
						
						if(null == cnl.getName())
						{
							
							// Prepare Channel Name & Profile Image by Participants Details
							generateChannelNameAndProfileForChannel(cvo, userID, chUsers);
							//cvo.setName(chnlName);
							
							
						}
						else {
							cvo.setName(cnl.getName());	
						}
						
						// Setup Channel Type and SubType
						cvo.setType(cnl.getType());
						cvo.setSubType(cnl.getSubType());
						
						if((null != cnl.getLatestMsgId()) && (!cnl.getLatestMsgId().equalsIgnoreCase("NA")))
								
						{
							if(msgMap.containsKey(cnl.getLatestMsgId()))
							{
								cvo.setMsg(msgMap.get(cnl.getLatestMsgId()));
								
								cvo.setMsgDate(cnl.getLatestMsgDate());		
							}
							
						}
						
						cvo.setActive(cp.isActive());
						
						cvo.getUserRoles().addAll(cp.getUserRoles());
						
						chnlVos.add(cvo);
						
					}
					
				}
			
			}
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return chnlVos;
	}
	
	private void generateChannelNameAndProfileForChannel(UserChannel cvo, String userID, List<ChannelUser> chUsers) throws ChannelServiceException
	{
		String result = null;
		try {
			
			// Step 1 : Fetch all Participents for Channel
			if(!chUsers.isEmpty())
			{
				List<String> prtUsers =  chUsers.stream()  
	                    .filter(p ->!p.getUserID().equals(userID) )   // filtering price  
	                    .map(pm ->pm.getFirstName())          // fetching price  
	                    .collect(Collectors.toList());  
				
				
				result = helper.convertStringListToString(prtUsers);
				cvo.setName(result);
			}
			if( null == result)
			{
				result = "Self";
			}
			
				
				
				if(chUsers.size()== 1)
				{
					ChannelUser u = chUsers.stream()  
		                    .filter(p ->p.getUserID().equals(userID))
		                    .findAny()
		                    .orElse(null);
					
					if(null != u.getProfileImage())
					{
						
						cvo.setChnLogo(u.getProfileImage());
					}
						
					
				}
				else if ( chUsers.size() == 2)
				{
					ChannelUser u = chUsers.stream()  
		                    .filter(p ->!p.getUserID().equals(userID))
		                    .findAny()
		                    .orElse(null);
					
					if(null != u.getProfileImage())
					{
						
						cvo.setChnLogo(u.getProfileImage());
					}
		                    
				}
			
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		
		
	}


	@Override
	public void addUserToChannel(ChnlUserActionVo request)
			throws ChannelServiceException {
		
		Date startT = new Date();
		try {
			// Step1: Validate AddUserChannel Request
			validateChnlUserActionRequest(request);
			
			if(!request.isValid())
			{
				throw new ChannelServiceException("Invalid request");
			}
			
			// perform Authorization
			AuthReq authReq = new AuthReq(request.getUserID(), request.getReqUserID(), request.getChannelID(),  PermissionType.UsrAdd,  request.getReqUserRoles());
			
			boolean hasPermission = authServce.hasPersmission(authReq);
			
			if(!hasPermission)
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Permission_Error.name(), ValidationErrorType.Permission_Error.getValue()));
				throw new ChannelServiceException("Authorization failed");
			}
			// Save Data DB
			addUserToChnRequestSaveToDB(request,startT);
			
			// Send Even to Queue for User Addition 
			
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		
	}
	
	private void validateChnlUserActionRequest(ChnlUserActionVo request) throws ChannelServiceException
	{
		try {
			request.setValid(true);
			
			// Step 1: Structural Validation 
			chnlUserActionStructuralValidation(request);
	
			if(request.isValid())
			{
				// Step 2: DB Validation
				chnlUserActionDbValidation(request);

			}
		
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
	}
	
	private void chnlUserActionStructuralValidation(ChnlUserActionVo request)
	{
		if(null == request.getReqUserID())
		{
			request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Action_User.name(), ValidationErrorType.Invalid_Action_User.getValue()));	
		}
		
		if( null == request.getUserID())
		{
			request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_User.name(), ValidationErrorType.Invalid_Channel_User.getValue()));
		}
		
		if( null == request.getChannelID())
		{
			request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_ID.name(), ValidationErrorType.Invalid_Channel_ID.getValue()));
		}
		
		if( null == request.getActionType())
		{
			request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_Action.name(), ValidationErrorType.Invalid_Channel_Action.getValue()));
		}
	}
	
	private void chnlUserActionDbValidation(ChnlUserActionVo request) throws ChannelParticipantDaoException
	{
		// Fetch all users of Channel to verify requested user is not already part of channel
		List<String> chnlUsers = new ArrayList<>();
		chnlUsers.add(request.getReqUserID());
		chnlUsers.add(request.getUserID());
		List<ChannelParticipant> chnlPartis = cprDao.getActiveChannelParticipentForUser(request.getChannelID(), chnlUsers, request.getToday());
		
		if(request.getReqUserID().equalsIgnoreCase(request.getUserID()))
		{
			request.setActionType(ChannelActionType.RemoveSelf);
		}
		
		if( chnlPartis.isEmpty())
		{
			request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_User.name(), ValidationErrorType.Invalid_Channel_User.getValue()));
		}
		else {
			
			// Verify Sender is part of chnl Participants
			ChannelParticipant reqUserRec = helper.getChnlParticipentRecord(request.getReqUserID(), chnlPartis);
			
			
			if(null == reqUserRec)
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_User.name(), ValidationErrorType.Invalid_Channel_User.getValue(), request.getReqUserID()));
			}
			else {
				request.setChnlType(reqUserRec.getChannelType());
				request.getReqUserRoles().addAll(reqUserRec.getUserRoles());
			}
			
			if(request.getActionType().equals(ChannelActionType.AddUser))
			{
				// Veriify Requested Add User is not already part of channel
				ChannelParticipant addUserRec = helper.getChnlParticipentRecord(request.getUserID(), chnlPartis);
				
				if( null != addUserRec)
				{
					request.setValid(false);
					request.getErrors().add(new ErrorMsg(ValidationErrorType.Channel_User_Already_Present.name(), ValidationErrorType.Channel_User_Already_Present.getValue(), request.getUserID()));
				}
					
			}
			else if ((request.getActionType().equals(ChannelActionType.RemoveUser))
					|| (request.getActionType().equals(ChannelActionType.RemoveSelf)))
			{
				// Veriify Requested Add User is already part of channel
				ChannelParticipant removeUserRec = helper.getChnlParticipentRecord(request.getUserID(), chnlPartis);
				
				if( null == removeUserRec)
				{
					request.setValid(false);
					request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_User.name(), ValidationErrorType.Invalid_Channel_User.getValue(), request.getUserID()));
				}
				
				request.setResponse(removeUserRec);
				
			}
			
		}
	}
	
	private void addUserToChnRequestSaveToDB(ChnlUserActionVo request, Date startT) throws ChannelServiceException
	{
		try {
			User user = userService.getUserForUserID(request.getUserID());
			
			if( null == user)
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_User.name(), ValidationErrorType.Invalid_Channel_User.getValue(), request.getUserID()));
			}
		
			ChannelParticipant cp = new ChannelParticipant();
			String cpID = helper.getSequanceNo(SequenceType.ChannelParticipant);
			cp.setId(cpID);
			cp.setChannelID(request.getChannelID());
			cp.setChannelType(request.getChnlType());
			cp.setUserID(request.getUserID());
			cp.setActive(true);
			
			if(request.getChnlType().equals(ChannelType.Messaging))
			{
				cp.getUserRoles().add(UserRoleType.Author);
					
			}
			else if ((request.getChnlType().equals(ChannelType.Blog))
					|| (request.getChnlType().equals(ChannelType.Audio))
					|| (request.getChnlType().equals(ChannelType.Video))
					)
			{
				
					cp.getUserRoles().add(UserRoleType.Viewer);
				
			}
			
			cp.setValidFrom(startT);
			cp.setValidTo(helper.getInfiniteDate());
			cp.setCreatedBy(request.getReqUserID());
			cp.setCreatedOn(startT);
			cp.setModifiedBy(request.getReqUserID());
			cp.setModifiedOn(startT);
			
			cprDao.save(cp);
			request.setResponse(cp);
			
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
	}


	@Override
	public void removeUserFromChannel(ChnlUserActionVo request) throws ChannelServiceException {
		
		Date startT = new Date();
		try {
			// Step1: Validate AddUserChannel Request
			validateChnlUserActionRequest(request);
			
			if(!request.isValid())
			{
				throw new ChannelServiceException("Invalid request");
			}
			
			PermissionType reqPermisosn  = null;
			if(request.getActionType().equals(ChannelActionType.RemoveSelf))
			{
				reqPermisosn = PermissionType.UsrDeleteSelf;
				
			}
			else {
				reqPermisosn = PermissionType.UsrDelete;
				
			}
			// perform Authorization
			AuthReq authReq = new AuthReq(request.getUserID(), request.getReqUserID(), request.getChannelID(),  reqPermisosn,  request.getReqUserRoles());
			
			boolean hasPermission = authServce.hasPersmission(authReq);
			
			if(!hasPermission)
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Permission_Error.name(), ValidationErrorType.Permission_Error.getValue()));
				throw new ChannelServiceException("Authorization failed");
			}
			// Save Data DB
			Date userValidaityDate = helper.removeDaysToDate(startT, 1);
			
			//removeUserFromChnReuestSaveToDB(request,userValidaityDate);
			
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
	
		
	}
	
	private void removeUserFromChnReuestSaveToDB(ChnlUserActionVo request, Date validToDate) throws ChannelServiceException
	{
		try {
			
			if(request.getChnlType().equals(ChannelType.Messaging))
			{
				// Close ChannelParticipents validTo till Todays Date
				cprDao.closeUserAccessonChannel(request.getReqUserID(), request.getUserID(), request.getChannelID(), validToDate);
				
			}
			else {
				// For Other channel Simply remove record from ChannelParticipents
				cprDao.removeUserAccessonChannel(request.getUserID(), request.getChannelID());
			}
			
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
	}


	@Override
	public ChannelParticipant getChannelParticipantForChannelID(String userID, String chnlID, Date today)
			throws ChannelServiceException {
		
		ChannelParticipant result = null;
		
		try {
			
			List<ChannelParticipant> cpList  = cprDao.getActiveChannelParticipentForUser(chnlID, userID, today);
			
			if(!cpList.isEmpty())
			{
				result = cpList.get(0);
			}
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return result;
	}


	@Override
	public void updateChnlParticipentReadStatus(String reqUserID, Collection userIDs, String channelID,
			ChannelActionType action, int count, Date validToDate) throws ChannelServiceException {
		try {
		
			cprDao.updateChnlParticipentReadStatus(reqUserID, userIDs, channelID, action, count, validToDate);
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
	}

	@Override
	public void updateChnlParticipentFirstUnreadMsgIDAndDate(String reqUserID, Collection userIDs, String channelID,
			String msgID, Date msgDate) throws ChannelServiceException {
		try {
			
			cprDao.updateChnlParticipentFirstUnreadMsgIDAndDate(reqUserID, userIDs, channelID, msgID, msgDate);
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
	}


	@Override
	public void updateChannelLatestMsgIdAndDate(String channelID, String msgID, Date date)
			throws ChannelServiceException {
			
		try {
			channelDao.updateChannelLatestMsgIdAndDate(channelID, msgID, date);
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
	}


	@Override
	public void getChannelParticipantForChannel(ChannelParticipantResponse resp) throws ChannelServiceException {
		
		
		try {
			
			resp.setValid(true);
			if( null == resp.getChnlId())
			{
				resp.setValid(false);
				resp.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_ID.name(), ValidationErrorType.Invalid_Channel_ID.getValue()));
			}	
			
			if(!resp.isValid())
			{
				throw new ChannelServiceException("Invalid request");
			}
			
						
			List<ChannelUser> chUsers =  getChannelUser(resp.getChnlId());
			
			resp.setChnlUsers(chUsers);
			
			
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		

	}
	
	private List<ChannelUser> getChannelUser(String chnlID) throws ChannelServiceException
	{
		List<ChannelUser> Chusers = null;
		
		try {
			List<ChannelParticipant> chUsers = cprDao.getChannelParticipentByChannel(chnlID);
			Collection chnlUsers = chUsers.stream()
					.map(ChannelParticipant::getUserID)
                    .collect(Collectors.toList());
			
			List<User> users = userService.getUserForUserIds(chnlUsers);
	
			
				// All Chnl Users are not available in System 
				
			Chusers = helper.prepareChannelUserFromParticipent(chUsers, users);
		
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return Chusers;
	}


	@Override
	public List<ChannelParticipant> getUserEnrolmentDetailsForChannel(String chnlID, String userID)
			throws ChannelServiceException {
	List<ChannelParticipant> result = null;
		
		try {
			
			result = cprDao.getUserEnrolmentDetailsForChannel(chnlID, userID);
			
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);
		}
		
		return result;
	}


	@Override
	public void createChannel(Channel chnl) throws ChannelServiceException {
		try {
			
			channelDao.saveChannel(chnl);
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);	
		}
	}


	@Override
	public void saveChannelParticipents(List<ChannelParticipant> cpList) throws ChannelServiceException {
		
	try {
			
			cpRep.saveAll(cpList);
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);	
		}
		
	}


	@Override
	public List<Channel> getChannelByIds(Collection chnlIds) throws ChannelServiceException {
		
		List<Channel> result = null;
		try {
			
			result = channelDao.getChannelForIds(chnlIds);
		}
		catch(Exception e)
		{
			throw new ChannelServiceException(e.getMessage(), e);	
		}
		
		return result;
	}



}

