package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;

import com.anchor.app.msg.model.EventNotification;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserNotificationResp implements Serializable {
	
	private String reqUserId;
	@JsonIgnore
	private Collection searchUserIds = new HashSet<String>();
	
	@JsonIgnore
	private Collection searchMsgChnlIds = new HashSet<String>();
	
	Page<EventNotification> result;
	
	private List<SearchUserVo> users = new ArrayList<>();
	private List<SearchChannelVo> chnls = new ArrayList<>();
	
	public String getReqUserId() {
		return reqUserId;
	}
	public void setReqUserId(String reqUserId) {
		this.reqUserId = reqUserId;
	}
	
	public Page<EventNotification> getResult() {
		return result;
	}
	public void setResult(Page<EventNotification> result) {
		this.result = result;
	}
	public Collection getSearchUserIds() {
		return searchUserIds;
	}
	public Collection getSearchMsgChnlIds() {
		return searchMsgChnlIds;
	}
	public List<SearchUserVo> getUsers() {
		return users;
	}
	public List<SearchChannelVo> getChnls() {
		return chnls;
	}
	
	

}
