package com.anchor.app.oauth.config;

import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.StringUtils;

import com.anchor.app.oauth.enums.AuthParamType;

public class CustomCodeGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

	private static final long serialVersionUID = 1L;
	private final String username;
	private final String password;
	private final String scope;

	protected CustomCodeGrantAuthenticationToken(
			String granttype,
			Authentication clientPrincipal, 
			Map<String, Object> additionalParameters) {
		super(new AuthorizationGrantType(granttype), clientPrincipal, additionalParameters);
		this.username = (String) additionalParameters.get(AuthParamType.USERNAME.getValue());
		this.password = (String) additionalParameters.get(AuthParamType.PASSWORD.getValue());
		String reqScope = (String) additionalParameters.get(OAuth2ParameterNames.SCOPE);
		if( null != reqScope)
		{
			this.scope = reqScope;
		}
		else {
			this.scope = "openid,profile";
		}		
		
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}
	
	public Set<String> getScope() {
		return StringUtils.commaDelimitedListToSet(scope.replace(" ", ""));
	}

}
