package com.anchor.app.msg.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.anchor.app.msg.enums.ActionStatusType;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.EventType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Not in Use 
 */
@Deprecated
@Document(collection= "appEvent")
public class AppEvent {

	@Id
	private String id;
	private EventType entityType;
	private String reqUserId;
	private EventEntityType reqEntityType;
	private String reqEntityId;

	private String reqComment;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date reqDate;

	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date actionDate;
	
	private ActionStatusType actionStatus;
	
	private String actionComment;
	
	private String createdBy;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date createdOn;
	
    private String modifiedBy;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date modifiedOn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EventType getEntityType() {
		return entityType;
	}

	public void setEntityType(EventType entityType) {
		this.entityType = entityType;
	}

	public String getReqUserId() {
		return reqUserId;
	}

	public void setReqUserId(String reqUserId) {
		this.reqUserId = reqUserId;
	}

	public EventEntityType getReqEntityType() {
		return reqEntityType;
	}

	public void setReqEntityType(EventEntityType reqEntityType) {
		this.reqEntityType = reqEntityType;
	}

	public String getReqEntityId() {
		return reqEntityId;
	}

	public void setReqEntityId(String reqEntityId) {
		this.reqEntityId = reqEntityId;
	}

	public String getReqComment() {
		return reqComment;
	}

	public void setReqComment(String reqComment) {
		this.reqComment = reqComment;
	}

	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public ActionStatusType getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(ActionStatusType actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getActionComment() {
		return actionComment;
	}

	public void setActionComment(String actionComment) {
		this.actionComment = actionComment;
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
