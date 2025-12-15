package com.anchor.app.oauth.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Service
public class MongoOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private final MongoTemplate mongoTemplate;
	private final RegisteredClientRepository registeredClientRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public MongoOAuth2AuthorizationService(MongoTemplate mongoTemplate,
			RegisteredClientRepository registeredClientRepository) {
		Assert.notNull(mongoTemplate, "mongoTemplate cannot be null");
		Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
		this.mongoTemplate = mongoTemplate;
		this.registeredClientRepository = registeredClientRepository;

		ClassLoader classLoader = MongoOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		this.objectMapper.registerModules(securityModules);
		this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
	}

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		
		OAuth2AuthorizationEntity entity = toEntity(authorization);
		
		Query query = new Query(Criteria.where("id").is(entity.getId()));
		OAuth2AuthorizationEntity existingEntity = mongoTemplate.findOne(query, OAuth2AuthorizationEntity.class);
		
		if (existingEntity == null) {
			mongoTemplate.insert(entity);
		} else {
			mongoTemplate.save(entity);
		}
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		Query query = new Query(Criteria.where("id").is(authorization.getId()));
		mongoTemplate.remove(query, OAuth2AuthorizationEntity.class);
	}

	@Override
	public OAuth2Authorization findById(String id) {
		Assert.hasText(id, "id cannot be empty");
		Query query = new Query(Criteria.where("id").is(id));
		OAuth2AuthorizationEntity entity = mongoTemplate.findOne(query, OAuth2AuthorizationEntity.class);
		return entity != null ? toObject(entity) : null;
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");
		
		Query query = new Query();
		if (tokenType == null) {
			query.addCriteria(new Criteria().orOperator(
				Criteria.where("state").is(token),
				Criteria.where("authorizationCodeValue").is(token),
				Criteria.where("accessTokenValue").is(token),
				Criteria.where("refreshTokenValue").is(token),
				Criteria.where("oidcIdTokenValue").is(token)
			));
		} else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
			query.addCriteria(Criteria.where("state").is(token));
		} else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
			query.addCriteria(Criteria.where("authorizationCodeValue").is(token));
		} else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
			query.addCriteria(Criteria.where("accessTokenValue").is(token));
		} else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
			query.addCriteria(Criteria.where("refreshTokenValue").is(token));
		} else if (tokenType.getValue().equals("id_token")) {
			query.addCriteria(Criteria.where("oidcIdTokenValue").is(token));
		}
		
		OAuth2AuthorizationEntity entity = mongoTemplate.findOne(query, OAuth2AuthorizationEntity.class);
		return entity != null ? toObject(entity) : null;
	}

	private OAuth2Authorization toObject(OAuth2AuthorizationEntity entity) {
		RegisteredClient registeredClient = this.registeredClientRepository.findById(entity.getRegisteredClientId());
		if (registeredClient == null) {
			throw new DataRetrievalFailureException(
					"The RegisteredClient with id '" + entity.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
		}

		OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
				.id(entity.getId())
				.principalName(entity.getPrincipalName())
				.authorizationGrantType(resolveAuthorizationGrantType(entity.getAuthorizationGrantType()))
				.authorizedScopes(StringUtils.commaDelimitedListToSet(entity.getAuthorizedScopes()))
				.attributes(attributes -> attributes.putAll(parseMap(entity.getAttributes())));

		if (entity.getState() != null) {
			builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
		}

		if (entity.getAuthorizationCodeValue() != null) {
			OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
					entity.getAuthorizationCodeValue(),
					entity.getAuthorizationCodeIssuedAt(),
					entity.getAuthorizationCodeExpiresAt());
			builder.token(authorizationCode, metadata -> metadata.putAll(parseMap(entity.getAuthorizationCodeMetadata())));
		}

		if (entity.getAccessTokenValue() != null) {
			OAuth2AccessToken accessToken = new OAuth2AccessToken(
					OAuth2AccessToken.TokenType.BEARER,
					entity.getAccessTokenValue(),
					entity.getAccessTokenIssuedAt(),
					entity.getAccessTokenExpiresAt(),
					StringUtils.commaDelimitedListToSet(entity.getAccessTokenScopes()));
			builder.token(accessToken, metadata -> metadata.putAll(parseMap(entity.getAccessTokenMetadata())));
		}

		if (entity.getRefreshTokenValue() != null) {
			OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
					entity.getRefreshTokenValue(),
					entity.getRefreshTokenIssuedAt(),
					entity.getRefreshTokenExpiresAt());
			builder.token(refreshToken, metadata -> metadata.putAll(parseMap(entity.getRefreshTokenMetadata())));
		}

		if (entity.getOidcIdTokenValue() != null) {
			OidcIdToken idToken = new OidcIdToken(
					entity.getOidcIdTokenValue(),
					entity.getOidcIdTokenIssuedAt(),
					entity.getOidcIdTokenExpiresAt(),
					parseMap(entity.getOidcIdTokenClaims()));
			builder.token(idToken, metadata -> metadata.putAll(parseMap(entity.getOidcIdTokenMetadata())));
		}

		return builder.build();
	}

	private OAuth2AuthorizationEntity toEntity(OAuth2Authorization authorization) {
		OAuth2AuthorizationEntity entity = new OAuth2AuthorizationEntity();
		entity.setId(authorization.getId());
		entity.setRegisteredClientId(authorization.getRegisteredClientId());
		entity.setPrincipalName(authorization.getPrincipalName());
		entity.setAuthorizationGrantType(authorization.getAuthorizationGrantType().getValue());
		entity.setAuthorizedScopes(StringUtils.collectionToCommaDelimitedString(authorization.getAuthorizedScopes()));
		entity.setAttributes(writeMap(authorization.getAttributes()));
		entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));

		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
				authorization.getToken(OAuth2AuthorizationCode.class);
		setTokenValues(
				authorizationCode,
				entity::setAuthorizationCodeValue,
				entity::setAuthorizationCodeIssuedAt,
				entity::setAuthorizationCodeExpiresAt,
				entity::setAuthorizationCodeMetadata
		);

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
				authorization.getToken(OAuth2AccessToken.class);
		setTokenValues(
				accessToken,
				entity::setAccessTokenValue,
				entity::setAccessTokenIssuedAt,
				entity::setAccessTokenExpiresAt,
				entity::setAccessTokenMetadata
		);
		if (accessToken != null && accessToken.getToken().getScopes() != null) {
			entity.setAccessTokenScopes(StringUtils.collectionToCommaDelimitedString(accessToken.getToken().getScopes()));
		}

		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
				authorization.getToken(OAuth2RefreshToken.class);
		setTokenValues(
				refreshToken,
				entity::setRefreshTokenValue,
				entity::setRefreshTokenIssuedAt,
				entity::setRefreshTokenExpiresAt,
				entity::setRefreshTokenMetadata
		);

		OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
				authorization.getToken(OidcIdToken.class);
		setTokenValues(
				oidcIdToken,
				entity::setOidcIdTokenValue,
				entity::setOidcIdTokenIssuedAt,
				entity::setOidcIdTokenExpiresAt,
				entity::setOidcIdTokenMetadata
		);
		if (oidcIdToken != null) {
			entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
		}

		return entity;
	}

	private void setTokenValues(
			OAuth2Authorization.Token<?> token,
			Consumer<String> tokenValueConsumer,
			Consumer<Instant> issuedAtConsumer,
			Consumer<Instant> expiresAtConsumer,
			Consumer<String> metadataConsumer) {
		if (token != null) {
			OAuth2Token oAuth2Token = token.getToken();
			tokenValueConsumer.accept(oAuth2Token.getTokenValue());
			issuedAtConsumer.accept(oAuth2Token.getIssuedAt());
			expiresAtConsumer.accept(oAuth2Token.getExpiresAt());
			metadataConsumer.accept(writeMap(token.getMetadata()));
		}
	}

	private Map<String, Object> parseMap(String data) {
		try {
			return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private String writeMap(Map<String, Object> metadata) {
		try {
			return this.objectMapper.writeValueAsString(metadata);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
		if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.AUTHORIZATION_CODE;
		} else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.CLIENT_CREDENTIALS;
		} else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.REFRESH_TOKEN;
		}
		return new AuthorizationGrantType(authorizationGrantType);
	}
}
