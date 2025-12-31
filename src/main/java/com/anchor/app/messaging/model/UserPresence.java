package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.messaging.enums.UserPresenceStatus;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_presence")
public class UserPresence {
    
    @Id
    private String userId;
    
    private String status; // ONLINE, OFFLINE, AWAY
    private Date lastSeenAt;
    private Boolean isTyping;
    private String typingInChannelId;
    private Boolean showOnlineStatus;
    private Boolean showLastSeen;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
