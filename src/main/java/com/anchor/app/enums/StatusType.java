package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum StatusType {

	Released("Released"),
	UnReleased("UnReleased"),
	InProduction("In Production"),
	PostProduction("Post Production"),
	Canceled("Canceled"),
	UNKNOWN("UNKNOWN");
	
	
	private String value;
    private static Map<String, StatusType> map = new HashMap<String, StatusType>();

	
    private StatusType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (StatusType type : StatusType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static StatusType getType(String type) {
        return (StatusType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
