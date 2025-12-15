package com.anchor.app.oauth.config;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import com.anchor.app.oauth.enums.AuthClientType;

/**
 * Custom authentication provider that allows passwordless authentication
 * for specific scenarios like user switching while maintaining security.
 */
public class PasswordlessAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);
    
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final UserDetailsService userDetailsService;
    private SessionRegistry sessionRegistry;

    public PasswordlessAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, 
            UserDetailsService userDetailsService) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(userDetailsService, "userDetailsService cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = null;

        try {
            PasswordlessAuthenticationToken passwordlessAuthentication = (PasswordlessAuthenticationToken) authentication;
            OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(passwordlessAuthentication);
            RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

            String username = passwordlessAuthentication.getUsername();
            Set<String> authorizedScopes = passwordlessAuthentication.getScope();
            UserDetails user = null;
            AuthClientType authClientType = AuthClientType.Internal;

            if (registeredClient.getClientSettings().getSettings().containsKey("clientType")) {
                String clientType = (String) registeredClient.getClientSettings().getSettings().get("clientType");
                authClientType = AuthClientType.valueOf(clientType);
            }

            user = userDetailsService.loadUserByUsername(username);
            

            if (user == null) {
                throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, 
                    "User not found: " + username, ERROR_URI));
            }

            // Skip password validation for passwordless authentication
            logger.info("Performing passwordless authentication for user: {}", username);

            if (!registeredClient.getAuthorizationGrantTypes().contains(passwordlessAuthentication.getGrantType())) {
                throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, 
                    "Grant type not supported", ERROR_URI));
            }

            // Validate scopes
            authorizedScopes.forEach(scope -> {
                if (!registeredClient.getScopes().contains(scope)) {
                    throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_SCOPE, 
                        "Invalid scope: " + scope, ERROR_URI));
                }
            });

            if (authorizedScopes.isEmpty()) {
                authorizedScopes.addAll(registeredClient.getScopes());
            }

            Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());

            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(passwordlessAuthentication.getGrantType())
                .authorizationGrant(passwordlessAuthentication);

            // Generate Access Token
            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
            OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
            
            if (generatedAccessToken == null) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), null);

            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .authorizationGrantType(passwordlessAuthentication.getGrantType());

            if (generatedAccessToken instanceof ClaimAccessor) {
                authorizationBuilder.token(accessToken,
                    (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                        ((ClaimAccessor) generatedAccessToken).getClaims()));
            } else {
                authorizationBuilder.accessToken(accessToken);
            }

            // Generate Refresh Token
            OAuth2RefreshToken refreshToken = null;
            if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
                tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
                if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }
                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                authorizationBuilder.refreshToken(refreshToken);
            }

            // Generate ID Token if OpenID Connect scope is requested
            OidcIdToken idToken;
            if (passwordlessAuthentication.getScope().contains(OidcScopes.OPENID)) {
                SessionInformation sessionInformation = getSessionInformation(usernamePasswordAuthenticationToken);
                if (sessionInformation != null) {
                    try {
                        tokenContext = tokenContextBuilder
                            .tokenType(ID_TOKEN_TOKEN_TYPE)
                            .authorization(authorizationBuilder.build())
                            .put("sessionId", sessionInformation.getSessionId())
                            .build();
                    } catch (Exception e) {
                        logger.warn("Failed to add session information to token context", e);
                        tokenContext = tokenContextBuilder.tokenType(ID_TOKEN_TOKEN_TYPE).build();
                    }
                } else {
                    tokenContext = tokenContextBuilder.tokenType(ID_TOKEN_TOKEN_TYPE).build();
                }

                OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
                if (!(generatedIdToken instanceof Jwt)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the ID token.", ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }

                idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
                authorizationBuilder.token(idToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
            }
            else {
                idToken = null;
            }

            Map<String, Object> additionalParameters = new HashMap<>();
            if (idToken != null) {
                additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
            }

            OAuth2Authorization authorization = authorizationBuilder
                .attribute(Principal.class.getName(), usernamePasswordAuthenticationToken)
                .build();
            this.authorizationService.save(authorization);

            result = new OAuth2AccessTokenAuthenticationToken(
                registeredClient, usernamePasswordAuthenticationToken, accessToken, refreshToken, additionalParameters);

        } catch (Exception e) {
            logger.error("Passwordless authentication failed: {}", e.getMessage(), e);
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED, e.getMessage(), ERROR_URI), e);
        }

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordlessAuthenticationToken.class.isAssignableFrom(authentication);
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
        throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT));
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    private SessionInformation getSessionInformation(Authentication principal) {
        if (this.sessionRegistry == null) {
            return null;
        }
        
        SessionInformation sessionInformation = null;
        try {
            if (principal.getName() != null) {
                sessionInformation = this.sessionRegistry.getSessionInformation(principal.getName());
            }
        } catch (Exception e) {
            logger.debug("Failed to get session information for principal: {}", principal.getName(), e);
        }
        
        return sessionInformation;
    }
}