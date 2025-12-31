package com.anchor.app.msg.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "channelMsgRelation")
public class ChannelMsgRelation implements Serializable {
	
	@Id
	private String id;
	
	private String channelID;
	
	// Parent Msg ID in case or Reply Msg
	private String parentMsgID;
	// Used if Reply Message on any one of attachment
	private String parentAttacmentID;
	private String msgId;
	private String senderId;
	private ChnlMsgAttribute attribute;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
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
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public ChnlMsgAttribute getAttribute() {
		return attribute;
	}
	public void setAttribute(ChnlMsgAttribute attribute) {
		this.attribute = attribute;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	

}
