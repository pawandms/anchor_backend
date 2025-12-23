package com.anchor.app.msg.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.exceptions.ValidationException;
import com.anchor.app.msg.enums.ConversationType;
import com.anchor.app.msg.enums.MemberRole;
import com.anchor.app.msg.model.Conversation;
import com.anchor.app.msg.model.ConversationMember;
import com.anchor.app.msg.repository.ConversationMemberRepository;
import com.anchor.app.msg.repository.ConversationRepository;
import com.anchor.app.msg.vo.ConversationResponseVo;
import com.anchor.app.msg.vo.CreateConversationVo;
import com.anchor.app.sequencer.service.Sequencer;
import com.anchor.app.vo.ErrorMsg;

@Service
public class ConversationService {
    
    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private ConversationMemberRepository conversationMemberRepository;
    
    /**
     * Validate create conversation request
     * @param vo the request to validate
     * @return ConversationResponseVo with error details if validation fails, null if valid
     * @throws ValidationException 
     */
    
    private void validateCreateConversationRequest(CreateConversationVo vo) throws ValidationException 
    {
        

        vo.setValid(true);
        try{
            // Step 1 : Strucutural Validation
            createConversationStructuralValidation(vo);

            // Step 2 : Business Validation

        }
        catch(Exception e)
        {
            throw new ValidationException(e.getMessage(), e);
        }    
       
        
        
      
    }
    

    private void createConversationStructuralValidation(CreateConversationVo request) throws ValidationException
    {
        try{

            if (request.getUserId() == null) 
            {
            request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Action_User.name(), ValidationErrorType.Invalid_Action_User.getValue()));
            }
            
