package com.anchor.app.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.anchor.app.users.dto.SignUpRequest;
import com.anchor.app.users.exceptions.ValidationException;
import com.anchor.app.users.service.UserSignUpService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/{version}/public")
public class UserSignUpController {
    
    Logger logger = LoggerFactory.getLogger(UserSignUpController.class);
    
    @Autowired
    private UserSignUpService userSignUpService;
    
    /**
     * User sign up endpoint
     * 
     * @param request SignUpRequest with user details
     * @param bindingResult Validation result
     * @return SignUpRequest with populated response fields
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request, BindingResult bindingResult) {
        
        ResponseEntity<?> response = null;

        logger.info("Received signup request for email: {}", request.getEmail());
        
        try {
            userSignUpService.signUpUser(request, bindingResult);
            logger.info("User signup successful for: {}", request.getEmail());
            response =  ResponseEntity.status(HttpStatus.CREATED).body(request);
            
        } catch (Exception e) {
            if(!request.getErrors().isEmpty())
            {
               response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", "Validation failed",
                "errors", request.getErrors()
            ));
            }
            else {
                response =  new ResponseEntity<>("User creation Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
            }    
            
        }

        return response;
    }
    
    /**
     * Check if email is available
     * 
     * @param email Email to check
     * @return Available status
     */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        logger.info("Checking email availability: {}", email);
        
        boolean exists = userSignUpService.emailExists(email);
        
        return ResponseEntity.ok(Map.of(
            "email", email,
            "available", !exists,
            "message", exists ? "Email already registered" : "Email available"
        ));
    }
}
