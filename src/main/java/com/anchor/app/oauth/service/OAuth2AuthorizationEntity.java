package com.anchor.app.oauth.service;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "oauth2_authorizations")
public class OAuth2AuthorizationEntity {

	@Id
	private String id;
	private String registeredClientId;
	private String principalName;
	private String authorizationGrantType;
	private String authorizedScopes;
	private String attributes;
	private String state;

	private String authorizationCodeValue;
	private Instant authorizationCodeIssuedAt;
	private Instant authorizationCodeExpiresAt;
	private String authorizationCodeMetadata;

	private String accessTokenValue;
	private Instant accessTokenIssuedAt;
	private Instant accessTokenExpiresAt;
	private String accessTokenMetadata;
	private String accessTokenScopes;

	private String refreshTokenValue;
	private Instant refreshTokenIssuedAt;
	private Instant refreshTokenExpiresAt;
	private String refreshTokenMetadata;

	private String oidcIdTokenValue;
	private Instant oidcIdTokenIssuedAt;
	private Instant oidcIdTokenExpiresAt;
	private String oidcIdTokenMetadata;
	private String oidcIdTokenClaims;

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegisteredClientId() {
		return registeredClientId;
	}

	public void setRegisteredClientId(String registeredClientId) {
		this.registeredClientId = registeredClientId;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getAuthorizationGrantType() {
		return authorizationGrantType;
	}

	public void setAuthorizationGrantType(String authorizationGrantType) {
		this.authorizationGrantType = authorizationGrantType;
	}

	public String getAuthorizedScopes() {
		return authorizedScopes;
	}

	public void setAuthorizedScopes(String authorizedScopes) {
		this.authorizedScopes = authorizedScopes;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAuthorizationCodeValue() {
		return authorizationCodeValue;
	}

	public void setAuthorizationCodeValue(String authorizationCodeValue) {
		this.authorizationCodeValue = authorizationCodeValue;
	}

	public Instant getAuthorizationCodeIssuedAt() {
		return authorizationCodeIssuedAt;
	}

	public void setAuthorizationCodeIssuedAt(Instant authorizationCodeIssuedAt) {
		this.authorizationCodeIssuedAt = authorizationCodeIssuedAt;
	}

	public Instant getAuthorizationCodeExpiresAt() {
		return authorizationCodeExpiresAt;
	}

	public void setAuthorizationCodeExpiresAt(Instant authorizationCodeExpiresAt) {
		this.authorizationCodeExpiresAt = authorizationCodeExpiresAt;
	}

	public String getAuthorizationCodeMetadata() {
		return authorizationCodeMetadata;
	}

	public void setAuthorizationCodeMetadata(String authorizationCodeMetadata) {
		this.authorizationCodeMetadata = authorizationCodeMetadata;
	}

	public String getAccessTokenValue() {
		return accessTokenValue;
	}

	public void setAccessTokenValue(String accessTokenValue) {
		this.accessTokenValue = accessTokenValue;
	}

	public Instant getAccessTokenIssuedAt() {
		return accessTokenIssuedAt;
	}

	public void setAccessTokenIssuedAt(Instant accessTokenIssuedAt) {
		this.accessTokenIssuedAt = accessTokenIssuedAt;
	}

	public Instant getAccessTokenExpiresAt() {
		return accessTokenExpiresAt;
	}

	public void setAccessTokenExpiresAt(Instant accessTokenExpiresAt) {
		this.accessTokenExpiresAt = accessTokenExpiresAt;
	}

	public String getAccessTokenMetadata() {
		return accessTokenMetadata;
	}

	public void setAccessTokenMetadata(String accessTokenMetadata) {
		this.accessTokenMetadata = accessTokenMetadata;
	}

	public String getAccessTokenScopes() {
		return accessTokenScopes;
	}

	public void setAccessTokenScopes(String accessTokenScopes) {
		this.accessTokenScopes = accessTokenScopes;
	}

	public String getRefreshTokenValue() {
		return refreshTokenValue;
	}

	public void setRefreshTokenValue(String refreshTokenValue) {
		this.refreshTokenValue = refreshTokenValue;
	}

	public Instant getRefreshTokenIssuedAt() {
		return refreshTokenIssuedAt;
	}

	public void setRefreshTokenIssuedAt(Instant refreshTokenIssuedAt) {
		this.refreshTokenIssuedAt = refreshTokenIssuedAt;
	}

	public Instant getRefreshTokenExpiresAt() {
		return refreshTokenExpiresAt;
	}

	public void setRefreshTokenExpiresAt(Instant refreshTokenExpiresAt) {
		this.refreshTokenExpiresAt = refreshTokenExpiresAt;
	}

	public String getRefreshTokenMetadata() {
		return refreshTokenMetadata;
	}

	public void setRefreshTokenMetadata(String refreshTokenMetadata) {
		this.refreshTokenMetadata = refreshTokenMetadata;
	}

	public String getOidcIdTokenValue() {
		return oidcIdTokenValue;
	}

	public void setOidcIdTokenValue(String oidcIdTokenValue) {
		this.oidcIdTokenValue = oidcIdTokenValue;
	}

	public Instant getOidcIdTokenIssuedAt() {
		return oidcIdTokenIssuedAt;
	}

	public void setOidcIdTokenIssuedAt(Instant oidcIdTokenIssuedAt) {
		this.oidcIdTokenIssuedAt = oidcIdTokenIssuedAt;
	}

	public Instant getOidcIdTokenExpiresAt() {
		return oidcIdTokenExpiresAt;
	}

	public void setOidcIdTokenExpiresAt(Instant oidcIdTokenExpiresAt) {
		this.oidcIdTokenExpiresAt = oidcIdTokenExpiresAt;
	}

	public String getOidcIdTokenMetadata() {
		return oidcIdTokenMetadata;
	}

	public void setOidcIdTokenMetadata(String oidcIdTokenMetadata) {
		this.oidcIdTokenMetadata = oidcIdTokenMetadata;
	}

	public String getOidcIdTokenClaims() {
		return oidcIdTokenClaims;
	}

	public void setOidcIdTokenClaims(String oidcIdTokenClaims) {
		this.oidcIdTokenClaims = oidcIdTokenClaims;
	}
}
