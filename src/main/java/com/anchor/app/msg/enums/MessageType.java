package com.anchor.app.msg.enums;

public enum MessageType {
    TEXT,
    MEDIA,
    EMOJI,
    SYSTEM;
    
    public static MessageType getType(String type) {
        if (type == null) {
            return TEXT;
        }
        try {
            return MessageType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TEXT;
        }
    }
}
