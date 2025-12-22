package com.anchor.app.oauth.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.anchor.app.oauth.enums.VisibilityType;



@Document("user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

    private Long uid;
    //User name
    @Field("userName")
	private String userName;
    //User nickname
    private String nickName;
    //Is it super administrator
    private boolean admin;
    //Gender
    private String gender;
    //Birthday
    private Long birthday;
    //Personal signature
    private String signature;
    //email
    private String email;
    //email
    private Long emailBindTime;
    //mobile
    private String mobile;
    //mobile
    private Long mobileBindTime;
    //Head portrait
    private String face;
    //Head portrait200*200
    private String face200;
    //Original image
    private String srcface;
    //State 2 normal user 3 forbidden user 4 virtual user 5 operation
    private Integer status;
    
    //Type
    
    @TextIndexed 
    private String firstName;
    
    @TextIndexed 
    private String lastName;
    
    private Long createdBy;
    private Date createdDate;
    private Long modifiedBy;
    private Date modifiedDate;
    
    private VisibilityType profileType;
    private Date lastLogin;
    
    private Integer type;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Long getBirthday() {
		return birthday;
	}
	public void setBirthday(Long birthday) {
		this.birthday = birthday;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getEmailBindTime() {
		return emailBindTime;
	}
	public void setEmailBindTime(Long emailBindTime) {
		this.emailBindTime = emailBindTime;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Long getMobileBindTime() {
		return mobileBindTime;
	}
	public void setMobileBindTime(Long mobileBindTime) {
		this.mobileBindTime = mobileBindTime;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getFace200() {
		return face200;
	}
	public void setFace200(String face200) {
		this.face200 = face200;
	}
	public String getSrcface() {
		return srcface;
	}
	public void setSrcface(String srcface) {
		this.srcface = srcface;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public VisibilityType getProfileType() {
		return profileType;
	}
	public void setProfileType(VisibilityType profileType) {
		this.profileType = profileType;
	}
	
		
	
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

	
}
