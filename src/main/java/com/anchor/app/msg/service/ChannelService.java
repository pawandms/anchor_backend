package com.anchor.app.msg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.msg.enums.ChannelMemberRole;
import com.anchor.app.msg.enums.ChannelSubType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.ChannelVisibility;
import com.anchor.app.msg.enums.SubscriptionType;
import com.anchor.app.msg.exceptions.ChannelServiceException;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.model.ChannelMember;
import com.anchor.app.msg.repository.ChannelMemberRepository;
import com.anchor.app.msg.repository.ChannelRepository;
import com.anchor.app.util.HelperBean;
import com.anchor.app.util.enums.SequenceType;

import java.util.Date;

@Service
public class ChannelService {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    
    @Autowired
    private HelperBean helperBean;
    
    /**
     * Create a Self channel for a user during signup
     * 
     * @param userId User ID
     * @param userName User name
     * @return Channel ID
     * @throws ChannelServiceException if channel creation fails
     */
    public String createSelfChannel(String userId, String userName) throws ChannelServiceException {
        try {
            Date now = new Date();
            String channelId = helperBean.getSequanceNo(SequenceType.Channel);
            
            // Create Self Channel
            Channel selfChannel = Channel.builder()
                .id(channelId)
                .type(ChannelType.Messaging)
                .subType(ChannelSubType.Self)
                .visibility(ChannelVisibility.PRIVATE)
                .subscriptionType(SubscriptionType.FREE)
                .name("Self Channel")
                .description("Personal self channel for " + userName)
                .lastActivityAt(now)
                .isActive(true)
                .disappearingMessages(false)
                .totalMembers(1)
                .isArchived(false)
                .crUser(userId)
                .crDate(now)
                .modUser(userId)
                .modDate(now)
                .build();
            
            channelRepository.save(selfChannel);
            logger.info("Self channel created with ID: {} for user: {}", channelId, userId);
            
            // Create ChannelMember record
            String memberId = helperBean.getSequanceNo(SequenceType.ChannelMember);
            
            ChannelMember channelMember = ChannelMember.builder()
                .id(memberId)
                .channelId(channelId)
                .userId(userId)
                .role(ChannelMemberRole.ADMIN)
                .validFrom(now)
                .validTo(helperBean.getInfiniteDate())
                .lastReadAt(now)
                .unreadCount(0)
                .isMuted(false)
                .isPinned(false)
                .pinOrder(0)
                .isHidden(false)
                .isArchived(false)
                .addedBy(userId)
                .receiveNotifications(true)
                .crUser(userId)
                .crDate(now)
                .modUser(userId)
                .modDate(now)
                .build();
            
            channelMemberRepository.save(channelMember);
            logger.info("ChannelMember created with ID: {} for channel: {} and user: {}", memberId, channelId, userId);
            
            return channelId;
            
        } catch (Exception e) {
            logger.error("Error creating self channel for user {}: {}", userId, e.getMessage(), e);
            throw new ChannelServiceException("Failed to create self channel: " + e.getMessage(), e);
        }
    }
}
