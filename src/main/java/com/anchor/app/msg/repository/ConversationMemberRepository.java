package com.anchor.app.msg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.ConversationMember;

@Repository
public interface ConversationMemberRepository extends MongoRepository<ConversationMember, Integer> {
    
    List<ConversationMember> findByConversationId(Long conversationId);
    
    List<ConversationMember> findByUserId(Long userId);
    
    Optional<ConversationMember> findByConversationIdAndUserId(Long conversationId, Long userId);
    
    @Query("{ 'conversationId': ?0, 'leftAt': null }")
    List<ConversationMember> findActiveMembers(Long conversationId);
    
    @Query("{ 'userId': ?0, 'leftAt': null }")
    List<ConversationMember> findActiveConversationsForUser(Long userId);
    
    Long countByConversationIdAndLeftAtIsNull(Long conversationId);
    
    void deleteByConversationId(Long conversationId);
}
