package com.anchor.app.msg.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.MemberRole;

@Document(collection = "conversation_members")
@CompoundIndex(name = "conv_user_idx", def = "{'conversationId': 1, 'userId': 1}", unique = true)
public class ConversationMember {
    
    @Id
    private Integer id;
    
    private String conversationId;
    private String userId;
    private MemberRole role;
    
    private Date joinedAt;
    
    // For tracking unread messages
    private String lastReadMessageId;
    private Date lastReadAt;
    
    // User preferences
    private Boolean isMuted;
    private Boolean isPinned;
    
    // Track if member left
    private Date leftAt;
    
    // Who added this member
    private String addedBy;
    
    public ConversationMember() {
        this.role = MemberRole.MEMBER;
        this.joinedAt = new Date();
        this.isMuted = false;
        this.isPinned = false;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    
    public MemberRole getRole() {
        return role;
    }
    
    public void setRole(MemberRole role) {
        this.role = role;
    }
    
    public Date getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }
    
    public String getLastReadMessageId() {
        return lastReadMessageId;
    }
    
    public void setLastReadMessageId(String lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
    
    public Date getLastReadAt() {
        return lastReadAt;
    }
    
    public void setLastReadAt(Date lastReadAt) {
        this.lastReadAt = lastReadAt;
    }
    
    public Boolean getIsMuted() {
        return isMuted;
    }
    
    public void setIsMuted(Boolean isMuted) {
        this.isMuted = isMuted;
    }
    
    public Boolean getIsPinned() {
        return isPinned;
    }
    
    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }
    
    public Date getLeftAt() {
        return leftAt;
    }
    
    public void setLeftAt(Date leftAt) {
        this.leftAt = leftAt;
    }
    
    public String getAddedBy() {
        return addedBy;
    }
    
    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
}
