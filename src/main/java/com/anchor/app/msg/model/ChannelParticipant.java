package com.anchor.app.msg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.UserRoleType;
import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection= "channelParticipant")
public class ChannelParticipant implements Serializable {

	private static final long serialVersionUID = 638469888633895262L;

	@Id
	private String id;

	private String channelID;
	private ChannelType channelType;
	private String userID;
	
	private boolean active;

	private List<UserRoleType> userRoles = new ArrayList<>();

	// Used to Show Total Unread Message for Respective channel for respective UserID
	private int unReadCount;
	private Date unReadDate;
	private String unReadMsgID;	
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date validFrom;
	
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date validTo;
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

	public ChannelType getChannelType() {
		return channelType;
	}

	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public List<UserRoleType> getUserRoles() {
		return userRoles;
	}
	
	public int getUnReadCount() {
		return unReadCount;
	}

	public void setUnReadCount(int unReadCount) {
		this.unReadCount = unReadCount;
	}

	

	public Date getUnReadDate() {
		return unReadDate;
	}

	public void setUnReadDate(Date unReadDate) {
		this.unReadDate = unReadDate;
	}

	public String getUnReadMsgID() {
		return unReadMsgID;
	}

	public void setUnReadMsgID(String unReadMsgID) {
		this.unReadMsgID = unReadMsgID;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
