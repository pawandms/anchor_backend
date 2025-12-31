package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anchor.app.media.model.MediaImage;
import com.anchor.app.msg.enums.ChannelSubType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.model.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Response of Channel belongs to User 
 */
public class UserChannel implements Serializable {
	
	private String chnlId;
	private ChannelType type;
	private ChannelSubType subType;
	private String name;
	private Message msg;
	private Date msgDate;
	private List<UserRoleType> userRoles = new ArrayList<>();
	private MediaImage chnLogo;
	private int unreadCount;
	private Date unReadDate;
	private String unReadMsgID;	
	private boolean active;
	private List<ChannelUser> chnlUsers = new ArrayList<>();
	
	@JsonIgnore
	private String msgID;
	
	public String getChnlId() {
		return chnlId;
	}
	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}
	
	
	
	public ChannelType getType() {
		return type;
	}
	public void setType(ChannelType type) {
		this.type = type;
	}
	public ChannelSubType getSubType() {
		return subType;
	}
	public void setSubType(ChannelSubType subType) {
		this.subType = subType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Message getMsg() {
		return msg;
	}
	public void setMsg(Message msg) {
		this.msg = msg;
	}
	public Date getMsgDate() {
		return msgDate;
	}
	public void setMsgDate(Date msgDate) {
		this.msgDate = msgDate;
	}
	public List<UserRoleType> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRoleType> userRoles) {
		this.userRoles = userRoles;
	}
	public MediaImage getChnLogo() {
		return chnLogo;
	}
	public void setChnLogo(MediaImage chnLogo) {
		this.chnLogo = chnLogo;
	}
	public int getUnreadCount() {
		return unreadCount;
	}
	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getMsgID() {
		return msgID;
	}
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
	public List<ChannelUser> getChnlUsers() {
		return chnlUsers;
	}
	public void setChnlUsers(List<ChannelUser> chnlUsers) {
		this.chnlUsers = chnlUsers;
	}
	
	
	
	
}
