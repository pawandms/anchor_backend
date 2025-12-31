package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anchor.app.msg.enums.EventActionType;
import com.anchor.app.msg.enums.UserActionStatusType;
import com.anchor.app.msg.enums.UserActionType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.enums.VisibilityType;
import com.anchor.app.msg.model.EventNotification;
import com.anchor.app.oauth.model.User;
import com.anchor.app.vo.BaseVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserActionVo extends BaseVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String reqUserID;
	private String srcUserID;
	private String trgUserID;
	
	@JsonIgnore
	private User srcUser;
	
	@JsonIgnore
	private User trgUser;

	private UserActionType actionType;
	private EventActionType actionRespType;
	private String actionMsg;
	@JsonIgnore
	private List<UserRoleType> userRoles = new ArrayList<>();
	private Date today = new Date();

	// For Change UserProfile Type
	private VisibilityType profileType;
	
	// For User Action Response for Add Friend/ Add Chnl etc
	private String userConnectionId;
	private String actionId;
	private UserActionStatusType actionStatus;
	
	// For User Search Action
	private String searchKey;
	private int searchPage;
	private int searchSize;
	
	@JsonIgnore
	private EventNotification event;
	
	public String getReqUserID() {
		return reqUserID;
	}
	public void setReqUserID(String reqUserID) {
		this.reqUserID = reqUserID;
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
	
	public User getSrcUser() {
		return srcUser;
	}
	public void setSrcUser(User srcUser) {
		this.srcUser = srcUser;
	}
	public User getTrgUser() {
		return trgUser;
	}
	public void setTrgUser(User trgUser) {
		this.trgUser = trgUser;
	}
	public UserActionType getActionType() {
		return actionType;
	}
	public void setActionType(UserActionType actionType) {
		this.actionType = actionType;
	}
	public EventActionType getActionRespType() {
		return actionRespType;
	}
	public void setActionRespType(EventActionType actionRespType) {
		this.actionRespType = actionRespType;
	}
	public List<UserRoleType> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRoleType> userRoles) {
		this.userRoles = userRoles;
	}
	public Date getToday() {
		return today;
	}
	public void setToday(Date today) {
		this.today = today;
	}
	public VisibilityType getProfileType() {
		return profileType;
	}
	public void setProfileType(VisibilityType profileType) {
		this.profileType = profileType;
	}
	public String getActionMsg() {
		return actionMsg;
	}
	public void setActionMsg(String actionMsg) {
		this.actionMsg = actionMsg;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public UserActionStatusType getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(UserActionStatusType actionStatus) {
		this.actionStatus = actionStatus;
	}
	public EventNotification getEvent() {
		return event;
	}
	public void setEvent(EventNotification event) {
		this.event = event;
	}
	public String getUserConnectionId() {
		return userConnectionId;
	}
	public void setUserConnectionId(String userConnectionId) {
		this.userConnectionId = userConnectionId;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public int getSearchPage() {
		return searchPage;
	}
	public void setSearchPage(int searchPage) {
		this.searchPage = searchPage;
	}
	public int getSearchSize() {
		return searchSize;
	}
	public void setSearchSize(int searchSize) {
		this.searchSize = searchSize;
	}

	

}
