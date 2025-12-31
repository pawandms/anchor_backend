package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anchor.app.enums.GenderType;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.enums.VisibilityType;

public class SearchUserVo implements Serializable {
	
	private String userID;
    private String firstName;
    private String lastName;
    private GenderType gender;
    private MediaImage profileImage;
    private VisibilityType profileType;
    private Date lastLogin;
    
	private boolean active;

	private List<UserRoleType> userRoles = new ArrayList<>();
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public List<UserRoleType> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRoleType> userRoles) {
		this.userRoles = userRoles;
	}
	public VisibilityType getProfileType() {
		return profileType;
	}
	public void setProfileType(VisibilityType profileType) {
		this.profileType = profileType;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	
	
}
