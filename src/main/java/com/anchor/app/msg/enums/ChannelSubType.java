package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelSubType {

	Group("Group"),
	OneToOne("OneToOne"),
	
	;
	
	
	private String value;
    private static Map<String, ChannelSubType> map = new HashMap<String, ChannelSubType>();

	
    private ChannelSubType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ChannelSubType type : ChannelSubType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ChannelSubType getType(String type) {
        return (ChannelSubType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
