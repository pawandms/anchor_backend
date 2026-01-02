package com.anchor.app.msg.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.Channel;

import java.util.Optional;

@Repository
public interface ChannelRepository extends MongoRepository<Channel, String> {
    
    /**
     * Find channel by creator user ID and Self subtype
     * @param crUser User ID
     * @return Optional Channel
     */
    Optional<Channel> findByCrUser(String crUser);
}
