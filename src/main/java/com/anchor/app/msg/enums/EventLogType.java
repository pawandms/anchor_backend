package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventLogType {

	UC("User_Connection"),
	User_CN("User_CN"),
	Meta("Meta"),
	;
	
	
	private String value;
    private static Map<String, EventLogType> map = new HashMap<String, EventLogType>();

	
    private EventLogType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (EventLogType type : EventLogType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static EventLogType getType(String type) {
        return (EventLogType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
