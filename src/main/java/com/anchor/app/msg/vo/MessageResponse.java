package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.anchor.app.vo.BaseVo;


public class MessageResponse extends BaseVo implements Serializable {
	
	private static final long serialVersionUID = 4886132886911501895L;
	
	private String userID;
	private String channelID;
	private Page<MessageVo> msgs;
	private int currentPage;
	private int totalPage;
	private int currentItems;
	private int totalItems;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	
	public Page<MessageVo> getMsgs() {
		return msgs;
	}
	public void setMsgs(Page<MessageVo> msgs) {
		this.msgs = msgs;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getCurrentItems() {
		return currentItems;
	}
	public void setCurrentItems(int currentItems) {
		this.currentItems = currentItems;
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	
	
}
