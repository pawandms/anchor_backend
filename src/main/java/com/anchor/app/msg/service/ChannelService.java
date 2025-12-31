package com.anchor.app.msg.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.anchor.app.msg.enums.ChannelActionType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.exceptions.ChannelServiceException;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.vo.ChannelParticipantResponse;
import com.anchor.app.msg.vo.ChannelResponse;
import com.anchor.app.msg.vo.ChnlUserActionVo;
import com.anchor.app.msg.vo.CreateChannelVo;

public interface ChannelService {
	

	public void createChannel(Channel chnl)throws ChannelServiceException;
	
	/**
	 * Create Channel
	 * @param request
	 * @return
	 * @throws ChannelServiceException
	 */
	public Channel createChannel(CreateChannelVo request) throws ChannelServiceException;
	
	/**
	 * Get Channel By ID
	 * @param chnlID
	 * @return
	 * @throws ChannelServiceException
	 */
	public Channel getChannelByID(String chnlID)throws ChannelServiceException;
	
	public List<Channel> getChannelByIds(Collection chnlIds)throws ChannelServiceException;
	
	public List<ChannelParticipant> getChannelParticipantForChannelID(String chnlID, Date today) throws ChannelServiceException;
	
	public ChannelParticipant getChannelParticipantForChannelID(String userID, String chnlID, Date today) throws ChannelServiceException;
	
	/**
	 * Get Channels belongs to user 
	 * @param userID
	 * @param type
	 * @return
	 * @throws ChannelServiceException
	 */
	public ChannelResponse getUserChannels(String userID, ChannelType type) throws ChannelServiceException;
	
	/**
	 * Add User to Channel
	 * @return
	 * @throws ChannelServiceException
	 */
	public void addUserToChannel(ChnlUserActionVo request)throws ChannelServiceException;
	
	public void removeUserFromChannel(ChnlUserActionVo request)throws ChannelServiceException;
	
	
	/**
	 * Update Channel Participant for New Message Arrival as increase unreadCount
	 * @param reqUserID
	 * @param userIDs
	 * @param channelID
	 * @param action
	 * @param count
	 * @param validToDate
	 */
	public void updateChnlParticipentReadStatus(String reqUserID, Collection userIDs, String channelID, ChannelActionType action, 
			int count, Date validToDate) throws ChannelServiceException;
	
	/**
	 * Update Channel Participant if Unread MsgID and Date is null as a First Unread Msg and Date 
	 * @param reqUserID
	 * @param userIDs
	 * @param channelID
	 * @param msgID
	 * @param msgDate
	 * @throws ChannelServiceException
	 */
	public void updateChnlParticipentFirstUnreadMsgIDAndDate(String reqUserID, Collection userIDs, String channelID, 
			String msgID, Date msgDate) throws ChannelServiceException;
	
	/**
	 * Update Latest MsgID & date for Chnl
	 * @param channelID
	 * @param msgID
	 * @param date
	 * @throws ChannelServiceException
	 */
	public void updateChannelLatestMsgIdAndDate(String channelID, String msgID, Date date)throws ChannelServiceException;
	
	public void getChannelParticipantForChannel(ChannelParticipantResponse resp) throws ChannelServiceException;	
	
	public List<ChannelParticipant> getUserEnrolmentDetailsForChannel(String chnlID, String userID) throws ChannelServiceException;
	
	public void saveChannelParticipents(List<ChannelParticipant> cpList) throws ChannelServiceException;
}
