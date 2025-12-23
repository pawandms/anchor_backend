package com.anchor.app.msg.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.enums.ConversationType;
import com.anchor.app.msg.model.Conversation;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, Long> {
    
    List<Conversation> findByCreatedBy(Long createdBy);
    
    List<Conversation> findByConversationType(ConversationType conversationType);
    
    Page<Conversation> findByIsActiveOrderByLastMessageAtDesc(Boolean isActive, Pageable pageable);
    
    @Query("{ 'lastMessageAt': { $gte: ?0, $lte: ?1 } }")
    List<Conversation> findByLastMessageAtBetween(Date startDate, Date endDate);
    
    @Query("{ 'admins': ?0 }")
    List<Conversation> findByAdmin(Long userId);
}
