package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ResponseType {

	PENDING("PENDING"),
	ACCEPTED("ACCEPTED"),
	REJECTED("REJECTED"),
	
	;
	
	
	private String value;
    private static Map<String, ResponseType> map = new HashMap<String, ResponseType>();

	
    private ResponseType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ResponseType type : ResponseType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ResponseType getType(String type) {
        return (ResponseType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
