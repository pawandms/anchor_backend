package com.anchor.app.users.controller;

import com.anchor.app.media.model.Media;
import com.anchor.app.media.service.MediaService;
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
    @PostMapping(value = "{userId}/media/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        
          ResponseEntity<?> response = null;
        try {
            
            // Upload file to MinIO
            String mediaID  = userService.addUpdateUserProfileImage(userId, file);
            
            // Create response body
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("profileImageID", mediaID);
            
            response =  ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
            
        } catch (Exception e) {
             response =  new ResponseEntity<>("User profile Update  Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
        }

        return response;
    }
    
    
    /**
     * Get user profile image URL
     * 
     * @param userId User ID
     * @return ProfileImageResponse with image URL
     */
    
    /*
    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<?> getProfileImage(@PathVariable String userId) {
        
        logger.info("Received profile image retrieval request for user: {}", userId);
        
        try {
            Media media = mediaService.getUserProfileImageMetadata(userId);
            
            if (media == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ProfileImageResponse.builder()
                        .success(false)
                        .message("Profile image not found for user")
                        .build());
            }
            
            String imageUrl = mediaService.getUserProfileImageUrl(userId);
            
            ProfileImageResponse response = ProfileImageResponse.builder()
                    .mediaId(media.getId())
                    .userId(userId)
                    .imageUrl(imageUrl)
                    .fileName(media.getFileName())
                    .fileSize(media.getFileSize())
                    .mimeType(media.getMimeType() != null ? media.getMimeType().name() : null)
                    .message("Profile image retrieved successfully")
                    .success(true)
                    .build();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving profile image for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ProfileImageResponse.builder()
                    .success(false)
                    .message("Failed to retrieve profile image: " + e.getMessage())
                    .build());
        }
    }
    
     */
    /**
     * Delete user profile image
     * 
     * @param userId User ID
     * @return Response with deletion status
     */

    /* 
    @DeleteMapping("/{userId}/profile-image")
    public ResponseEntity<?> deleteProfileImage(@PathVariable String userId) {
        
        logger.info("Received profile image deletion request for user: {}", userId);
        
        try {
            mediaService.deleteUserProfileImage(userId);
            
            logger.info("Profile image deleted successfully for user: {}", userId);
            
            return ResponseEntity.ok(ProfileImageResponse.builder()
                    .userId(userId)
                    .message("Profile image deleted successfully")
                    .success(true)
                    .build());
            
        } catch (Exception e) {
            logger.error("Error deleting profile image for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ProfileImageResponse.builder()
                    .success(false)
                    .message("Failed to delete profile image: " + e.getMessage())
                    .build());
        }
    }
    
    */
    
}
