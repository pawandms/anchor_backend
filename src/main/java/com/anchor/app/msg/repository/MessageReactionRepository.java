package com.anchor.app.msg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.enums.MsgReactionType;
import com.anchor.app.msg.model.MessageReaction;

@Repository
public interface MessageReactionRepository extends MongoRepository<MessageReaction, Integer> {
    
    List<MessageReaction> findByMessageId(String messageId);
    
    List<MessageReaction> findByConversationId(String conversationId);
    
    Optional<MessageReaction> findByMessageIdAndUserId(String messageId, String userId);
    
    List<MessageReaction> findByMessageIdAndReactionType(String messageId, MsgReactionType reactionType);
    
    Long countByMessageId(String messageId);
    
    Long countByMessageIdAndReactionType(String messageId, MsgReactionType reactionType);
    
    void deleteByMessageId(String messageId);
    
    void deleteByMessageIdAndUserId(String messageId, String userId);
}
