package com.anchor.app.msg.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for Service Side processing of
 * get Message for User on given channel 
 */
public class UserChnlMsgVo {
	
	private String chnlID;
	private String userID;
	
	List<ChnlUserEnrolmentVo> enrollDates = new ArrayList<>();

	public String getChnlID() {
		return chnlID;
	}

	public void setChnlID(String chnlID) {
		this.chnlID = chnlID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public List<ChnlUserEnrolmentVo> getEnrollDates() {
		return enrollDates;
	}

	public void setEnrollDates(List<ChnlUserEnrolmentVo> enrollDates) {
		this.enrollDates = enrollDates;
	}
	
	

}
