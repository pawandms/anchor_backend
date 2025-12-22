package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum CollectionType {

	Movie("Movie"),
	Music_Video("Music Video"),
	Music_Audio("Music Audio"),
	TV_Shows("TV Shows");
	
	private String value;
    private static Map<String, CollectionType> map = new HashMap<String, CollectionType>();

	
    private CollectionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (CollectionType type : CollectionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static CollectionType getType(String type) {
        return (CollectionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
