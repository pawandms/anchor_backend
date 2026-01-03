package com.anchor.app.media.enums;

/**
 * Enum representing the type of entity that the media is associated with.
 */
public enum MediaEntityType {
    
    MESSAGE("MESSAGE", "Message"),
    USER_PROFILE("USER_PROFILE", "User Profile"),
    CHANNEL_AVATAR("CHANNEL_AVATAR", "Channel Avatar"),
    STATUS("STATUS", "Status"),
    CALL("CALL", "Call");
    
    private final String code;
    private final String description;
    
    MediaEntityType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get MediaEntityType from code value
     * @param code the code to look up
     * @return the matching MediaEntityType
     * @throws IllegalArgumentException if code is not found
     */
    public static MediaEntityType fromCode(String code) {
        for (MediaEntityType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown MediaEntityType code: " + code);
    }
}
