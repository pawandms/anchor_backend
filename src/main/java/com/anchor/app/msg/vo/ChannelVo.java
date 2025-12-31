package com.anchor.app.msg.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.oauth.model.User;

public class ChannelVo extends Channel {
	
	private List<UserRoleType> userRoles = new ArrayList<>();
	private Date validFrom;
	private Date validTo;
	
	private MessageVo latestMsg;
	
	private List<User> chnlUsers = new ArrayList<>();
	
	public List<UserRoleType> getUserRoles() {
		return userRoles;
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

	public List<User> getChnlUsers() {
		return chnlUsers;
	}

	public MessageVo getLatestMsg() {
		return latestMsg;
	}

	public void setLatestMsg(MessageVo latestMsg) {
		this.latestMsg = latestMsg;
	}

	
	
}
