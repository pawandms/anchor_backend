package com.anchor.app.msg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.DeliveryStatus;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_message_relation")
public class UserMessageRelation {
    
    @Id
    private String id;
    
    private String userId;
    private String channelId;
    private String messageId;
    private Boolean isVisible;
    private Boolean isDeleted;
    private Date deletedAt;
    private String deliveryStatus; // SENT, DELIVERED, READ
    private Date deliveredAt;
    private Date readAt;
    private Boolean isStarred;
    private Date starredAt;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
