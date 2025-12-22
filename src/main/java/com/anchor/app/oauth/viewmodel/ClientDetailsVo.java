package com.anchor.app.oauth.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anchor.app.oauth.enums.AuthorityType;
import com.anchor.app.oauth.enums.GrantType;
import com.anchor.app.vo.BaseVo;


public class ClientDetailsVo extends BaseVo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5440076221875170132L;
	private String clientID;
	private String clientPassword;
	private String clientName;
	private List<AuthorityType> scope;
	private List<GrantType> grants;
	private String registeredRedirectUri;
	private List<AuthorityType> authorities;
	private int accessTokenValiditySeconds;
	private int refreshTokenValiditySeconds;
	private int autoApprove;
	Map<String, Object> additionalInformation;
	
	
	public ClientDetailsVo() {
		super();
		this.grants = new ArrayList<GrantType>();
		this.grants.add(GrantType.AUTHORIZATION_CODE);
		this.grants.add(GrantType.REFRESH_TOKEN);
		this.grants.add(GrantType.PASSWORD);
		this.grants.add(GrantType.IMPLICIT);
		
		this.scope = new ArrayList<AuthorityType>();
		this.scope.add(AuthorityType.READ_SCOPE);
		
		this.authorities = new ArrayList<AuthorityType>();
		
		this.registeredRedirectUri = "https://anchor.messenger/login";
		this.additionalInformation = new HashMap<>();
		this.autoApprove = 1;
		this.accessTokenValiditySeconds = 86400;
		this.refreshTokenValiditySeconds = 31104000;
		
	}


	public String getClientID() {
		return clientID;
	}


	public void setClientID(String clientID) {
		this.clientID = clientID;
	}


	public String getClientPassword() {
		return clientPassword;
	}


	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}


	
	public String getClientName() {
		return clientName;
	}


	public void setClientName(String clientName) {
		this.clientName = clientName;
	}



	public List<AuthorityType> getScope() {
		return scope;
	}


	public List<GrantType> getGrants() {
		return grants;
	}


	public String getRegisteredRedirectUri() {
		return registeredRedirectUri;
	}


	public void setRegisteredRedirectUri(String registeredRedirectUri) {
		this.registeredRedirectUri = registeredRedirectUri;
	}


	public List<AuthorityType> getAuthorities() {
		return authorities;
	}


	public int getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}


	public int getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}


	public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}


	public int getAutoApprove() {
		return autoApprove;
	}


	public void setAutoApprove(int autoApprove) {
		this.autoApprove = autoApprove;
	}


	public Map<String, Object> getAdditionalInformation() {
		return additionalInformation;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientID == null) ? 0 : clientID.hashCode());
		result = prime * result + ((clientPassword == null) ? 0 : clientPassword.hashCode());
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
		ClientDetailsVo other = (ClientDetailsVo) obj;
		if (clientID == null) {
			if (other.clientID != null)
				return false;
		} else if (!clientID.equals(other.clientID))
			return false;
		if (clientPassword == null) {
			if (other.clientPassword != null)
				return false;
		} else if (!clientPassword.equals(other.clientPassword))
			return false;
		return true;
	}


	
}
