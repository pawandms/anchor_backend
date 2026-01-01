package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaType {

	IMAGE("IMAGE"),
	VIDEO("VIDEO"),
	AUDIO("AUDIO"),
	DOCUMENT("DOCUMENT"),
	VOICE("VOICE"),
	PROFILE_IMAGE("PROFILE_IMAGE"),
	THUMBNAIL("THUMBNAIL"),
	
	;
	
	
	private String value;
    private static Map<String, MediaType> map = new HashMap<String, MediaType>();

	
    private MediaType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MediaType type : MediaType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MediaType getType(String type) {
        return (MediaType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
