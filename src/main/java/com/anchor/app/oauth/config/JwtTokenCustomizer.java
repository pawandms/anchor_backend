package com.anchor.app.oauth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	

    @Override
    public void customize(JwtEncodingContext context) {

        
        // Only customize JWT access tokens, not ID tokens
        if ("access_token".equals(context.getTokenType().getValue())) {
            JwtClaimsSet.Builder claims = context.getClaims();
            
            // Get the authenticated user
            Authentication authentication = context.getPrincipal();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                logger.info("JwtToken Customizer Called for UserName:"+username);        
                try {
                    // Get Internal User Details for token claims
                    /* 
                    User loggedInUser = userService.getActiveUserByUsername(username);
                    if (loggedInUser != null) {
                        List<Integer> roleArray = new ArrayList<>();
                        loggedInUser.getUserRole().forEach(userRole -> roleArray.add(userRole.getRoleId()));
                        
                        // Add custom claims to the JWT
                        claims.claim("userID", loggedInUser.getUserID().toString());
                        claims.claim("userTypeID", loggedInUser.getUserTypeID().toString());
                        claims.claim("roleID", roleArray);
                        claims.claim("clientID", loggedInUser.getClientID().toString());
                    }
                    */
                } catch (Exception e) {
                    // Log the error but don't fail token generation
                    // You might want to add proper logging here
                    System.err.println("Error adding custom claims to JWT: " + e.getMessage());
                }
            }
        }
    }
}
