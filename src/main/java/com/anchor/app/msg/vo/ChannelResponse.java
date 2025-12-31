package com.anchor.app.msg.vo;

import java.util.ArrayList;
import java.util.List;

import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.vo.BaseVo;

public class ChannelResponse extends BaseVo {

	// Response for get Channel List for User
	
	private String userID;
	private ChannelType type;
	//List<ChannelVo> channels = new ArrayList<>();
	private List<UserChannel> channels = new ArrayList<>();
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public ChannelType getType() {
		return type;
	}
	public void setType(ChannelType type) {
		this.type = type;
	}
	public List<UserChannel> getChannels() {
		return channels;
	}
	public void setChannels(List<UserChannel> channels) {
		this.channels = channels;
	}
	
	/*
	
	public List<ChannelVo> getChannels() {
		return channels;
	}
	*/
	
	

	
	
}
