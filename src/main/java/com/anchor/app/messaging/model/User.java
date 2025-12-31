package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String profileImageMediaId;
    private String about;
    private String publicKey;
    private Map<String, Object> privacySettings;
    private Map<String, Object> notificationSettings;
    private Boolean isTwoStepVerificationEnabled;
    private String language;
    private Date lastActiveAt;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
