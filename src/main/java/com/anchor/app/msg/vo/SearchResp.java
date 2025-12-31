package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchResp implements Serializable {
	
	private String reqUserId;
	private String searchKey;
	@JsonIgnore
	private Collection searchUserIds = new HashSet<String>();
	
	@JsonIgnore
	private Collection searchMsgChnlIds = new HashSet<String>();
	
	Page<SearchVo> result;
	
	public String getReqUserId() {
		return reqUserId;
	}
	public void setReqUserId(String reqUserId) {
		this.reqUserId = reqUserId;
	}
	public String getSearchKey() {
		return searchKey;
	}
	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}
	public Page<SearchVo> getResult() {
		return result;
	}
	public void setResult(Page<SearchVo> result) {
		this.result = result;
	}
	public Collection getSearchUserIds() {
		return searchUserIds;
	}
	public Collection getSearchMsgChnlIds() {
		return searchMsgChnlIds;
	}
	
	
	
	

}
