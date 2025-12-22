package com.anchor.app.msg.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anchor.app.model.ApiError;
import com.anchor.app.msg.service.FriendSuggestionService;
import com.anchor.app.msg.vo.FriendSuggestion;

@RestController
@RequestMapping(value = "/api/{version}/friends", version = "v1")
public class FriendSuggestionController {
    
    @Autowired
    private FriendSuggestionService suggestionService;
    
    /**
     * Get friend suggestions based on mutual connections
     * 
     * @param jwt Current user's JWT token
     * @param limit Number of suggestions (default 10, max 50)
     * @return List of suggested friends with mutual connection count
     */
    @GetMapping("/suggestions")
    public ResponseEntity<?> getFriendSuggestions(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            // Get user ID from JWT
            Long userId = jwt.getClaim("userID");
            
            if (userId == null) {
                ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, "User ID not found in token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }
            
            // Validate and cap limit
            if (limit < 1) limit = 10;
            if (limit > 50) limit = 50;
            
            // Get suggestions
            List<FriendSuggestion> suggestions = suggestionService.getSuggestedFriends(userId, limit);
            
            return new ResponseEntity<>(suggestions, HttpStatus.OK);
            
        } catch (Exception e) {
            ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error fetching friend suggestions: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Get mutual friend count between current user and another user
     */
    @GetMapping("/mutual-count")
    public ResponseEntity<?> getMutualFriendCount(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long otherUserId) {
        
        try {
            Long userId = jwt.getClaim("userID");
            
            if (userId == null) {
                ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, "User ID not found in token");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }
            
            int mutualCount = suggestionService.getMutualFriendCount(userId, otherUserId);
            
            return new ResponseEntity<>(Map.of("mutualCount", mutualCount), HttpStatus.OK);
            
        } catch (Exception e) {
            ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error calculating mutual friends: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
