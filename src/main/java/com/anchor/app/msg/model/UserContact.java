package com.anchor.app.msg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.ContactType;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_contacts")
public class UserContact {
    
    @Id
    private String id;
    private String userId;
    private String contactId;
    private ContactType contactType; // FRIEND, BLOCKED
    private String oneToOneChannelId;
    private Boolean isFavorite;
    private String customName;
    private Integer interactionScore;
    private Date lastInteractionAt;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
