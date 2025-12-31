package com.anchor.app.msg.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anchor.app.msg.enums.MsgActionType;


public class ChnlMsgAttribute implements Serializable {
	
	private int recipientCount;
	private int readUserCount;

	Map<String, String> userReaction = new HashMap<>(); 

	public int getRecipientCount() {
		return recipientCount;
	}

	public void setRecipientCount(int recipientCount) {
		this.recipientCount = recipientCount;
	}

	public int getReadUserCount() {
		return readUserCount;
	}

	public void setReadUserCount(int readUserCount) {
		this.readUserCount = readUserCount;
	}

	public Map<String, String> getUserReaction() {
		return userReaction;
	}

	public void setUserReaction(Map<String, String> userReaction) {
		this.userReaction = userReaction;
	}

	
	
}
