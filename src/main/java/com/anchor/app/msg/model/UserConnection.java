package com.anchor.app.msg.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.UserConnectionStatusType;

@Document(collection= "userConnection")
public class UserConnection {
	
	@Id
	private String id;
	
	// Common Key between Src and Trg User Ids 
	private String connectionId; 
	private String srcUserId;
	private String trgUserId;
	private UserConnectionStatusType status;
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
	
	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	public String getSrcUserId() {
		return srcUserId;
	}
	public void setSrcUserId(String srcUserId) {
		this.srcUserId = srcUserId;
	}
	public String getTrgUserId() {
		return trgUserId;
	}
	public void setTrgUserId(String trgUserId) {
		this.trgUserId = trgUserId;
	}
	public UserConnectionStatusType getStatus() {
		return status;
	}
	public void setStatus(UserConnectionStatusType status) {
		this.status = status;
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
