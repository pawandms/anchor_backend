package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.messaging.enums.CallType;
import com.anchor.app.messaging.enums.CallStatus;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "calls")
public class Call {
    
    @Id
    private String id;
    
    private String channelId;
    private String callType; // VOICE or VIDEO
    private String initiatedBy;
    private List<String> participants;
    private String status; // RINGING, ONGOING, COMPLETED, MISSED, REJECTED, FAILED
    private Date startedAt;
    private Date endedAt;
    private Integer duration;
    private List<String> missedBy;
    private List<String> rejectedBy;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
