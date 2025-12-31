package com.anchor.app.event.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.anchor.app.msg.enums.UserActionStatusType;
import com.anchor.app.msg.enums.UserActionType;
import com.anchor.app.msg.enums.VisibilityType;

public class Meta implements Serializable {
	
	private String userId;
	private String chnlId;
	private String msgID;
	private VisibilityType visibility;
	private String name;
	private String lastName;
	private String description;
	private List<String> tag;
	private EventLocation loc;
	private String value;
	private UserActionStatusType  actionStatus;
	private String srcActionID;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getChnlId() {
		return chnlId;
	}
	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}
	
	public VisibilityType getVisibility() {
		return visibility;
	}
	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
	public EventLocation getLoc() {
		return loc;
	}
	public void setLoc(EventLocation loc) {
		this.loc = loc;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMsgID() {
		return msgID;
	}
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public UserActionStatusType getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(UserActionStatusType actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getSrcActionID() {
		return srcActionID;
	}

	public void setSrcActionID(String srcActionID) {
		this.srcActionID = srcActionID;
	}
}
