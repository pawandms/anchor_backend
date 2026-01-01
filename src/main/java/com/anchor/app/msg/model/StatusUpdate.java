package com.anchor.app.msg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.StatusUpdateType;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "status_updates")
public class StatusUpdate {
    
    @Id
    private String id;
    
    private String userId;
    private StatusUpdateType type; // TEXT, IMAGE, VIDEO
    private String content;
    private String mediaId;
    private String backgroundColor;
    private String fontStyle;
    private Date expiresAt;
    private List<Object> privacySettings;
    private Integer viewCount;
    private Boolean isActive;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
