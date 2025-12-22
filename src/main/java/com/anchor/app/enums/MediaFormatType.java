package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaFormatType {

	HLS("HLS"),
	DASH("DASH"),
	MP_4("MP4");
	
	private String value;
    private static Map<String, MediaFormatType> map = new HashMap<String, MediaFormatType>();

	
    private MediaFormatType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MediaFormatType type : MediaFormatType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MediaFormatType getType(String type) {
        return (MediaFormatType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
