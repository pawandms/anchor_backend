package com.anchor.app.notification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anchor.app.notification.service.CentrifugoService;
import com.anchor.app.notification.service.UserPresenceService;
import com.anchor.app.notification.service.UserPresenceService.UserPresenceInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/{version}/notification")
public class NotificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    @Autowired
    private CentrifugoService centrifugoService;
    
    @Autowired
    private UserPresenceService userPresenceService;
    
    /**
     * Generate Centrifugo client connection token
     * 
     * @param authentication Current authenticated user
     * @return Centrifugo JWT token
     */
    @GetMapping("/client/token")
    public ResponseEntity<?> getCentrifugoToken(Authentication authentication) {
        logger.info("Generating Centrifugo token for user");
        
        try {
            // Generate token via service (service handles user retrieval)
            Map<String, Object> tokenData = centrifugoService.generateConnectionToken();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.putAll(tokenData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error generating Centrifugo token", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to generate token: " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Generate Centrifugo channel subscription token
     * 
     * @param authentication Current authenticated user
     * @param channel Channel name as query parameter
     * @return Centrifugo JWT token for channel subscription
     */
    @GetMapping("/channel/token")
    public ResponseEntity<?> getChannelToken(
            Authentication authentication,
            @RequestParam String channel) {
        logger.info("Generating Centrifugo channel token for user");
        
        try {
            // Validate channel parameter
            if (channel == null || channel.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Channel name is required");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Generate channel token via service (service handles user retrieval)
            Map<String, Object> tokenData = centrifugoService.generateChannelToken(channel);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.putAll(tokenData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error generating Centrifugo channel token", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to generate channel token: " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
        
}
