package com.anchor.app.messaging.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelMemberRole {

	ADMIN("ADMIN"),
	MEMBER("MEMBER"),
	SUPER_ADMIN("SUPER_ADMIN"),
	
	;
	
	
	private String value;
    private static Map<String, ChannelMemberRole> map = new HashMap<String, ChannelMemberRole>();

	
    private ChannelMemberRole(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ChannelMemberRole type : ChannelMemberRole.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ChannelMemberRole getType(String type) {
        return (ChannelMemberRole) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
