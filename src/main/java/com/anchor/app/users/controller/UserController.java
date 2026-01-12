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
    
    
    
}
