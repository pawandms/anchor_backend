package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelType {

	Messaging("Messaging"),
	Video("Media"),
	Audio("Audio"),
	Blog("Blog"),
	All("All"),
	
	;
	
	
	private String value;
    private static Map<String, ChannelType> map = new HashMap<String, ChannelType>();

	
    private ChannelType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ChannelType type : ChannelType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ChannelType getType(String type) {
        return (ChannelType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
