package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.Date;

import com.anchor.app.enums.GenderType;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.msg.enums.ChannelSubType;
import com.anchor.app.msg.enums.ChannelSubscriptionType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.ChannelVisibility;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.VisibilityType;

public class SearchVo implements Serializable {
	
	
	private EventEntityType entityType;
	
	private SearchChannelVo chnl;
	private SearchUserVo user;
	
	
	/*
	private VisibilityType visibility;
	private String Id;
	
	// Attribute related to User
	
	private String firstName;
    private String lastName;
    private GenderType gender;
    private MediaImage profileImage;
    
    private Date lastLogin;
    private boolean active;


    // Attribute Related to Msg Chnl
    
    private ChannelType chnlType;
	private ChannelSubType chnlSubType;
	private ChannelSubscriptionType chnlSubscriptionType;
	private String chnlName;
	private String chnlDescription;
    private MediaImage chnlLogo;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public GenderType getGender() {
		return gender;
	}
	public void setGender(GenderType gender) {
		this.gender = gender;
	}
	public MediaImage getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(MediaImage profileImage) {
		this.profileImage = profileImage;
	}
	
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public ChannelType getChnlType() {
		return chnlType;
	}
	public void setChnlType(ChannelType chnlType) {
		this.chnlType = chnlType;
	}
	public String getChnlName() {
		return chnlName;
	}
	public void setChnlName(String chnlName) {
		this.chnlName = chnlName;
	}
	public String getChnlDescription() {
		return chnlDescription;
	}
	public void setChnlDescription(String chnlDescription) {
		this.chnlDescription = chnlDescription;
	}
	public MediaImage getChnlLogo() {
		return chnlLogo;
	}
	public void setChnlLogo(MediaImage chnlLogo) {
		this.chnlLogo = chnlLogo;
	}
	public ChannelSubType getChnlSubType() {
		return chnlSubType;
	}
	public void setChnlSubType(ChannelSubType chnlSubType) {
		this.chnlSubType = chnlSubType;
	}
	
	public ChannelSubscriptionType getChnlSubscriptionType() {
		return chnlSubscriptionType;
	}
	public void setChnlSubscriptionType(ChannelSubscriptionType chnlSubscriptionType) {
		this.chnlSubscriptionType = chnlSubscriptionType;
	}
	public VisibilityType getVisibility() {
		return visibility;
	}
	public void setVisibility(VisibilityType visibility) {
		this.visibility = visibility;
	}
	
	*/
	
	public EventEntityType getEntityType() {
		return entityType;
	}
	public void setEntityType(EventEntityType entityType) {
		this.entityType = entityType;
	}
	
	public SearchChannelVo getChnl() {
		return chnl;
	}
	public void setChnl(SearchChannelVo chnl) {
		this.chnl = chnl;
	}
	public SearchUserVo getUser() {
		return user;
	}
	public void setUser(SearchUserVo user) {
		this.user = user;
	}
	
	
	
    
	
}
