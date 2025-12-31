package com.anchor.app.messaging.enums;

import java.util.HashMap;
import java.util.Map;

public enum CallType {

	VOICE("VOICE"),
	VIDEO("VIDEO"),
	
	;
	
	
	private String value;
    private static Map<String, CallType> map = new HashMap<String, CallType>();

	
    private CallType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (CallType type : CallType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static CallType getType(String type) {
        return (CallType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
