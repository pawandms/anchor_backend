package com.anchor.app.oauth.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/public/token")
public class TokenDebugController {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Debug endpoint to decode JWT token and show header/payload
     * 
     * @param token JWT token to decode
     * @return Decoded token information
     */
    @PostMapping("/debug")
    public Map<String, Object> debugToken(@RequestBody Map<String, String> request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String token = request.get("token");
            if (token == null || token.isEmpty()) {
                result.put("error", "Token is required");
                return result;
            }
            
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                result.put("error", "Invalid JWT format");
                return result;
            }
            
            // Decode header
            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            Map<String, Object> header = objectMapper.readValue(headerJson, Map.class);
            result.put("header", header);
            
            // Decode payload
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);
            result.put("payload", payload);
            
            result.put("success", true);
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("success", false);
        }
        
        return result;
    }
}
