package com.anchor.app.msg.enums;

public enum MemberRole {
    ADMIN,
    MEMBER;
    
    public static MemberRole getRole(String role) {
        if (role == null) {
            return MEMBER;
        }
        try {
            return MemberRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MEMBER;
        }
    }
}
