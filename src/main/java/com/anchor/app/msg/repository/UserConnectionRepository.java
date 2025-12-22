package com.anchor.app.msg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.UserConnection;
import com.anchor.app.msg.model.UserConnection.ConnectionStatus;

@Repository
public interface UserConnectionRepository extends MongoRepository<UserConnection, String> {
    
    // Find all accepted connections for a user
    @Query("{ $or: [ { 'userId': ?0, 'status': 'ACCEPTED' }, { 'connectedUserId': ?0, 'status': 'ACCEPTED' } ] }")
    List<UserConnection> findAcceptedConnections(Long userId);
    
    // Find connection between two users
    @Query("{ $or: [ " +
           "{ 'userId': ?0, 'connectedUserId': ?1 }, " +
           "{ 'userId': ?1, 'connectedUserId': ?0 } " +
           "] }")
    Optional<UserConnection> findConnectionBetweenUsers(Long userId1, Long userId2);
    
    // Find pending requests for a user
    @Query("{ 'connectedUserId': ?0, 'status': 'PENDING' }")
    List<UserConnection> findPendingRequests(Long userId);
    
    // Find pending requests sent by user
    @Query("{ 'userId': ?0, 'status': 'PENDING' }")
    List<UserConnection> findSentRequests(Long userId);
    
    // Count connections
    @Query(value = "{ $or: [ { 'userId': ?0, 'status': 'ACCEPTED' }, { 'connectedUserId': ?0, 'status': 'ACCEPTED' } ] }", count = true)
    Long countConnections(Long userId);
    
    // Check if connection exists
    @Query(value = "{ $or: [ " +
           "{ 'userId': ?0, 'connectedUserId': ?1, 'status': ?2 }, " +
           "{ 'userId': ?1, 'connectedUserId': ?0, 'status': ?2 } " +
           "] }", exists = true)
    boolean existsByUserIdsAndStatus(Long userId1, Long userId2, ConnectionStatus status);
}
