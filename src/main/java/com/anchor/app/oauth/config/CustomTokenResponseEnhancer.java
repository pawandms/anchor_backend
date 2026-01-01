package com.anchor.app.oauth.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.model.UserAuth;
import com.anchor.app.users.repository.UserAuthRepository;
import com.anchor.app.users.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomTokenResponseEnhancer {

    private static final Logger logger = LoggerFactory.getLogger(CustomTokenResponseEnhancer.class);
    
    private final UserAuthRepository userAuthRepository;
     private final UserRepository userRepository;
    
    public CustomTokenResponseEnhancer(UserAuthRepository userAuthRepository, UserRepository userRepository) {
        this.userAuthRepository = userAuthRepository;
        this.userRepository = userRepository;
    }
    
    public void enhance(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication instanceof OAuth2AccessTokenAuthenticationToken) {
            OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = 
                (OAuth2AccessTokenAuthenticationToken) authentication;
            
            // Get the principal (user details)
            Authentication principal = (Authentication) accessTokenAuthentication.getPrincipal();
            String username = principal.getName();
            
            try {
                // Find UserAuth by username
                UserAuth userAuth = userAuthRepository.findByUserName(username);
                
                if (userAuth != null) {
                        
                        // Create custom response with additional parameters
                        Map<String, Object> additionalParameters = new HashMap<>(accessTokenAuthentication.getAdditionalParameters());
                        additionalParameters.put("userID", userAuth.getId());
                        additionalParameters.put("username", userAuth.getIdentifier());
                        
                        if (userAuth.getRoles() != null && !userAuth.getRoles().isEmpty()) {
                            additionalParameters.put("roles", userAuth.getRoles());
                        }
                        
                        // Build enhanced token response
                        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessTokenAuthentication.getAccessToken().getTokenValue())
                            .tokenType(OAuth2AccessToken.TokenType.BEARER)
                            .expiresIn(accessTokenAuthentication.getAccessToken().getExpiresAt().getEpochSecond() - 
                                      accessTokenAuthentication.getAccessToken().getIssuedAt().getEpochSecond());
                        
                        if (accessTokenAuthentication.getRefreshToken() != null) {
                            builder.refreshToken(accessTokenAuthentication.getRefreshToken().getTokenValue());
                        }
                        
                        if (accessTokenAuthentication.getAccessToken().getScopes() != null) {
                            builder.scopes(accessTokenAuthentication.getAccessToken().getScopes());
                        }
                        
                        // Add custom parameters to response
                        builder.additionalParameters(additionalParameters);
                        
                        OAuth2AccessTokenResponse tokenResponse = builder.build();
                        
                        // Write response
                        OAuth2AccessTokenResponseHttpMessageConverter converter = new OAuth2AccessTokenResponseHttpMessageConverter();
                        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
                        converter.write(tokenResponse, null, httpResponse);
                        
                        logger.debug("Enhanced token response with userID: {} and username: {}", userAuth.getId(), userAuth.getIdentifier());
                    
                }
            } catch (Exception e) {
                logger.error("Error enhancing token response for user: " + username, e);
            }
        }
    }
}
