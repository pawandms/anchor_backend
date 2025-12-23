package com.anchor.app.msg.vo;

import java.util.ArrayList;
import java.util.List;

import com.anchor.app.msg.enums.ConversationType;
import com.anchor.app.vo.BaseVo;
import com.anchor.app.vo.MediaImage;

public class CreateConversationVo extends BaseVo {
    
    private Long userId;
    private List<Long> participantIds = new ArrayList<>();
   
    private ConversationType type;
    // Group specific fields (optional)
    private String name;
    private String description;
    private MediaImage media;
    
    // Calculated Field
    private boolean checkSelfChannel;


    public CreateConversationVo() {
        super();
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public List<Long> getParticipantIds() {
        return participantIds;
    }
    
    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
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

    public MediaImage getMedia() {
        return media;
    }

    public void setMedia(MediaImage media) {
        this.media = media;
    }

    public ConversationType getType() {
        return type;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }

    public boolean isCheckSelfChannel() {
        return checkSelfChannel;
    }

    public void setCheckSelfChannel(boolean checkSelfChannel) {
        this.checkSelfChannel = checkSelfChannel;
    }
    
        
}
