package com.anchor.app.msg.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation.LookupOperationBuilder;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation.UnwindOperationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anchor.app.action.service.ActionLogService;
import com.anchor.app.action.util.ActionLogUtil;
import com.anchor.app.action.vo.ActionLogReq;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.event.service.EventLogService;
import com.anchor.app.event.service.EventService;
import com.anchor.app.exception.ConversionException;
import com.anchor.app.media.model.Media;
import com.anchor.app.msg.dao.ChannelParticipantDao;
import com.anchor.app.msg.dao.MessageDao;
import com.anchor.app.msg.dao.MsgBoxRelationDao;
import com.anchor.app.msg.enums.ChannelActionType;
import com.anchor.app.msg.enums.ChannelSubscriptionType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.ChannelVisibility;
import com.anchor.app.msg.enums.MsgActionType;
import com.anchor.app.msg.enums.MsgType;
import com.anchor.app.msg.enums.PermissionType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.exceptions.MsgServiceException;
import com.anchor.app.msg.model.Attachment;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.model.MsgAction;
import com.anchor.app.msg.model.MsgBoxRelation;
import com.anchor.app.msg.model.UserMsgView;
import com.anchor.app.msg.repository.ChannelMsgRelationRep;
import com.anchor.app.msg.repository.MessageRepository;
import com.anchor.app.msg.repository.MsgBoxRelationRepository;
import com.anchor.app.msg.repository.UserMsgViewRepository;
import com.anchor.app.msg.service.ChannelService;
import com.anchor.app.msg.service.ContentService;
import com.anchor.app.msg.service.MsgService;
import com.anchor.app.msg.vo.ChnlUserEnrolmentVo;
import com.anchor.app.msg.vo.CreateChannelVo;
import com.anchor.app.msg.vo.MessageActionVo;
import com.anchor.app.msg.vo.MessageResponse;
import com.anchor.app.msg.vo.MessageVo;
import com.anchor.app.msg.vo.MsgAggregateVo;
import com.anchor.app.msg.vo.UserChnlMsgVo;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.oauth.service.UserService;
import com.anchor.app.oauth.vo.AuthReq;
import com.anchor.app.util.HelperBean;
import com.anchor.app.vo.ErrorMsg;

