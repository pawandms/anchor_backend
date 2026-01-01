package com.anchor.app.msg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.RequestType;
import com.anchor.app.msg.enums.ResponseType;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contact_requests")
public class ContactRequest {
    
    @Id
    private String id;
    
    private String fromUserId;
    private String toUserId;
    private RequestType requestType; // FRIEND_REQUEST
    private ResponseType responseType; // PENDING, ACCEPTED, REJECTED
    private Date requestedAt;
    private Date respondedAt;
    private String message;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
