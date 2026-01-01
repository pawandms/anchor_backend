package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ContactType {

	FRIEND("FRIEND"),
	BLOCKED("BLOCKED"),
	
	;
	
	
	private String value;
    private static Map<String, ContactType> map = new HashMap<String, ContactType>();

	
    private ContactType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ContactType type : ContactType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ContactType getType(String type) {
        return (ContactType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
