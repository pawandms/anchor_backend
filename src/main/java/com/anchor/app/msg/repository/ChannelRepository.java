package com.anchor.app.msg.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.Channel;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends MongoRepository<Channel, String> {

    /**
     * Find channel by creator user ID and Self subtype
     * 
     * @param crUser User ID
     * @return Optional Channel
     */
    Optional<Channel> findByCrUser(String crUser);

    /**
     * Find all GROUP channels the user is a member of, excluding 'Self' channels.
     * Uses aggregation to join with channel_members and filter by subType.
     * 
     * @param userId The user ID to find channels for
     * @return List of matching Channel objects
     */
    @Aggregation(pipeline = {
            "{ '$lookup': { 'from': 'channel_members', 'localField': '_id', 'foreignField': 'channelId', 'as': 'members' } }",
            "{ '$match': { 'members.userId': ?0, 'subType': 'GROUP' } }"
    })
    List<Channel> findUserGroupChannels(String userId);
}
