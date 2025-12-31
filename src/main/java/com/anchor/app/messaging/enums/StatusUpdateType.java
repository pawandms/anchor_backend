package com.anchor.app.messaging.enums;

import java.util.HashMap;
import java.util.Map;

public enum StatusUpdateType {

	TEXT("TEXT"),
	IMAGE("IMAGE"),
	VIDEO("VIDEO"),
	
	;
	
	
	private String value;
    private static Map<String, StatusUpdateType> map = new HashMap<String, StatusUpdateType>();

	
    private StatusUpdateType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (StatusUpdateType type : StatusUpdateType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static StatusUpdateType getType(String type) {
        return (StatusUpdateType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
