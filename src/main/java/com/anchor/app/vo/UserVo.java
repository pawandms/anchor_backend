package com.anchor.app.vo;

import java.io.Serializable;

import com.anchor.app.enums.GenderType;
import com.anchor.app.oauth.enums.UserRoleType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.anchor.app.oauth.model.UserVerifyToken;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserVo extends BaseVo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4731790451616841559L;
	
	private Long id;
	private String userName;
	private String email;
	private String firstName;
	private String lastName;
	private GenderType gender;
	private String password;
	private UserRoleType role;
	private VisibilityType profileType;

	
	@JsonIgnore
	private UserVerifyToken verificationToken;
	
	
	public UserVo() {
		super();
		this.role = UserRoleType.GENERAL_USER;
	}
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public UserRoleType getRole() {
		return role;
	}
	public void setRole(UserRoleType role) {
		this.role = role;
	}

	public VisibilityType getProfileType() {
		return profileType;
	}
	public void setProfileType(VisibilityType profileType) {
		this.profileType = profileType;
	}
	public UserVerifyToken getVerificationToken() {
		return verificationToken;
	}
	public void setVerificationToken(UserVerifyToken verificationToken) {
		this.verificationToken = verificationToken;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		UserVo other = (UserVo) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	

}
