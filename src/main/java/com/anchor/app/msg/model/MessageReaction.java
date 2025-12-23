package com.anchor.app.msg.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.MsgReactionType;

@Document(collection = "message_reactions")
@CompoundIndex(name = "msg_user_idx", def = "{'messageId': 1, 'userId': 1}", unique = true)
public class MessageReaction {
    
    @Id
    private Long id;
    
    @Indexed
    private String messageId;
    
    private Long conversationId;
    
    private Long userId;
    
    private MsgReactionType reactionType;
    
    private Date createdAt;
    
    public MessageReaction() {
        this.createdAt = new Date();
    }
    
    // Getters and Setters
    
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    
    public MsgReactionType getReactionType() {
        return reactionType;
    }
    
    public void setReactionType(MsgReactionType reactionType) {
        this.reactionType = reactionType;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    
}
