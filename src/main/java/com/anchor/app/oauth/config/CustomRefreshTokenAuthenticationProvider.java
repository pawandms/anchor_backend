package com.anchor.app.oauth.config;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;


public class CustomRefreshTokenAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);
    
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public CustomRefreshTokenAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        // Add comprehensive logging to track request source
        String requestId = java.util.UUID.randomUUID().toString().substring(0, 8);
        MDC.put("requestId", requestId);
        
        logger.info("=== REFRESH TOKEN AUTHENTICATION START [{}] ===", requestId);
        logger.info("Authentication class: {}", authentication.getClass().getSimpleName());
        logger.info("Authentication details: {}", authentication.getDetails());
        logger.info("Authentication principal: {}", authentication.getPrincipal());
        
        // Try to get HTTP request information from Spring context
        try {
            org.springframework.web.context.request.RequestAttributes requestAttributes = 
                org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                jakarta.servlet.http.HttpServletRequest request = 
                    ((org.springframework.web.context.request.ServletRequestAttributes) requestAttributes).getRequest();
                
                logger.info("HTTP Method: {}", request.getMethod());
                logger.info("Request URL: {}", request.getRequestURL());
                logger.info("Request URI: {}", request.getRequestURI());
                logger.info("Query String: {}", request.getQueryString());
                logger.info("Remote Address: {}", request.getRemoteAddr());
                logger.info("User Agent: {}", request.getHeader("User-Agent"));
                logger.info("Content Type: {}", request.getContentType());
                
                // Log all headers
                logger.info("=== REQUEST HEADERS ===");
                java.util.Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    logger.info("Header {}: {}", headerName, request.getHeader(headerName));
                }
                
                // Log all parameters
                logger.info("=== REQUEST PARAMETERS ===");
                java.util.Map<String, String[]> parameterMap = request.getParameterMap();
                for (java.util.Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    logger.info("Parameter {}: {}", entry.getKey(), java.util.Arrays.toString(entry.getValue()));
                }
            } else {
                logger.warn("No request context available - this might be a background/scheduled task");
            }
        } catch (Exception e) {
            logger.warn("Could not retrieve HTTP request information: {}", e.getMessage());
        }
        
        // Log thread and stack trace information
        logger.info("Thread: {}", Thread.currentThread().getName());
        logger.info("Stack trace preview:");
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Math.min(10, stackTrace.length); i++) {
            logger.info("  [{}] {}", i, stackTrace[i]);
        }

        Authentication result = null;
        try{
            OAuth2RefreshTokenAuthenticationToken refreshTokenAuthentication = 
            (OAuth2RefreshTokenAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
                refreshTokenAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        OAuth2Authorization authorization = this.authorizationService.findByToken(
                refreshTokenAuthentication.getRefreshToken(), OAuth2TokenType.REFRESH_TOKEN);
        if (authorization == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        if (!registeredClient.getId().equals(authorization.getRegisteredClientId())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (!refreshToken.isActive()) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        // Generate new access token
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authorization.getAttribute(Principal.class.getName()))
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorization(authorization)
                .authorizedScopes(authorization.getAuthorizedScopes())
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrant(refreshTokenAuthentication);

        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization);
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                            ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // Generate new refresh token if configured
        OAuth2RefreshToken newRefreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }
            newRefreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(newRefreshToken);
        }

        // Generate ID token if OpenID Connect scope is present
        OidcIdToken idToken = null;
        if (authorization.getAuthorizedScopes().contains(OidcScopes.OPENID)) {
            tokenContext = tokenContextBuilder
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    .authorization(authorizationBuilder.build())
                    .build();
            OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the ID token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }
            OidcIdToken generatedOidcIdToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
            idToken = generatedOidcIdToken;
            authorizationBuilder.token(idToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, generatedOidcIdToken.getClaims()));
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        if (idToken != null) {
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
        }

        // Add refresh token expiry information
        if (newRefreshToken != null) {
            long refreshTokenExpiresIn = registeredClient.getTokenSettings().getRefreshTokenTimeToLive().getSeconds();
            additionalParameters.put("refresh_expires_in", refreshTokenExpiresIn);
        }

        // Add Internla User Details
        /* 
		if(authClientType.equals(AuthClientType.Internal))
		{
			com.hifinite.components.user.model.User loggedInUser = userService.getActiveUserByUsername(username);
				if (loggedInUser != null) {
					java.util.List<Integer> roleArray = new java.util.ArrayList<>();
					loggedInUser.getUserRole().forEach(userRole -> roleArray.add(userRole.getRoleId()));
					
					// Add custom claims to the JWT
					additionalParameters.put("userID", loggedInUser.getUserID().toString());
					additionalParameters.put("userTypeID", loggedInUser.getUserTypeID().toString());
					additionalParameters.put("roleID", roleArray);
					additionalParameters.put("clientID", loggedInUser.getClientID().toString());
					
				}
		}
                
        */


        authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        result =  new OAuth2AccessTokenAuthenticationToken(registeredClient, 
                authorization.getAttribute(Principal.class.getName()), accessToken, newRefreshToken, additionalParameters);
        
        logger.info("=== REFRESH TOKEN AUTHENTICATION SUCCESS [{}] ===", MDC.get("requestId"));
        logger.info("Generated new access token for client: {}", registeredClient.getClientId());
        logger.info("Access token expires at: {}", accessToken.getExpiresAt());
        if (newRefreshToken != null) {
            logger.info("Generated new refresh token expires at: {}", newRefreshToken.getExpiresAt());
        }
        
        }
        catch(Exception e)
		{
			logger.error("=== REFRESH TOKEN AUTHENTICATION FAILED [{}] ===", MDC.get("requestId"));
			logger.error("Error details: {}", e.getMessage(), e);
			throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED, e.getMessage(), ERROR_URI), e);
		}
		finally {
		    logger.info("=== REFRESH TOKEN AUTHENTICATION END [{}] ===", MDC.get("requestId"));
		    MDC.remove("requestId");
		}

        return result;
        
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
            Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}
