package com.anchor.app.media.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing the type of media content.
 */
public enum MediaType {
    
    Audio("Audio"),
	Video("Video"),
	HlsVideo("HlsVideo"),
	Image("Image"),
	MultiMedia("MultiMedia"),
	Document("Document"),
	Invalid ("Invalid"),
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
