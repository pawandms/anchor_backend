package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum CallStatus {

	RINGING("RINGING"),
	ONGOING("ONGOING"),
	COMPLETED("COMPLETED"),
	MISSED("MISSED"),
	REJECTED("REJECTED"),
	FAILED("FAILED"),
	
	;
	
	
	private String value;
    private static Map<String, CallStatus> map = new HashMap<String, CallStatus>();

	
    private CallStatus(String value )
    {
    this.value =value;	
    }
    
    static {
        for (CallStatus type : CallStatus.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static CallStatus getType(String type) {
        return (CallStatus) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