@Service
public class MsgServiceImpl implements MsgService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HelperBean helper;

	@Autowired
	private ChannelService chnlService;
	
	@Autowired
	private MessageRepository msgRep;

	@Autowired
	private MsgBoxRelationRepository mbrRep;
	
	@Autowired
	private AuthService authServce;
	
	@Autowired
	private MessageDao msgDao;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MsgBoxRelationDao msgBoxRelDao;

	@Autowired
	private ChannelMsgRelationRep chnlMsgRep;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserMsgViewRepository umvRep;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ChannelParticipantDao chnlParticipantDao;

	@Autowired
	private EventLogService eventLogService;

	@Autowired
	private ActionLogUtil actionLogUtil;

	@Autowired
	private ActionLogService actionLogService;

	@Override
	public Message addMessage(MessageActionVo request) throws MsgServiceException {
		Date startT = new Date();
		Message response = null;
		try {
			// Requested Message Action
			//request.setActionType(MsgActionType.Add);
			
			// Validate addMessage Request
			msgActionValidation(request);
			
			if(!request.isValid())
			{
				throw new MsgServiceException("Invalid request");
			}
			
			
				// perform Authorization
				AuthReq authReq = new AuthReq(request.getUserID(), request.getUserID(), request.getChnlID(),  PermissionType.CnAdd,  request.getUserRoles());
				
			 boolean hasPermission = authServce.hasPersmission(authReq);
			
			 if(!hasPermission)
			 {
				 throw new MsgServiceException("Invalid Perssion");
			 }
			 
			if(hasPermission)
			{
				// Store Data to DB
				// Prepare CreateMessageDb Entities
				helper.prepareCreateMessage(request, startT);
				
				if(!request.getAttachments().isEmpty())
				{
				List<Attachment> attachment = 	contentService.saveMsgAttachments(request.getMessage().getId(), request.getUserID(), startT, request.getAttachments());
				
				request.getMessage().setAttachments(attachment);
				}
				
				// Store Data To DB
				
				saveMessageToDB(request);
				
				response = request.getMessage();
				
				// Add Event to Queu for Add New Message to Channel 
				eventService.AddMessageToChannelEvent(request.getUserID(), startT, request.getChnlID(), request.getMessage());
				
			}
			
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
		return response;
	}
	
	/**
	 * Save Message to DB
	 * @param request
	 * @throws MsgServiceException
	 */
	@Transactional
	private void saveMessageToDB(MessageActionVo req) throws MsgServiceException
	{
		try {
			if(null == req)
			{
				throw new MsgServiceException("Invalid create message request");
				
			}
			if( null != req.getMessage())
			{
				
				msgRep.save(req.getMessage());
			}
			if( null != req.getChnlMsgRelation())
			{
				chnlMsgRep.save(req.getChnlMsgRelation());
				
			}
			
			// Update Channel Participent for unread Message Count
			
			
			Collection uids = req.getChannelUsers().stream()
            .filter(c-> !c.getUserID().equalsIgnoreCase(req.getUserID()))
            .map(ChannelParticipant::getUserID)
            .distinct()
            .collect(Collectors.toList());
			
			if(uids.contains(req.getUserID()))
			{
				throw new MsgServiceException("Invalid System Processing"); 
			}
			
			chnlService.updateChnlParticipentReadStatus(req.getUserID(), uids, req.getChnlID(), ChannelActionType.AddUnread, 1, req.getToday());
			
			// Update Channel Participant First Unread Msg ID and Date if its null 
			chnlService.updateChnlParticipentFirstUnreadMsgIDAndDate(req.getUserID(), uids, req.getChnlID(),
					req.getChnlMsgRelation().getMsgId(), req.getChnlMsgRelation().getCreatedOn());
			
			// Update Channel for Latest MsgID and Date
			chnlService.updateChannelLatestMsgIdAndDate(req.getChnlID(), req.getMessage().getId(), req.getToday());
			
			/*
			 * Not required 
			if( !req.getMsgBoxRelations().isEmpty())
			{
				mbrRep.saveAll(req.getMsgBoxRelations());
			}
			
			*/
					
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
	}
	
	private void msgActionValidation(MessageActionVo request) throws MsgServiceException
	{
		request.setValid(true);
		
		// Structural validation
		messageActionStructuralValidation(request);
		
		if(request.isValid())
		{
			// DB Validation
			messageActionDBValidation(request);	
		}
		
		
	}
	
	private void messageActionStructuralValidation(MessageActionVo request) throws MsgServiceException
	{
		try {
			
			if( null == request.getUserID())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_User.name(), 
						ValidationErrorType.Invalid_Channel_User.getValue()));
			}
			
			if( null == request.getChnlID())
			{
					request.setValid(false);
					request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), 
							ValidationErrorType.Invalid_Request.getValue()));
					
			}
				
			
			if( null == request.getActionType())
			{
				request.setValid(false);
				request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Msg_Action.name(), 
						ValidationErrorType.Invalid_Msg_Action.getValue()));
			}
			
			 if(request.getActionType().equals(MsgActionType.Reply))
			{
				 
				 if( null == request.getParentMsgID())
				 {
					 request.setValid(false);
						request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Msg_ID.name(), 
								ValidationErrorType.Invalid_Msg_ID.getValue()));
				 }
				
			}
			
			if((request.getActionType().equals(MsgActionType.Reply))
				|| (request.getActionType().equals(MsgActionType.Add))	)
			{
				if( null == request.getType())
				{
					request.setValid(false);
					request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Msg_Type.name(), 
							ValidationErrorType.Invalid_Msg_Type.getValue()));
				}
				else  {
					
					if((request.getType().equals(MsgType.Text))
						|| (request.getType().equals(MsgType.Html)))
					{
						if( null == request.getBody())
						{
							request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Msg_Action.name(), 
									ValidationErrorType.Invalid_Msg_Action.getValue(), request.getActionType().name()));
							
						}
					}
				}	
			}
			
			 if(request.getActionType().equals(MsgActionType.MsgReaction))
				{
					 if( null == request.getMsgID())
					 {
						 request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Msg_ID.name(), 
									ValidationErrorType.Invalid_Msg_ID.getValue()));
					 }
					 
					 if( null == request.getMsgReaction())
					 {
						 request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Msg_Action.name(), 
									ValidationErrorType.Invalid_Msg_Action.getValue()));
					 }
					
				}
			
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
	}

	private void messageActionDBValidation(MessageActionVo request) throws MsgServiceException
	{
		try {
			// Verify Chnl is Valid
			
			if( null == request.getChnlID())
			{
				// First time User Send one to one Message to Another User Hence Channel needs to be created 
				
			}
			else {
			
				Channel chnl = chnlService.getChannelByID(request.getChnlID());
				
				if(null == chnl)
				{
					request.setValid(false);
					request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_ID.name(),"ChannelID", request.getChnlID()));
				}
				else {
				
					// No Other Participent Details required for View Message Action
					if(request.getActionType().equals(MsgActionType.View))
						{
						ChannelParticipant userRec = chnlService.getChannelParticipantForChannelID(request.getUserID(),request.getChnlID(), request.getToday());
					
						if(null == userRec)
						{
							request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_User.name(), "User", request.getUserID()));
						}
						else {
							request.getUserRoles().addAll(userRec.getUserRoles());
						}
					
						
						}
					else if((request.getActionType().equals(MsgActionType.Reply))
							|| (request.getActionType().equals(MsgActionType.Add))
							|| (request.getActionType().equals(MsgActionType.Update_UnRead))
							|| (request.getActionType().equals(MsgActionType.MsgReaction))
							)
					{
						// Fetch Chnl Participent 
						List<ChannelParticipant> chnlParticipants = chnlService.getChannelParticipantForChannelID(request.getChnlID(), request.getToday());
						
						// Verify Sender is part of chnl Participants
						ChannelParticipant senderRec = helper.getChnlParticipentRecord(request.getUserID(), chnlParticipants);
						
						if(null == senderRec)
						{
							request.setValid(false);
							request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Channel_User.name(), "User", request.getUserID()));
						}
						else {
						
							request.setChannelUsers(chnlParticipants);
							request.getUserRoles().addAll(senderRec.getUserRoles());
						}
						
						
					}
					
					
				}
			}
			
			
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
	}

	@Override
	public Message replyMessage(MessageActionVo request) throws MsgServiceException {
		
		Date startT = new Date();
		Message response = null;
		try {
			// Requested Message Action
			request.setActionType(MsgActionType.Reply);
			
			// Validate addMessage Request
			msgActionValidation(request);
			
			if(!request.isValid())
			{
				throw new MsgServiceException("Invalid request");
			}
			
			// perform Authorization
			
			AuthReq authReq = new AuthReq(request.getUserID(),request.getUserID(), request.getChnlID(),  PermissionType.CnAdd,  request.getUserRoles());
			
			boolean hasPermission = authServce.hasPersmission(authReq);
			
			if(hasPermission)
			{
				// Store Data to DB
				// Prepare CreateMessageDb Entities
				helper.prepareCreateMessage(request, startT);
				
				// Store Data To DB
				
				saveMessageToDB(request);
				
				response = request.getMessage();
			}
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
		return response;
	}

	@Override
	@Deprecated
	public Page<MessageVo> getChannelMessageForUser(MessageActionVo request, Pageable pageable)
			throws MsgServiceException {
		
		Page<MessageVo> pageResposne = null;
		try {
		
			// Requested Message Action
			request.setActionType(MsgActionType.View);
			
			// Validate addMessage Request
			msgActionValidation(request);
			
			if(!request.isValid())
			{
				throw new MsgServiceException("Invalid request");
			}
			
			// perform Authorization
			
			AuthReq authReq = new AuthReq(request.getUserID(),request.getUserID(), request.getChnlID(),  PermissionType.CnView,  request.getUserRoles());
			
			boolean hasPermission = authServce.hasPersmission(authReq);
			if(hasPermission)
			{
				// get All the Message Belongs to User for Respective Channel 
				
				//Page<MsgAggregateVo> aggResponse = messageDao.getMessagesForUserForChannel(request.getChnlID(), request.getUserID(), pageable);
				//List<MsgAggregateVo> aggResponse = mbrRep.getMsgBoxRelationByUserIDAndChnlID(request.getChnlID(), request.getUserID(), pageable);
				//logger.info("Aggregated Results :"+aggResponse);
				Page<MsgBoxRelation> msgBoxPage = mbrRep.getMsgBoxRelationForUser(request.getUserID(), request.getChnlID(), pageable);
				
				if(msgBoxPage.hasContent())
				{
					List<MsgBoxRelation> msgBoxRelList = msgBoxPage.getContent();
					
					
					Set<String> msgIdSet = msgBoxRelList.stream()
							.map(MsgBoxRelation::getMsgID)
							.collect(Collectors.toSet());
			                
					
					Set<String> parentMsgIDSet = msgBoxRelList.stream()
							.filter(mbr -> Objects.nonNull(mbr.getParentMsgID()))
							.map(MsgBoxRelation::getMsgID)
							.collect(Collectors.toSet());
					
					Set<String> allMsgSet = new HashSet<>();
					allMsgSet.addAll(msgIdSet);
					allMsgSet.addAll(parentMsgIDSet);
				
					List<Message> msgList = msgRep.findAllByIdsIn(allMsgSet);
					
					Map<String, Message> msgMap = msgList.stream().collect(
			                Collectors.toMap(Message::getId, msg -> msg));
			
					List<MessageVo> mvoList = new ArrayList<>();
					
					// Prepare MessageVo List
					for (MsgBoxRelation mbr : msgBoxRelList)
					{
						MessageVo mvo =  prepareMessageVo(mbr, msgMap);
						mvoList.add(mvo);
					}
			
					   LongSupplier
			            total
			            = () -> (int)(msgBoxPage.getTotalElements());
			         
					pageResposne  = PageableExecutionUtils.getPage(
							mvoList,
		    		        pageable, total);
		        
				}
			}
	
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MsgServiceException(e.getMessage(), e);
		}
		
		return pageResposne;
	}
	
	
	private MessageVo prepareMessageVo(MsgBoxRelation mbr, Map<String, Message> msgMap) throws ConversionException
	{
		MessageVo mvo = new MessageVo();
		
		try {
			Message msg = msgMap.get(mbr.getMsgID());
			if( null == msg)
			{
				throw new ConversionException("Msg not found for MsgBoxRelationID:"+mbr.getId());	
			}
			
			mvo.setMsgBoxRelationID(mbr.getId());
			mvo.setMsgID(mbr.getMsgID());
			mvo.setType(msg.getType());
			mvo.setBody(msg.getBody());
			mvo.getAttachments().addAll(msg.getAttachments());
			
			// setup Parents Attachement ID Details
			if(null != mbr.getParentAttacmentID())
			{
				mvo.setParentAttacmentID(mbr.getParentAttacmentID());
			}
			
			mvo.setChnlID(mbr.getChnlID());
			mvo.setBoxType(mbr.getBoxType());
			mvo.setBoxID(mbr.getBoxID());
			mvo.setUserID(mbr.getUserID());
			mvo.setMsgActionType(mbr.getMsgActionType());
			mvo.setVisible(mbr.isVisible());
			mvo.setDelete(mbr.isDelete());
			mvo.setRead(mbr.isRead());
			mvo.setSenderID(mbr.getSenderID());
			
			User sender = userService.getUserForUserID(mbr.getSenderID());
			
			if( null == sender)
			{
				throw new ConversionException("Sender User not found for MsgBoxRelationID:"+mbr.getId());
			}
			mvo.setSenderFirstName(sender.getFirstName());
			mvo.setSenderLastName(sender.getLastName());
			mvo.setMsgDate(mbr.getCreatedOn());
			
			// setup Parent Message Details
			if( null != mbr.getParentMsgID())
			{
				Message parentMsg = msgMap.get(mbr.getParentMsgID());
				if( null == parentMsg)
				{
					throw new ConversionException("Parrent Msg not found for MsgBoxRelationID:"+mbr.getId());	
				}
				mvo.setParentMsgID(mbr.getParentMsgID());
				mvo.setParentMessage(parentMsg);
				
			}	
		}
		catch(Exception e)
		{
			throw new ConversionException(e.getMessage(), e);
		}
		
		
		return mvo;
	}

	@Override
	@Deprecated
	public List<MessageVo> getRecentMessageOfChannelForUser(String userID, Collection channelIds)
			throws MsgServiceException {

		List<MessageVo> msgVos = new ArrayList<>();
		try {
				
			
			// Get Recept Msg for Rach of Channel for Respective User
			
			List<MsgAggregateVo> msgAggVos = msgBoxRelDao.getRecentMessageForUserForChannels(userID, channelIds);
		
			Map<String, Message> msgMap = new HashMap<>();
			List<MsgBoxRelation> msgBoxRelList = new ArrayList<>();
			
			if(!msgAggVos.isEmpty())
			{
			
				for (MsgAggregateVo msgAggvo : msgAggVos)
				{
					// Populate MsgBoxRelation
					if( null != msgAggvo.getMsgBoxRelation())
					{
						msgBoxRelList.add(msgAggvo.getMsgBoxRelation());
					}
					
					// Populate Messages 
					if(!msgAggvo.getMsgList().isEmpty())
					{
						Message msg = msgAggvo.getMsgList().get(0);
						msgMap.put(msg.getId(), msg);
					}
					
					if(!msgAggvo.getParentMsgList().isEmpty())
					{
						Message msg = msgAggvo.getParentMsgList().get(0);
						msgMap.put(msg.getId(), msg);
					}
				}
		
			}
		
				
			
				
				// Prepare MessageVo List
				for (MsgBoxRelation mbr : msgBoxRelList)
				{
					MessageVo mvo =  prepareMessageVo(mbr, msgMap);
					msgVos.add(mvo);
				}

				
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
		return msgVos;
	}

	@Override
	public List<Message> getMessagesByMsgIds(Collection msgIds) throws MsgServiceException {
		List<Message> msgs = null;
		try {
			
			msgs = msgRep.findAllByIdsIn(msgIds);
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
		return msgs;
	}

	@Override
	public Page<UserMsgView> getMessageForChannel(String userID,String chnlID, Pageable page) throws MsgServiceException {
		Page<UserMsgView>  pageResposne = null;
		
		try {
		
			 pageResposne = umvRep.getUserMsgForChannel(userID, chnlID, page);
			
			if(pageResposne.isEmpty())
			{
				logger.info("Return Page is Empty");
			}
			else {
				pageResposne.get().forEach(mv -> {
					if( null != mv.getMessage())
					{
					
					mv.getMessage().setCreatedBy(mv.getCreatedBy());
					mv.getMessage().setCreatedOn(mv.getCreatedOn());
					mv.getMessage().setModifiedBy(mv.getModifiedBy());
					mv.getMessage().setModifiedOn(mv.getModifiedOn());
					}
				});
			}

		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
		return pageResposne;
	}

	@Override
	public Message getMessagesByMsgId(String msgId) throws MsgServiceException {
		
		Message msg = null;
		try {
			
			Optional<Message> msgOpt  = msgRep.findById(msgId);
			if(msgOpt.isPresent())
			{
				msg = msgOpt.get();
			}
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
		return msg;
	}

	@Override
	public Attachment getAttachmentById(String msgId, String contentId) throws MsgServiceException 
	{
		Attachment result = null;
		
		try {
			
			Message msg = getMessagesByMsgId(msgId);
			if( null != msg)
			{
				result = msg.getAttachments().stream()
						  .filter(atch -> contentId.equals(atch.getContentID()))
						  .findAny()
						  .orElse(null);
			}
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
		return result;
	}

	@Override
	public void performMsgAction(MessageActionVo request) throws MsgServiceException 
	{
		
		Date startT = new Date();
		try {
			// Requested Message Action
			//request.setActionType(MsgActionType.Add);
			
			// Validate addMessage Request
			msgActionValidation(request);
			
			if(!request.isValid())
			{
				throw new MsgServiceException("Invalid request");
			}
			
			
				// perform Authorization
				AuthReq authReq = new AuthReq(request.getUserID(), request.getUserID(), request.getChnlID(),  PermissionType.CnEdit,  request.getUserRoles());
				
			 boolean hasPermission = authServce.hasPersmission(authReq);
			
			 if(!hasPermission)
			 {
				 throw new MsgServiceException("Invalid Perssion");
			 }
			 
			 if(request.getActionType().equals(MsgActionType.Update_UnRead))
			 {
				 update_UnReadMsgCount(request.getChnlID(), request.getUserID(), startT);
			 }
			 else if(request.getActionType().equals(MsgActionType.MsgReaction))
			 {
				 updateMsgReactionByUser(request.getMsgID(), request.getChnlID(), request.getUserID(), startT,request.getMsgReaction()); 
			 }
		
		}	
		catch(Exception e )
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
	}

	
	private void update_UnReadMsgCount(String chnlID, String userID, Date tr_date) throws MsgServiceException
	{
		try {
			chnlParticipantDao.removeUnreadStatuOfChnlParticipent(userID, userID, chnlID, tr_date);
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
	}
	
	private void updateMsgReactionByUser(String msgID, String chnlID, String userID, Date tr_date, String msgReaction) throws MsgServiceException
	{
		try {
			msgDao.addUpdateMsgReactionByUser(msgID, chnlID, userID, tr_date, msgReaction);
			
			// Send Event to EventLogService
			//eventLogService.createMsgAddReactionEvent(userID, userID, chnlID, msgID, msgReaction);
			ActionLogReq actionLogReq =  actionLogUtil.createAddMsgReactionReqLog(userID, null,chnlID, msgID, msgReaction);
			actionLogService.processActionLogRequest(actionLogReq);
			
		}
		catch(Exception e)
		{
			throw new MsgServiceException(e.getMessage(), e);
		}
		
	}

}
