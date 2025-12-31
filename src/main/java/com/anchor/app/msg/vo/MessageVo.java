package com.anchor.app.msg.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.MsgActionType;
import com.anchor.app.msg.enums.MsgBoxType;
import com.anchor.app.msg.enums.MsgType;
import com.anchor.app.msg.model.Attachment;
import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.model.MsgBoxRelation;

public class MessageVo {

	private String userID;
	
	
	private String msgBoxRelationID;
	private String chnlID;
	
	private MsgBoxRelation msgInfo;
	private Message message;
	
	// Parent Message Vo details if its reply to Parent Message
	private Message parentMessage;
		
	
	// From Message Entity
	private String msgID;
	private MsgType type;
	private String body;
	private List<Attachment> attachments = new ArrayList<>();
	
	// From MsgRelation Entity
	private String parentMsgID;
	private String parentAttacmentID;
	
	
	private ChannelType channelType = ChannelType.Messaging ;
	private MsgBoxType boxType;
	private String boxID;
	private MsgActionType msgActionType;
	private boolean visible;
	private boolean delete;
	private boolean read;
	private String senderID;
	private String senderFirstName;
	private String senderLastName;
	private Date msgDate;
	
	public String getMsgBoxRelationID() {
		return msgBoxRelationID;
	}
	public void setMsgBoxRelationID(String msgBoxRelationID) {
		this.msgBoxRelationID = msgBoxRelationID;
	}
	
	
	
	public String getMsgID() {
		return msgID;
	}
	public void setMsgID(String msgID) {
		this.msgID = msgID;
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
	public List<Attachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	public String getParentMsgID() {
		return parentMsgID;
	}
	public void setParentMsgID(String parentMsgID) {
		this.parentMsgID = parentMsgID;
	}
	public String getParentAttacmentID() {
		return parentAttacmentID;
	}
	public void setParentAttacmentID(String parentAttacmentID) {
		this.parentAttacmentID = parentAttacmentID;
	}

	
	public Message getParentMessage() {
		return parentMessage;
	}
	public void setParentMessage(Message parentMessage) {
		this.parentMessage = parentMessage;
	}
	public ChannelType getChannelType() {
		return channelType;
	}
	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}
	public String getChnlID() {
		return chnlID;
	}
	public void setChnlID(String chnlID) {
		this.chnlID = chnlID;
	}
	public MsgBoxType getBoxType() {
		return boxType;
	}
	public void setBoxType(MsgBoxType boxType) {
		this.boxType = boxType;
	}
	public String getBoxID() {
		return boxID;
	}
	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public MsgActionType getMsgActionType() {
		return msgActionType;
	}
	public void setMsgActionType(MsgActionType msgActionType) {
		this.msgActionType = msgActionType;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public String getSenderID() {
		return senderID;
	}
	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}
	public String getSenderFirstName() {
		return senderFirstName;
	}
	public void setSenderFirstName(String senderFirstName) {
		this.senderFirstName = senderFirstName;
	}
	public String getSenderLastName() {
		return senderLastName;
	}
	public void setSenderLastName(String senderLastName) {
		this.senderLastName = senderLastName;
	}
	public Date getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(Date msgDate) {
		this.msgDate = msgDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((msgBoxRelationID == null) ? 0 : msgBoxRelationID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageVo other = (MessageVo) obj;
		if (msgBoxRelationID == null) {
			if (other.msgBoxRelationID != null)
				return false;
		} else if (!msgBoxRelationID.equals(other.msgBoxRelationID))
			return false;
		return true;
	}

	
	
}
