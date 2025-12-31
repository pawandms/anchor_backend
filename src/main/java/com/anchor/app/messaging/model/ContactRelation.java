package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.messaging.enums.RelationshipType;
import com.anchor.app.messaging.enums.ContactStatus;
import com.anchor.app.messaging.enums.ContactSource;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contacts")
public class ContactRelation {
    
    @Id
    private String id;
    
    private String userId;
    private String contactUserId;
    private RelationshipType relationshipType; // FRIEND, SUGGESTION, REJECTED, BLOCKED_BY_ME, BLOCKED_BY_THEM
    private ContactStatus status; // SUGGESTED, PENDING_SENT, PENDING_RECEIVED, ACCEPTED, REJECTED
    private String requestSentBy;
    private ContactSource source; // MUTUAL_FRIEND, CHANNEL_MEMBER, PHONE_CONTACT, MANUAL
    private String sourceChannelId;
    private List<String> mutualFriends;
    private Integer mutualFriendCount;
    private List<String> sharedChannels;
    private Integer interactionScore;
    private Date lastInteractionAt;
    private Boolean isFavorite;
    private String customName;
    private String oneToOneChannelId;
    private Date requestSentAt;
    private Date acceptedAt;
    private Date rejectedAt;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
