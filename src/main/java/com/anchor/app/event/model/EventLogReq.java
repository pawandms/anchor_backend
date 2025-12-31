package com.anchor.app.event.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.anchor.app.msg.enums.UserActionType;

public class EventLogReq implements Serializable {
	
	private String reqUserID;
	private UserActionType actionType;
	
	private List<EventLog> logs = new ArrayList<>();

	public String getReqUserID() {
		return reqUserID;
	}
	public void setReqUserID(String reqUserID) {
		this.reqUserID = reqUserID;
	}
	
	public UserActionType getActionType() {
		return actionType;
	}
	public void setActionType(UserActionType actionType) {
		this.actionType = actionType;
	}
	public List<EventLog> getLogs() {
		return logs;
	}
	public void setLogs(List<EventLog> logs) {
		this.logs = logs;
	}


}
