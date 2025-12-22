package com.anchor.app.msg.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.ConversationType;

@Document(collection = "conversations")
public class Conversation {
    
    @Id
    private Integer id;
    
    private ConversationType conversationType;
    
    // Group specific fields
    private String name;
    private String description;
    private String avatarUrl;
    
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;
    
    // Last message info for preview
    private Date lastMessageAt;
    private String lastMessageText;
    
    private Boolean isActive;
    
    // Admin user IDs (for group)
    private List<String> admins;
    
    // Group settings
    private ConversationSettings settings;
    
    public Conversation() {
        this.isActive = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public ConversationType getConversationType() {
        return conversationType;
    }
    
    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Date getLastMessageAt() {
        return lastMessageAt;
    }
    
    public void setLastMessageAt(Date lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
    
    public String getLastMessageText() {
        return lastMessageText;
    }
    
    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public List<String> getAdmins() {
        return admins;
    }
    
    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }
    
    public ConversationSettings getSettings() {
        return settings;
    }
    
    public void setSettings(ConversationSettings settings) {
        this.settings = settings;
    }
    
    // Inner class for settings
    public static class ConversationSettings {
        private Boolean allowMemberAdd;
        private Boolean allowMediaShare;
        private Boolean muteNotifications;
        
        public ConversationSettings() {
            this.allowMemberAdd = true;
            this.allowMediaShare = true;
            this.muteNotifications = false;
        }
        
        public Boolean getAllowMemberAdd() {
            return allowMemberAdd;
        }
        
        public void setAllowMemberAdd(Boolean allowMemberAdd) {
            this.allowMemberAdd = allowMemberAdd;
        }
        
        public Boolean getAllowMediaShare() {
            return allowMediaShare;
        }
        
        public void setAllowMediaShare(Boolean allowMediaShare) {
            this.allowMediaShare = allowMediaShare;
        }
        
        public Boolean getMuteNotifications() {
            return muteNotifications;
        }
        
        public void setMuteNotifications(Boolean muteNotifications) {
            this.muteNotifications = muteNotifications;
        }
    }
}
