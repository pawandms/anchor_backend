package com.anchor.app.msg.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.MsgActionType;
import com.anchor.app.msg.enums.MsgBoxType;

@Document(collection= "MsgBoxRelation")
public class MsgBoxRelation {

	@Id
	private String id;
	
	private String msgID;

	// Parent Msg ID in case or Reply Msg
	private String parentMsgID;
	// Used if Reply Message on any one of attachment
	private String parentAttacmentID;
	private String chnlID;
	private MsgBoxType boxType;
	private String boxID;
	private String userID;
	private MsgActionType msgActionType;
	private boolean visible;
	private boolean delete;
	private boolean read;
	private String senderID;
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
	public String getMsgID() {
		return msgID;
	}
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
	public String getParentMsgID() {
		return parentMsgID;
	}
	public void setParentMsgID(String parentMsgID) {
		this.parentMsgID = parentMsgID;
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
	public String getParentAttacmentID() {
		return parentAttacmentID;
	}
	public void setParentAttacmentID(String parentAttacmentID) {
		this.parentAttacmentID = parentAttacmentID;
	}

	
	
}
