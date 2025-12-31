package com.anchor.app.messaging.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelVisibility {

	PUBLIC("PUBLIC"),
	PRIVATE("PRIVATE"),
	PROTECTED("PROTECTED"),
	
	;
	
	
	private String value;
    private static Map<String, ChannelVisibility> map = new HashMap<String, ChannelVisibility>();

	
    private ChannelVisibility(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ChannelVisibility type : ChannelVisibility.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ChannelVisibility getType(String type) {
        return (ChannelVisibility) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
