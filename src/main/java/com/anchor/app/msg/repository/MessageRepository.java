package com.anchor.app.msg.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.enums.MessageStatus;
import com.anchor.app.msg.enums.MessageType;
import com.anchor.app.msg.model.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    
    Page<Message> findByConversationIdOrderByCreatedAtDesc(String conversationId, Pageable pageable);
    
    List<Message> findBySenderId(String senderId);
    
    List<Message> findByConversationIdAndSenderId(String conversationId, String senderId);
    
    Page<Message> findByConversationIdAndIsDeletedOrderByCreatedAtDesc(
        String conversationId, Boolean isDeleted, Pageable pageable);
    
    List<Message> findByConversationIdAndMessageType(String conversationId, MessageType messageType);
    
    @Query("{ 'conversationId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    List<Message> findByConversationIdAndCreatedAtBetween(
        String conversationId, Date startDate, Date endDate);
    
    @Query("{ 'conversationId': ?0, 'status': ?1 }")
    List<Message> findByConversationIdAndStatus(String conversationId, MessageStatus status);
    
    @Query("{ 'replyTo.messageId': ?0 }")
    List<Message> findRepliesByOriginalMessageId(String originalMessageId);
    
    @Query("{ 'isEncrypted': ?0 }")
    Page<Message> findByIsEncrypted(Boolean isEncrypted, Pageable pageable);
    
    Long countByConversationId(String conversationId);
    
    Long countByConversationIdAndIsDeleted(String conversationId, Boolean isDeleted);
    
    void deleteByConversationId(String conversationId);
}
