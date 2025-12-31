package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.msg.enums.MsgActionType;
import com.anchor.app.msg.enums.MsgType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.model.ChannelMsgRelation;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.model.Message;
import com.anchor.app.vo.BaseVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageActionVo extends BaseVo implements Serializable {

	private String chnlID;
	private MsgType type;
	private String body;
	private List<MultipartFile > attachments = new ArrayList<>();
	private String userID;
	
	// Used for Perform Msg Actin i.e. Read , Like, Dislike
	private String MsgID;
	private String parentMsgID;

	// Used if Reply message on one of attachment
	private String replyattachmentID;
	private MsgActionType actionType;
	private boolean deleteForAll;
	
	@JsonIgnore
	private Message message;
	
	private List<ChannelParticipant> channelUsers = new ArrayList<>();
	private ChannelMsgRelation chnlMsgRelation;
	
	
//	@JsonIgnore
	//private List<ChannelParticipant> chnlParticipants = new ArrayList<>();
	
	@JsonIgnore
	private List<UserRoleType> userRoles = new ArrayList<>();
	
	private Date today = new Date();
	
	// Reaction made by User on respective Msg
	private String msgReaction;
	
	public String getChnlID() {
		return chnlID;
	}
	public void setChnlID(String chnlID) {
		this.chnlID = chnlID;
	}
	public MsgType getType() {
		return type;
	}
	public void setType(MsgType type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public List<MultipartFile> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<MultipartFile> attachments) {
		this.attachments = attachments;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getMsgID() {
		return MsgID;
	}
	public void setMsgID(String msgID) {
		MsgID = msgID;
	}
	public String getParentMsgID() {
		return parentMsgID;
	}
	public void setParentMsgID(String parentMsgID) {
		this.parentMsgID = parentMsgID;
	}
	
	public String getReplyattachmentID() {
		return replyattachmentID;
	}
	public void setReplyattachmentID(String replyattachmentID) {
		this.replyattachmentID = replyattachmentID;
	}
	public MsgActionType getActionType() {
		return actionType;
	}
	public void setActionType(MsgActionType actionType) {
		this.actionType = actionType;
	}
	public boolean isDeleteForAll() {
		return deleteForAll;
	}
	public void setDeleteForAll(boolean deleteForAll) {
		this.deleteForAll = deleteForAll;
	}
	
	/*
	public List<ChannelParticipant> getChnlParticipants() {
		return chnlParticipants;
	}
	
	*/
	
	public List<UserRoleType> getUserRoles() {
		return userRoles;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
	public Date getToday() {
		return today;
	}
	public void setToday(Date today) {
		this.today = today;
	}
	public ChannelMsgRelation getChnlMsgRelation() {
		return chnlMsgRelation;
	}
	public void setChnlMsgRelation(ChannelMsgRelation chnlMsgRelation) {
		this.chnlMsgRelation = chnlMsgRelation;
	}
	public List<ChannelParticipant> getChannelUsers() {
		return channelUsers;
	}
	public void setChannelUsers(List<ChannelParticipant> channelUsers) {
		this.channelUsers = channelUsers;
	}
	public String getMsgReaction() {
		return msgReaction;
	}
	public void setMsgReaction(String msgReaction) {
		this.msgReaction = msgReaction;
	}


	

}
