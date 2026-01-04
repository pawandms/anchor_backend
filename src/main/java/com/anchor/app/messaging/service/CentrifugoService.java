package com.anchor.app.messaging.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    
    @Value("${centrifugo.api.url:http://localhost:8000/api}")
    private String centrifugoApiUrl;
    
    @Value("${centrifugo.api.key:anchor-api-key-2025}")
    private String apiKey;
    
    private final ObjectMapper objectMapper;
    
    public CentrifugoService() {
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Publish user status change (active/inactive) to a channel using Server API
     * 
     * @param userId User ID
     * @param status "active" or "inactive"
     */
    public void publishUserStatus(String userId, String status) {
        try {
            String channel = "user:status:" + userId;
            
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("status", status);
            data.put("timestamp", System.currentTimeMillis());
            
            publishViaHttpApi(channel, data);
            
            logger.info("Published user status: userId={}, status={}", userId, status);
            
        } catch (Exception e) {
            logger.error("Error publishing user status", e);
        }
    }
    
    /**
     * Publish a message to a Centrifugo channel via HTTP API
     * 
     * @param channel Channel name
     * @param data Data to publish
     */
    private void publishViaHttpApi(String channel, Object data) {
        try {
            // Using HTTP API directly with RestTemplate for server-side publishing
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            
            Map<String, Object> request = new HashMap<>();
            request.put("method", "publish");
            
            Map<String, Object> params = new HashMap<>();
            params.put("channel", channel);
            params.put("data", data);
            
            request.put("params", params);
            
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.set("X-API-Key", apiKey);
            headers.set("Authorization", "apikey " + apiKey);
            
            String jsonRequest = objectMapper.writeValueAsString(request);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(jsonRequest, headers);
            
            org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(
                centrifugoApiUrl,
                org.springframework.http.HttpMethod.POST,
                entity,
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.debug("Successfully published to channel: {}", channel);
            } else {
                logger.error("Failed to publish to channel: {}, status: {}", channel, response.getStatusCode());
            }
            
        } catch (Exception e) {
            logger.error("Error publishing to Centrifugo channel: {}", channel, e);
            throw new RuntimeException("Failed to publish message", e);
        }
    }
    
    /**
     * Broadcast message to all users in a channel
     * 
     * @param channel Channel name
     * @param message Message to broadcast
     */
    public void broadcast(String channel, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("timestamp", System.currentTimeMillis());
        
        publishViaHttpApi(channel, data);
    }
}
