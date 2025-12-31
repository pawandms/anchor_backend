package com.anchor.app.msg.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.anchor.app.msg.enums.ActionStatusType;
import com.anchor.app.msg.enums.EventActionType;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.EventType;
import com.anchor.app.msg.enums.UserActionStatusType;
import com.anchor.app.msg.enums.UserActionType;
import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection= "eventNotification")
public class EventNotification {
	
	@Id
	private String id;
	private String srcUserID;
	private String trgUserID;
	private EventType type;
	private EventEntityType entity;
	private String entityId;
	private String trgChnlID;
	private String trgMsgID;
	private UserActionType srcAction;
	private String srcMsg;
	
	private UserActionType trgAction;
	private UserActionStatusType trgActionStatus;
	private String trgMsg;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date actionDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date responseDate;
	
	private boolean processFlag;
	
	private boolean validFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSrcUserID() {
		return srcUserID;
	}

	public void setSrcUserID(String srcUserID) {
		this.srcUserID = srcUserID;
	}

	public String getTrgUserID() {
		return trgUserID;
	}

	public void setTrgUserID(String trgUserID) {
		this.trgUserID = trgUserID;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public EventEntityType getEntity() {
		return entity;
	}

	public void setEntity(EventEntityType entity) {
		this.entity = entity;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}


	public String getSrcMsg() {
		return srcMsg;
	}

	public void setSrcMsg(String srcMsg) {
		this.srcMsg = srcMsg;
	}

	
	public UserActionType getSrcAction() {
		return srcAction;
	}

	public void setSrcAction(UserActionType srcAction) {
		this.srcAction = srcAction;
	}

	public UserActionType getTrgAction() {
		return trgAction;
	}

	public void setTrgAction(UserActionType trgAction) {
		this.trgAction = trgAction;
	}

	public UserActionStatusType getTrgActionStatus() {
		return trgActionStatus;
	}

	public void setTrgActionStatus(UserActionStatusType trgActionStatus) {
		this.trgActionStatus = trgActionStatus;
	}

	public String getTrgMsg() {
		return trgMsg;
	}

	public void setTrgMsg(String trgMsg) {
		this.trgMsg = trgMsg;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public boolean isProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(boolean processFlag) {
		this.processFlag = processFlag;
	}

	public boolean isValidFlag() {
		return validFlag;
	}

	public void setValidFlag(boolean validFlag) {
		this.validFlag = validFlag;
	}

	public String getTrgChnlID() {
		return trgChnlID;
	}

	public void setTrgChnlID(String trgChnlID) {
		this.trgChnlID = trgChnlID;
	}

	public String getTrgMsgID() {
		return trgMsgID;
	}

	public void setTrgMsgID(String trgMsgID) {
		this.trgMsgID = trgMsgID;
	}
}
