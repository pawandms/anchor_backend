package com.anchor.app.msg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "message_reactions")
public class MessageReaction {
    
    @Id
    private String id;
    
    private String messageId;
    private String channelId;
    private String mediaId;
    private String userId;
    private String emoji;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
