package com.anchor.app.oauth.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler 
{
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    
    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        logger.error("Authentication failure occurred: {}", exception.getMessage(), exception);
        
        // Check if it's an OAuth2 authentication exception
        if (exception instanceof OAuth2AuthenticationException) {
            handleOAuth2AuthenticationException(request, response, (OAuth2AuthenticationException) exception);
        } else {
            handleGeneralAuthenticationException(request, response, exception);
        }
    }
    
    private void handleOAuth2AuthenticationException(HttpServletRequest request, HttpServletResponse response,
            OAuth2AuthenticationException exception) throws IOException {
        
        OAuth2Error error = exception.getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);

        // Create enhanced error response with additional details
        OAuth2Error errorResponse = new OAuth2Error(
            error.getErrorCode(),
            error.getDescription(),
            error.getUri()
        );
        
        this.errorHttpResponseConverter.write(errorResponse, null, httpResponse);
    }
    
    private void handleGeneralAuthenticationException(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        Map<String, Object> errorResponse = new HashMap<>();
        
        if (exception instanceof BadCredentialsException) {
            errorResponse.put("error", "invalid_credentials");
            errorResponse.put("error_description", "Invalid username or password");
        } else if (exception instanceof DisabledException) {
            errorResponse.put("error", "account_disabled");
            errorResponse.put("error_description", "User account is disabled");
        } else if (exception instanceof LockedException) {
            errorResponse.put("error", "account_locked");
            errorResponse.put("error_description", "User account is locked");
        } else {
            errorResponse.put("error", "authentication_failed");
            errorResponse.put("error_description", "Authentication failed: " + exception.getMessage());
        }
        
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("path", request.getRequestURI());
        
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
