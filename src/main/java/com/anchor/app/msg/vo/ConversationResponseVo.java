package com.anchor.app.msg.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.anchor.app.msg.enums.ConversationType;
import com.anchor.app.msg.model.Conversation;
import com.anchor.app.vo.BaseVo;

public class ConversationResponseVo extends BaseVo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private ConversationType conversationType;
    private String name;
    private String description;
    private String avatarUrl;
    private Date createdDate;
    private Date lastMessageAt;
    private String lastMessageText;
    private Boolean isActive;
    private List<Long> participantIds;
    private boolean isExisting;
    
    public ConversationResponseVo() {
        super();
    }
    
    public ConversationResponseVo(Conversation conversation) {
        super();
        this.id = conversation.getId();
        this.conversationType = conversation.getConversationType();
        this.name = conversation.getName();
        this.description = conversation.getDescription();
        this.avatarUrl = conversation.getAvatarUrl();
        this.createdDate = conversation.getCreatedDate();
        this.lastMessageAt = conversation.getLastMessageAt();
        this.lastMessageText = conversation.getLastMessageText();
        this.isActive = conversation.getIsActive();
        this.isExisting = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
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
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
    
    public List<Long> getParticipantIds() {
        return participantIds;
    }
    
    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }
    
    public boolean isExisting() {
        return isExisting;
    }
    
    public void setExisting(boolean isExisting) {
        this.isExisting = isExisting;
    }
}
