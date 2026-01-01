package com.anchor.app.msg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForwardedFrom {
    private String originalSenderId;
    private String originalSenderName;
    private String originalChannelId;
    private Date forwardedAt;
    private Integer forwardChain;
}
