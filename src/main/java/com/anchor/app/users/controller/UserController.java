package com.anchor.app.users.controller;

import com.anchor.app.media.dto.StreamMediaInfo;
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
    
}
