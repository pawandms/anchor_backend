package com.anchor.app.msg.vo;

import java.util.ArrayList;
import java.util.List;

import com.anchor.app.vo.BaseVo;

public class ChannelParticipantResponse extends BaseVo {
	
	private String userID;
	private String chnlId;
	private List<ChannelUser> chnlUsers = new ArrayList<>();
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getChnlId() {
		return chnlId;
	}
	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}
	public List<ChannelUser> getChnlUsers() {
		return chnlUsers;
	}
	public void setChnlUsers(List<ChannelUser> chnlUsers) {
		this.chnlUsers = chnlUsers;
	}

	
	

}
