package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ContactStatus {

	SUGGESTED("SUGGESTED"),
	PENDING_SENT("PENDING_SENT"),
	PENDING_RECEIVED("PENDING_RECEIVED"),
	ACCEPTED("ACCEPTED"),
	REJECTED("REJECTED"),
	
	;
	
	
	private String value;
    private static Map<String, ContactStatus> map = new HashMap<String, ContactStatus>();

	
    private ContactStatus(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ContactStatus type : ContactStatus.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ContactStatus getType(String type) {
        return (ContactStatus) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
