package com.anchor.app.msg.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_connections")
@CompoundIndex(name = "user_connection_idx", def = "{'userId': 1, 'connectedUserId': 1}", unique = true)
public class UserConnection {
    
    @Id
    private String id;
    
    @Indexed
    private Long userId;
    
    @Indexed
    private Long connectedUserId;
    
    private ConnectionStatus status; // PENDING, ACCEPTED, BLOCKED
    
    private Date requestedAt;
    private Date acceptedAt;
    
    private Long requestedBy; // Who sent the connection request
    
    public UserConnection() {
        this.status = ConnectionStatus.PENDING;
        this.requestedAt = new Date();
    }
    
    public enum ConnectionStatus {
        PENDING,
        ACCEPTED,
        BLOCKED,
        REJECTED
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getConnectedUserId() {
        return connectedUserId;
    }
    
    public void setConnectedUserId(Long connectedUserId) {
        this.connectedUserId = connectedUserId;
    }
    
    public ConnectionStatus getStatus() {
        return status;
    }
    
    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }
    
    public Date getRequestedAt() {
        return requestedAt;
    }
    
    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }
    
    public Date getAcceptedAt() {
        return acceptedAt;
    }
    
    public void setAcceptedAt(Date acceptedAt) {
        this.acceptedAt = acceptedAt;
    }
    
    public Long getRequestedBy() {
        return requestedBy;
    }
    
    public void setRequestedBy(Long requestedBy) {
        this.requestedBy = requestedBy;
    }
}
