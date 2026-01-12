package com.anchor.app.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.notification.exceptions.NotificationException;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.util.EnvProp;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for interacting with Centrifugo messaging system.
 * Uses HTTP API for server-side publishing (recommended for backend services).
 * The centrifuge-java library is available for client-side subscriptions if needed.
 */
@Service
public class CentrifugoService {
    
    private static final Logger logger = LoggerFactory.getLogger(CentrifugoService.class);

    @Autowired
    private EnvProp envProp;
    
    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private final ObjectMapper objectMapper;
    
    public CentrifugoService() {
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Generate Centrifugo client connection token
     * 
     * @return Map containing token and its details
     * @throws NotificationException 
     */
    public Map<String, Object> generateConnectionToken() throws NotificationException {
        try {
            // Get user details from authentication facade
            User user = authenticationFacade.getApiAuthenticationDetails();
            
            if( null == user)
            {
                throw new NotificationException("Invalid User");
            }   
            
            String userId = user.getId();
            String userName = user.getUserName();
            
            String centrifugoTokenSecret = envProp.getCentrifugeAuthTokenSecret();
            int tokenExpirationSeconds = envProp.getCentrifugeAuthTokenExpirationSec();

            long currentTimeMillis = System.currentTimeMillis();
            Date issuedAt = new Date(currentTimeMillis);
            Date expiration = new Date(currentTimeMillis + (tokenExpirationSeconds * 1000L));
            
            SecretKey key = Keys.hmacShaKeyFor(centrifugoTokenSecret.getBytes(StandardCharsets.UTF_8));
            
            String token = Jwts.builder()
                    .subject(userId)
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                    .signWith(key)
                    .compact();
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userID", userId);
            result.put("userName", userName);
            result.put("subject", userId);
            result.put("expires_in", tokenExpirationSeconds);
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", userId);
            claims.put("iat", issuedAt.getTime() / 1000);
            claims.put("exp", expiration.getTime() / 1000);
            result.put("claims", claims);
            
            logger.info("Generated Centrifugo token for user: {} ({})", userName, userId);
            return result;
            
        } catch (Exception e) {
            logger.error("Error generating Centrifugo token", e);
            throw new NotificationException("Failed to generate Centrifugo token", e);
        }
    }
    
    /**
     * Generate Centrifugo channel subscription token
     * 
     * @param channel Channel name
     * @return Map containing token and its details
     * @throws NotificationException 
     */
    public Map<String, Object> generateChannelToken(String channel) throws NotificationException {
        try {
            // Get user details from authentication facade
            User user = authenticationFacade.getApiAuthenticationDetails();
            if( null == user)
            {
                throw new NotificationException("Invalid User");
            }   
            String userId = user.getId();
            String userName = user.getUserName();
            
            String centrifugoTokenSecret = envProp.getCentrifugeAuthTokenSecret();
            
            long currentTimeMillis = System.currentTimeMillis();
            Date expiration = new Date(currentTimeMillis + 300000L); // 300 seconds = 5 minutes
            
            SecretKey key = Keys.hmacShaKeyFor(centrifugoTokenSecret.getBytes(StandardCharsets.UTF_8));
            
            String token = Jwts.builder()
                    .subject(userId)
                    .expiration(expiration)
                    .claim("channel", channel)
                    .signWith(key)
                    .compact();
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userID", userId);
            result.put("userName", userName);
            result.put("subject", userId);
            result.put("expires_in", 300); // 300 seconds
            result.put("channel", channel);
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", userId);
            claims.put("exp", expiration.getTime() / 1000);
            claims.put("channel", channel);
            result.put("claims", claims);
            
            logger.info("Generated Centrifugo channel token for user: {} ({}), channel: {}", userName, userId, channel);
            return result;
            
        } catch (Exception e) {
            logger.error("Error generating Centrifugo channel token for channel: {}", channel, e);
            throw new NotificationException("Failed to generate Centrifugo channel token", e);
        }
    }
        
    
}
