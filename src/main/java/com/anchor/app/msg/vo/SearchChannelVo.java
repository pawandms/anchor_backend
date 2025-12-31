package com.anchor.app.msg.vo;

import java.io.Serializable;

import com.anchor.app.media.model.MediaImage;
import com.anchor.app.msg.enums.ChannelSubType;
import com.anchor.app.msg.enums.ChannelSubscriptionType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.VisibilityType;

public class SearchChannelVo implements Serializable {

	private String Id;
	private ChannelType type;
	private ChannelSubType subType;
	private VisibilityType visibility;
	private ChannelSubscriptionType subscriptionType;
	private String name;
	private String description;
	private MediaImage logo;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
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
	public VisibilityType getVisibility() {
		return visibility;
	}
	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
	}
	public ChannelSubscriptionType getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(ChannelSubscriptionType subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public MediaImage getLogo() {
		return logo;
	}
	public void setLogo(MediaImage logo) {
		this.logo = logo;
	}
	
	
	
		
		
}
