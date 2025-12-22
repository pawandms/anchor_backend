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
    private Integer id;
    
    @Indexed
    private String messageId;
    
    private String conversationId;
    
    private String userId;
    
    private MsgReactionType reactionType;
    
    private Date createdAt;
    
    public MessageReaction() {
        this.createdAt = new Date();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
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
}
