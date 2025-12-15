package com.anchor.app.oauth.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ResourceServerTestController {

    @GetMapping("/profile")
    public Map<String, Object> getUserProfile(Authentication authentication) {
        Map<String, Object> profile = new HashMap<>();
        
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtAuth.getToken();
            
            // Extract custom claims from JWT
            profile.put("username", jwt.getSubject());
            profile.put("userID", jwt.getClaimAsString("userID"));
            profile.put("userTypeID", jwt.getClaimAsString("userTypeID"));
            profile.put("clientID", jwt.getClaimAsString("clientID"));
            profile.put("roleID", jwt.getClaim("roleID"));
            profile.put("authorities", jwt.getClaim("authorities"));
            profile.put("tokenType", "JWT");
        } else {
            profile.put("username", authentication.getName());
            profile.put("authorities", authentication.getAuthorities());
            profile.put("tokenType", "Session");
        }
        
        return profile;
    }

    @GetMapping("/protected")
    public Map<String, Object> getProtectedResource(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a protected resource");
        response.put("user", principal.getName());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/public/info")
    public Map<String, Object> getPublicInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a public endpoint");
        response.put("timestamp", System.currentTimeMillis());
        response.put("authRequired", false);
        return response;
    }
}
