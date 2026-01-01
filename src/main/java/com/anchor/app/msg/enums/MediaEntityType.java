package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaEntityType {

	MESSAGE("MESSAGE"),
	USER_PROFILE("USER_PROFILE"),
	CHANNEL_AVATAR("CHANNEL_AVATAR"),
	STATUS("STATUS"),
	CALL("CALL"),
	
	;
	
	
	private String value;
    private static Map<String, MediaEntityType> map = new HashMap<String, MediaEntityType>();

	
    private MediaEntityType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MediaEntityType type : MediaEntityType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MediaEntityType getType(String type) {
        return (MediaEntityType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
