package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum GenderType {
	Unknown("Unknown"),
	Male("Male"),
	Female("Female"),
	Other("Other");
	
	
	private String value;
    private static Map<String, GenderType> map = new HashMap<String, GenderType>();

	
    private GenderType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (GenderType type : GenderType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static GenderType getType(String type) {
        return (GenderType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
