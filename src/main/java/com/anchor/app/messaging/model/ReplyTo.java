package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyTo {
    private String messageId;
    private String senderId;
    private String senderName;
    private String content;
    private String messageType;
}
