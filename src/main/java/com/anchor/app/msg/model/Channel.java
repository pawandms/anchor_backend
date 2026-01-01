package com.anchor.app.msg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.ChannelSubType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.ChannelVisibility;
import com.anchor.app.msg.enums.SubscriptionType;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "channels")
public class Channel {
    
    @Id
    private String id;
    
    private ChannelType type; // Messaging, Video, Audio, Blog
    private ChannelSubType subType; // ONE_TO_ONE or GROUP
    private ChannelVisibility visibility; // PUBLIC, PRIVATE, PROTECTED
    private SubscriptionType subscriptionType; // FREE, MONTHLY_SUBSCRIPTION, DAILY_SUBSCRIPTION, PAY_PER_VIEW
    private String name;
    private String description;
    private String avatarMediaId;
    private Date lastActivityAt;
    private String lastActivityText;
    private String lastActivityType;
    private Boolean isActive;
    private ChannelSettings channelSettings;
    private Boolean disappearingMessages;
    private Integer disappearingMessagesDuration;
    private Integer totalMembers;
    private Boolean isArchived;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
