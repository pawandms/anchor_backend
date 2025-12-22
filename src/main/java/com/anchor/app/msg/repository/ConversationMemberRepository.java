package com.anchor.app.msg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.ConversationMember;

@Repository
public interface ConversationMemberRepository extends MongoRepository<ConversationMember, Integer> {
    
    List<ConversationMember> findByConversationId(String conversationId);
    
    List<ConversationMember> findByUserId(String userId);
    
    Optional<ConversationMember> findByConversationIdAndUserId(String conversationId, String userId);
    
    @Query("{ 'conversationId': ?0, 'leftAt': null }")
    List<ConversationMember> findActiveMembers(String conversationId);
    
    @Query("{ 'userId': ?0, 'leftAt': null }")
    List<ConversationMember> findActiveConversationsForUser(String userId);
    
    Long countByConversationIdAndLeftAtIsNull(String conversationId);
    
    void deleteByConversationId(String conversationId);
}
