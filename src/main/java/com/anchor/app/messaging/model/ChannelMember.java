package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.messaging.enums.ChannelMemberRole;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "channel_members")
public class ChannelMember {
    
    @Id
    private String id;
    
    private String channelId;
    private String userId;
    private ChannelMemberRole role; // ADMIN, MEMBER, SUPER_ADMIN
    private Date validFrom;
    private Date validTo;
    private String lastReadMessageId;
    private Date lastReadAt;
    private Integer unreadCount;
    private Boolean isMuted;
    private Date mutedUntil;
    private Boolean isPinned;
    private Integer pinOrder;
    private Boolean isHidden;
    private Boolean isArchived;
    private String addedBy;
    private String removedBy;
    private List<Object> customNotificationSettings;
    private Boolean receiveNotifications;
    private String notificationSound;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
