package com.anchor.app.notification.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service to track user presence/online status
 */
@Service
public class UserPresenceService {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    // Map to store online users: userId -> user info
    private final Map<String, UserPresenceInfo> onlineUsers = new ConcurrentHashMap<>();
    
    /**
     * Mark user as online
     */
    public void userJoined(String userId, String clientId) {
        UserPresenceInfo info = new UserPresenceInfo(userId, clientId, System.currentTimeMillis());
        onlineUsers.put(userId, info);
        logger.info("User joined - UserID: {}, ClientID: {}, Total online: {}", 
            userId, clientId, onlineUsers.size());
    }
    
    /**
     * Mark user as offline
     */
    public void userLeft(String userId, String clientId) {
        UserPresenceInfo removed = onlineUsers.remove(userId);
        if (removed != null) {
            logger.info("User left - UserID: {}, ClientID: {}, Total online: {}", 
                userId, clientId, onlineUsers.size());
        }
    }
    
    /**
     * Get all online users
     */
    public Map<String, UserPresenceInfo> getOnlineUsers() {
        return new ConcurrentHashMap<>(onlineUsers);
    }
    
    /**
     * Check if user is online
     */
    public boolean isUserOnline(String userId) {
        return onlineUsers.containsKey(userId);
    }
    
    /**
     * Get online user count
     */
    public int getOnlineUserCount() {
        return onlineUsers.size();
    }
    
    /**
     * Clear all online users (useful for cleanup)
     */
    public void clear() {
        onlineUsers.clear();
        logger.info("Cleared all online users");
    }
    
    /**
     * Inner class to store user presence information
     */
    public static class UserPresenceInfo {
        private final String userId;
        private final String clientId;
        private final long joinedAt;
        
        public UserPresenceInfo(String userId, String clientId, long joinedAt) {
            this.userId = userId;
            this.clientId = clientId;
            this.joinedAt = joinedAt;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public String getClientId() {
            return clientId;
        }
        
        public long getJoinedAt() {
            return joinedAt;
        }
    }
}
