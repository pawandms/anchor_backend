package com.anchor.app.msg.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.ChannelMember;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelMemberRepository extends MongoRepository<ChannelMember, String> {
    
    /**
     * Find all channel members by channel ID
     * @param channelId Channel ID
     * @return List of ChannelMembers
     */
    List<ChannelMember> findByChannelId(String channelId);
    
    /**
     * Find all channel members by user ID
     * @param userId User ID
     * @return List of ChannelMembers
     */
    List<ChannelMember> findByUserId(String userId);
    
    /**
     * Find specific channel member by channel ID and user ID
     * @param channelId Channel ID
     * @param userId User ID
     * @return Optional ChannelMember
     */
    Optional<ChannelMember> findByChannelIdAndUserId(String channelId, String userId);
    
    /**
     * Check if channel member exists
     * @param channelId Channel ID
     * @param userId User ID
     * @return true if exists
     */
    boolean existsByChannelIdAndUserId(String channelId, String userId);
}
