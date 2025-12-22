package com.anchor.app.msg.enums;

public enum ConversationType {
    ONE_TO_ONE,
    GROUP;
    
    public static ConversationType getType(String type) {
        if (type == null) {
            return ONE_TO_ONE;
        }
        try {
            return ConversationType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ONE_TO_ONE;
        }
    }
}
