package com.anchor.app.oauth.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.anchor.app.oauth.model.UserAuth;
import com.anchor.app.users.repository.UserAuthRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomTokenResponseHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomTokenResponseHandler.class);
    
    private final UserAuthRepository userAuthRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public CustomTokenResponseHandler(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        if (authentication instanceof OAuth2AccessTokenAuthenticationToken) {
            OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = 
                (OAuth2AccessTokenAuthenticationToken) authentication;
            
            // Get the principal
            Authentication principal = (Authentication) accessTokenAuthentication.getPrincipal();
            String username = principal.getName();
            
            try {
                // Build response map
                Map<String, Object> tokenResponse = new HashMap<>();
                tokenResponse.put("access_token", accessTokenAuthentication.getAccessToken().getTokenValue());
                tokenResponse.put("token_type", "Bearer");
                tokenResponse.put("expires_in", 
                    accessTokenAuthentication.getAccessToken().getExpiresAt().getEpochSecond() - 
                    accessTokenAuthentication.getAccessToken().getIssuedAt().getEpochSecond());
                
                if (accessTokenAuthentication.getRefreshToken() != null) {
                    tokenResponse.put("refresh_token", accessTokenAuthentication.getRefreshToken().getTokenValue());
                }
                
                if (accessTokenAuthentication.getAccessToken().getScopes() != null && 
                    !accessTokenAuthentication.getAccessToken().getScopes().isEmpty()) {
                    tokenResponse.put("scope", String.join(" ", accessTokenAuthentication.getAccessToken().getScopes()));
                }
                
                // Add custom user claims to response
                UserAuth userAuth = userAuthRepository.findByUserName(username);
                
                if (userAuth != null) {
                    // Add custom fields to response payload
                    tokenResponse.put("userID", userAuth.getId());
                    tokenResponse.put("username", userAuth.getIdentifier());
                    
                    if (userAuth.getRoles() != null && !userAuth.getRoles().isEmpty()) {
                        tokenResponse.put("roles", userAuth.getRoles());
                    }
                    
                    logger.debug("Added custom claims to token response for user: {}, userID: {}", 
                        username, userAuth.getId());
                }
                
                // Write response
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
                
            } catch (Exception e) {
                logger.error("Error creating custom token response for user: " + username, e);
                throw new ServletException("Error creating token response", e);
            }
        }
    }
}
