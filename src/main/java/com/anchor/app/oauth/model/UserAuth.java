package com.anchor.app.oauth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class UserAuth implements Serializable {
	
	 //  id
	@Id
    private Long id;
    private Long uid;
    //1 mobile number 2 email 3 user name 4qq 5 wechat 6 Tencent Weibo 7 Sina Weibo
    private Integer identityType;
    //Mobile phone number, email address, user name or unique identification of third-party application
    private String identifier;
    //Password Certificate (stored password in the station, not saved or saved token outside the station)
    private String certificate;
    //MD5 salt value encryption
    private String md5;
    
    private Long createdBy;
    private Date createdDate;
    private Long modifiedBy;
    private Date modifiedDate;
    
    //Role ID
    private List<String> roles;

    private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;

    
    public UserAuth()
    {
    	this.roles = new ArrayList<String>();
    	this.accountNonExpired = true;
    	this.accountNonLocked = true;
    	this.credentialsNonExpired = true;
    
    }
    
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

	public Integer getIdentityType() {
		return identityType;
	}

	public void setIdentityType(Integer identityType) {
		this.identityType = identityType;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public List<String> getRoles() {
		return roles;
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


	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		UserAuth other = (UserAuth) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserAuth [id=" + id + ", uid=" + uid + ", identityType=" + identityType + ", identifier=" + identifier
				+ ", certificate=" + certificate + ", md5=" + md5 + ", roles=" + roles + "]";
	}

	
    

}
