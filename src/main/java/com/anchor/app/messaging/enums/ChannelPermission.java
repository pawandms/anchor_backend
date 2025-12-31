package com.anchor.app.messaging.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelPermission {

	EVERYONE("EVERYONE"),
	ADMINS("ADMINS"),
	
	;
	
	
	private String value;
    private static Map<String, ChannelPermission> map = new HashMap<String, ChannelPermission>();

	
    private ChannelPermission(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ChannelPermission type : ChannelPermission.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ChannelPermission getType(String type) {
        return (ChannelPermission) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
