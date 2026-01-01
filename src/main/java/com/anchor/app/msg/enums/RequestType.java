package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum RequestType {

	FRIEND_REQUEST("FRIEND_REQUEST"),
	
	;
	
	
	private String value;
    private static Map<String, RequestType> map = new HashMap<String, RequestType>();

	
    private RequestType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (RequestType type : RequestType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static RequestType getType(String type) {
        return (RequestType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
