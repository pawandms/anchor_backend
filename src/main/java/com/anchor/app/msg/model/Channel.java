package com.anchor.app.msg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.media.model.MediaImage;
import com.anchor.app.msg.enums.ChannelSubType;
import com.anchor.app.msg.enums.ChannelSubscriptionType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.VisibilityType;
import com.anchor.app.vo.BaseVo;

@Document(collection= "channel")
public class Channel extends BaseVo implements Serializable
{

	@Id
	private String id;
	
	private ChannelType type;
	private ChannelSubType subType;
	private VisibilityType visibility;
	private ChannelSubscriptionType subscriptionType;
	
	private String name;
	private String description;
	
	private String encriptedName;
	
	// Available Images for Media
	private List<MediaImage> imageList = new ArrayList();
	private boolean active;
	private String latestMsgId = "NA";
	private Date latestMsgDate = new Date();
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getEncriptedName() {
		return encriptedName;
	}
	public void setEncriptedName(String encriptedName) {
		this.encriptedName = encriptedName;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getLatestMsgId() {
		return latestMsgId;
	}
	public void setLatestMsgId(String latestMsgId) {
		this.latestMsgId = latestMsgId;
	}
	public Date getLatestMsgDate() {
		return latestMsgDate;
	}
	public void setLatestMsgDate(Date latestMsgDate) {
		this.latestMsgDate = latestMsgDate;
	}
	public List<MediaImage> getImageList() {
		return imageList;
	}
	public void setImageList(List<MediaImage> imageList) {
		this.imageList = imageList;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Channel other = (Channel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
}
