package com.anchor.app.messaging.controller;

import com.anchor.app.messaging.service.CentrifugoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/{version}/presence")
public class UserPresenceController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserPresenceController.class);
    
    @Autowired
    private CentrifugoService centrifugoService;
    
    /**
     * Set user status to active
     * 
     * @param userId User ID
     * @return Response
     */
    @PostMapping("/{userId}/active")
    public ResponseEntity<?> setUserActive(@PathVariable String userId) {
        logger.info("Setting user active: {}", userId);
        
        try {
            centrifugoService.publishUserStatus(userId, "active");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("status", "active");
            response.put("message", "User status set to active");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error setting user active", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to set user active: " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Set user status to inactive
     * 
     * @param userId User ID
     * @return Response
     */
    @PostMapping("/{userId}/inactive")
    public ResponseEntity<?> setUserInactive(@PathVariable String userId) {
        logger.info("Setting user inactive: {}", userId);
        
        try {
            centrifugoService.publishUserStatus(userId, "inactive");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("status", "inactive");
            response.put("message", "User status set to inactive");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error setting user inactive", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to set user inactive: " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Broadcast a message to all users listening to a specific user's status
     * 
     * @param userId User ID
     * @param message Message to broadcast
     * @return Response
     */
    @PostMapping("/{userId}/broadcast")
    public ResponseEntity<?> broadcastToUser(
            @PathVariable String userId,
            @RequestBody Map<String, String> request) {
        
        logger.info("Broadcasting to user: {}", userId);
        
        try {
            String message = request.get("message");
            String channel = "user:status:" + userId;
            
            centrifugoService.broadcast(channel, message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", userId);
            response.put("message", "Message broadcasted successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error broadcasting message", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to broadcast: " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
