package com.anchor.app.users.controller;

import com.anchor.app.media.dto.StreamMediaInfo;
import com.anchor.app.users.dto.UserInfoUpdateRequest;
import com.anchor.app.users.dto.UserNotification;
import com.anchor.app.users.dto.UserPrivacy;
import com.anchor.app.users.dto.UserProfile;
import com.anchor.app.users.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/{version}/users/")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * Upload or update user profile image
     * 
     * @param userId User ID
     * @param file Profile image file
     * @return ProfileImageResponse with upload details
     */
    /**
     * Get user profile by user ID
     * 
     * @param userId User ID from path
     * @param request UserProfile request object for authorization and validation
     * @return User profile information
     */
    @GetMapping(value = "{userId}/profile", 
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserProfile(
            @PathVariable String userId) 
        {
                ResponseEntity<?> response = null;
                UserProfile req = null;
        try {
            logger.info("Fetching user profile for userId: {}", userId);
            
            req = new UserProfile();
            req.setId(userId);
            
            userService.getUserProfileById(req);
            
            if(req.isValid())
            {
              response =  ResponseEntity.status(HttpStatus.OK).body(req);      
            }   

           
            
        } catch (Exception e) {
            if(!req.getErrors().isEmpty())
            {
               response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", "Validation failed",
                "errors", req.getErrors()
            ));
            }
            else {
                response =  new ResponseEntity<>("get User Profile Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
            }
        
        }

        return response;
    }
    
    /**
     * Upload or update user profile image
     * 
     * @param userId User ID
     * @param file Profile image file
     * @return ProfileImageResponse with upload details
     */
    @PostMapping(value = "{userId}/media/profile-image", 
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable String userId,
            @RequestParam(value = "file", required = true) MultipartFile file) {
        
          ResponseEntity<?> response = null;
          StreamMediaInfo req = null;
        try {
            req = new StreamMediaInfo();
            req.setUserID(userId);
            req.setInputFile(file);
            
            // Upload file to MinIO
            userService.addUpdateUserProfileImage(req);
            if(req.isValid())
            {
                response =  ResponseEntity.status(HttpStatus.CREATED).body(req);    
            }   
            
            
        } catch (Exception e) {
            
            if(!req.getErrors().isEmpty())
            {
               response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", "Validation failed",
                "errors", req.getErrors()
            ));
            }
            else {
                response =  new ResponseEntity<>("set User Profile Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
            }
        
        }

        return response;
    }
    
    /**
     * Update user privacy settings
     * 
     * @param userId User ID from path
     * @param privacySettings UserPrivacy object containing privacy settings to update
     * @return Updated privacy settings confirmation
     */
    @PostMapping(value = "{userId}/privacy-settings",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePrivacySettings(
            @PathVariable String userId,
            @RequestBody UserPrivacy privacySettings) {
        
        ResponseEntity<?> response = null;
        
        try {
            logger.info("Updating privacy settings for userId: {}", userId);
            
            // Update privacy settings
            userService.updateUserPrivacySettings(userId, privacySettings);
            
            // Build success response
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Privacy settings updated successfully");
            successResponse.put("userId", userId);
            successResponse.put("privacySettings", privacySettings);
            
            response = ResponseEntity.status(HttpStatus.OK).body(successResponse);
            
        } catch (Exception e) {
            logger.error("Error updating privacy settings for userId: {}", userId, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to update privacy settings");
            errorResponse.put("error", e.getMessage());
            
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        
        return response;
    }
    
    /**
     * Update user notification settings
     * 
     * @param userId User ID from path
     * @param notificationSettings UserNotification object containing notification settings to update
     * @return Updated notification settings confirmation
     */
    @PostMapping(value = "{userId}/notification-settings",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateNotificationSettings(
            @PathVariable String userId,
            @RequestBody UserNotification notificationSettings) {
        
        ResponseEntity<?> response = null;
        
        try {
            logger.info("Updating notification settings for userId: {}", userId);
            
            // Update notification settings
            userService.updateUserNotificationSettings(userId, notificationSettings);
            
            // Build success response
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "Notification settings updated successfully");
            successResponse.put("userId", userId);
            successResponse.put("notificationSettings", notificationSettings);
            
            response = ResponseEntity.status(HttpStatus.OK).body(successResponse);
            
        } catch (Exception e) {
            logger.error("Error updating notification settings for userId: {}", userId, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to update notification settings");
            errorResponse.put("error", e.getMessage());
            
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        
        return response;
    }
    
    /**
     * Update user info (firstName, lastName, nickName, mobile, email)
     * Only updates fields that are provided (not null)
     * When email is updated, userName is also updated with the same value
     * 
     * @param userId User ID from path
     * @param userInfoRequest UserInfoUpdateRequest containing fields to update
     * @return Updated user info confirmation
     */
    @PostMapping(value = "{userId}/user-info",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserInfo(
            @PathVariable String userId,
            @RequestBody UserInfoUpdateRequest userInfoRequest) {
        
        ResponseEntity<?> response = null;
        
        try {
            logger.info("Updating user info for userId: {}", userId);
            
            // Update user info
            userService.updateUserInfo(userId, userInfoRequest);
            
            if (userInfoRequest.isValid()) {
                // Build success response
                Map<String, Object> successResponse = new HashMap<>();
                successResponse.put("success", true);
                successResponse.put("message", "User info updated successfully");
                successResponse.put("userId", userId);
                successResponse.put("updatedFields", userInfoRequest);
                
                response = ResponseEntity.status(HttpStatus.OK).body(successResponse);
            }
            
        } catch (Exception e) {
            logger.error("Error updating user info for userId: {}", userId, e);
            
            if (!userInfoRequest.getErrors().isEmpty()) {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "Validation failed",
                    "errors", userInfoRequest.getErrors()
                ));
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Failed to update user info");
                errorResponse.put("error", e.getMessage());
                
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
        
        return response;
    }
    
}