            if(request.getParticipantIds().isEmpty())
            {
                // Qualify for Checking User having Existing Self Channel Or Not 
                request.setCheckSelfChannel(true);
            }   
        
        }
        catch(Exception e)
        {
            throw new ValidationException(e.getMessage(), e);
        }
    }

    private void createConversationBusinessValidation(CreateConversationVo request) throws ValidationException
    {
        try{

            if (request.getUserId() == null) 
            {
            request.setValid(false);
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Action_User.name(), ValidationErrorType.Invalid_Action_User.getValue()));
            }
            
            if(request.getParticipantIds().isEmpty())
            {
                // Qualify for Checking User having Existing Self Channel Or Not 
                request.setCheckSelfChannel(true);
            }   
        
        }
        catch(Exception e)
        {
            throw new ValidationException(e.getMessage(), e);
        }
    }


    /**
     * Create or get existing conversation
     * If a conversation with exact same participants exists, return it
     * Otherwise create a new conversation
     */
    @Transactional
    public ConversationResponseVo createOrGetConversation(CreateConversationVo vo) {
        
        // Step 1:  Validate request
        ConversationResponseVo validationError = validateRequest(vo);
        if (validationError != null) {
            return validationError;
        }
        
        Long currentUserId = vo.getUserId();
        ConversationType conversationType = vo.getConversationType();
        
        // Add current user to participants if not already included
        Set<Long> allParticipants = new HashSet<>(vo.getParticipantIds());
        allParticipants.add(currentUserId);
        
        // For one-to-one conversations, check if conversation already exists
        if (conversationType == ConversationType.ONE_TO_ONE) {
            Optional<Conversation> existingConversation = findExistingOneToOneConversation(
                currentUserId, 
                vo.getParticipantIds().get(0)
            );
            
            if (existingConversation.isPresent()) {
                ConversationResponseVo response = new ConversationResponseVo(existingConversation.get());
                response.setExisting(true);
                
                // Get participant IDs
                List<Long> participantIds = conversationMemberRepository
                    .findByConversationId(existingConversation.get().getId())
                    .stream()
                    .map(ConversationMember::getUserId)
                    .collect(Collectors.toList());
                response.setParticipantIds(participantIds);
                
                return response;
            }
        }
        
        // For group conversations, check if exact same group exists
        if (conversationType == ConversationType.GROUP) {
            Optional<Conversation> existingConversation = findExistingGroupConversation(allParticipants);
            
            if (existingConversation.isPresent()) {
                ConversationResponseVo response = new ConversationResponseVo(existingConversation.get());
                response.setExisting(true);
                
                // Get participant IDs
                List<Long> participantIds = conversationMemberRepository
                    .findByConversationId(existingConversation.get().getId())
                    .stream()
                    .map(ConversationMember::getUserId)
                    .collect(Collectors.toList());
                response.setParticipantIds(participantIds);
                
                return response;
            }
        }
        
        // Create new conversation
        Conversation conversation = new Conversation();
        conversation.setId(Sequencer.getNextSequence(Sequencer.CONVERSATION_SEQ));
        conversation.setConversationType(conversationType);
        conversation.setName(vo.getName());
        conversation.setDescription(vo.getDescription());
        conversation.setAvatarUrl(vo.getAvatarUrl());
        conversation.setCreatedBy(currentUserId);
        conversation.setCreatedDate(new Date());
        conversation.setModifiedBy(currentUserId);
        conversation.setModifiedDate(new Date());
        conversation.setIsActive(true);
        
        // Save conversation
        conversation = conversationRepository.save(conversation);
        
        // Create conversation members
        List<ConversationMember> members = new ArrayList<>();
        for (Long participantId : allParticipants) {
            ConversationMember member = new ConversationMember();
            member.setId(Sequencer.getNextSequence(Sequencer.CONVERSATION_MEMBER_SEQ).intValue());
            member.setConversationId(conversation.getId());
            member.setUserId(participantId);
            
            // Set creator as admin for groups
            if (conversationType == ConversationType.GROUP && participantId.equals(currentUserId)) {
                member.setRole(MemberRole.ADMIN);
            } else {
                member.setRole(MemberRole.MEMBER);
            }
            
            member.setJoinedAt(new Date());
            member.setCreatedBy(currentUserId);
            member.setCreatedDate(new Date());
            member.setModifiedBy(currentUserId);
            member.setModifiedDate(new Date());
            
            members.add(member);
        }
        
        conversationMemberRepository.saveAll(members);
        
        // Prepare response
        ConversationResponseVo response = new ConversationResponseVo(conversation);
        response.setExisting(false);
        response.setParticipantIds(new ArrayList<>(allParticipants));
        
        return response;
    }
    
    /**
     * Find existing one-to-one conversation between two users
     */
    private Optional<Conversation> findExistingOneToOneConversation(Long userId1, Long userId2) {
        // Get all active conversations for user1
        List<ConversationMember> user1Conversations = conversationMemberRepository
            .findActiveConversationsForUser(userId1);
        
        // Get all active conversations for user2
        List<ConversationMember> user2Conversations = conversationMemberRepository
            .findActiveConversationsForUser(userId2);
        
        // Find common conversation IDs
        Set<Long> user1ConvIds = user1Conversations.stream()
            .map(ConversationMember::getConversationId)
            .collect(Collectors.toSet());
        
        Set<Long> user2ConvIds = user2Conversations.stream()
            .map(ConversationMember::getConversationId)
            .collect(Collectors.toSet());
        
        user1ConvIds.retainAll(user2ConvIds);
        
        // Check if any of these conversations are one-to-one with exactly 2 members
        for (Long convId : user1ConvIds) {
            List<ConversationMember> members = conversationMemberRepository
                .findActiveMembers(convId);
            
            if (members.size() == 2) {
                Optional<Conversation> conv = conversationRepository.findById(convId);
                if (conv.isPresent() && conv.get().getConversationType() == ConversationType.ONE_TO_ONE) {
                    return conv;
                }
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Find existing group conversation with exact same participants
     */
    private Optional<Conversation> findExistingGroupConversation(Set<Long> participantIds) {
        // Get all group conversations
        List<Conversation> groupConversations = conversationRepository
            .findByConversationType(ConversationType.GROUP);
        
        // Check each conversation for exact participant match
        for (Conversation conv : groupConversations) {
            List<ConversationMember> members = conversationMemberRepository
                .findActiveMembers(conv.getId());
            
            Set<Long> existingParticipants = members.stream()
                .map(ConversationMember::getUserId)
                .collect(Collectors.toSet());
            
            // Check if participant sets match exactly
            if (existingParticipants.equals(participantIds)) {
                return Optional.of(conv);
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Get conversation by ID
     */
    public Optional<Conversation> getConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId);
    }
    
    /**
     * Get all conversations for a user
     */
    public List<ConversationResponseVo> getUserConversations(Long userId) {
        List<ConversationMember> memberRecords = conversationMemberRepository
            .findActiveConversationsForUser(userId);
        
        List<ConversationResponseVo> conversations = new ArrayList<>();
        
        for (ConversationMember member : memberRecords) {
            Optional<Conversation> conv = conversationRepository.findById(member.getConversationId());
            if (conv.isPresent() && conv.get().getIsActive()) {
                ConversationResponseVo vo = new ConversationResponseVo(conv.get());
                
                // Get participant IDs
                List<Long> participantIds = conversationMemberRepository
                    .findByConversationId(conv.get().getId())
                    .stream()
                    .filter(m -> m.getLeftAt() == null)
                    .map(ConversationMember::getUserId)
                    .collect(Collectors.toList());
                vo.setParticipantIds(participantIds);
                
                conversations.add(vo);
            }
        }
        
        // Sort by last message date
        conversations.sort((a, b) -> {
            Date dateA = a.getLastMessageAt();
            Date dateB = b.getLastMessageAt();
            if (dateA == null && dateB == null) return 0;
            if (dateA == null) return 1;
            if (dateB == null) return -1;
            return dateB.compareTo(dateA);
        });
        
        return conversations;
    }
}
