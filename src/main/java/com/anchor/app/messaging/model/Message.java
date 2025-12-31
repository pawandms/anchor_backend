package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.messaging.enums.MessageType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {
    
    @Id
    private String id;
    
    private String channelId;
    private String senderId;
    private String senderName;
    private String messageType; // TEXT, IMAGE, VIDEO, AUDIO, VOICE, DOCUMENT, LOCATION, CONTACT, STICKER, GIF, POLL, SYSTEM
    private String content;
    private List<String> mediaIds;
    private Location location;
    private List<Contact> contacts;
    private Poll poll;
    private ReplyTo replyTo;
    private ForwardedFrom forwardedFrom;
    private List<Mention> mentions;
    private Boolean isEdited;
    private Date editedAt;
    private Boolean deletedForEveryone;
    private Date deletedAt;
    private String deletedBy;
    private Boolean isEncrypted;
    private String encryptionVersion;
    private Map<String, Object> metadata;
    private Date expiresAt;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
