package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaOrderType {

	TRENDING("Trending"),
	LATEST("Latest"),
	POPULAR("Poular");
	
	private String value;
    private static Map<String, MediaOrderType> map = new HashMap<String, MediaOrderType>();

	
    private MediaOrderType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MediaOrderType type : MediaOrderType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MediaOrderType getType(String type) {
        return (MediaOrderType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
