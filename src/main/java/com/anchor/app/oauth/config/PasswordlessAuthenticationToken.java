package com.anchor.app.oauth.config;

import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.StringUtils;

import com.anchor.app.oauth.enums.AuthParamType;

/**
 * Custom authentication token for passwordless authentication scenarios.
 * This token is used when authenticating users without password validation,
 * such as in user switching functionality.
 */
public class PasswordlessAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private static final long serialVersionUID = 1L;
    
    public static final AuthorizationGrantType PASSWORDLESS_GRANT_TYPE = new AuthorizationGrantType("passwordless");
    
    private final String username;
    private final String scope;

    public PasswordlessAuthenticationToken(
            Authentication clientPrincipal,
            Map<String, Object> additionalParameters) {
        super(PASSWORDLESS_GRANT_TYPE, clientPrincipal, additionalParameters);
        this.username = (String) additionalParameters.get(AuthParamType.USERNAME.getValue());
        String reqScope = (String) additionalParameters.get(OAuth2ParameterNames.SCOPE);
        if (null != reqScope) {
            this.scope = reqScope;
        } else {
            this.scope = "openid,profile";
        }
    }

    public String getUsername() {
        return this.username;
    }

    public Set<String> getScope() {
        return StringUtils.commaDelimitedListToSet(scope.replace(" ", ""));
    }
}