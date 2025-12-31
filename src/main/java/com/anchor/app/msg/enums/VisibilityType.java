package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum VisibilityType {

	Public("Public"),
	Private("Private"),
	Protected("Protected"),
	;
	private String value;
    private static Map<String, VisibilityType> map = new HashMap<String, VisibilityType>();

	
    private VisibilityType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (VisibilityType type : VisibilityType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static VisibilityType getType(String type) {
        return (VisibilityType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
