package com.anchor.app.msg.enums;

public enum MessageStatus {
    SENT,
    DELIVERED,
    READ,
    FAILED;
    
    public static MessageStatus getStatus(String status) {
        if (status == null) {
            return SENT;
        }
        try {
            return MessageStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SENT;
        }
    }
}
