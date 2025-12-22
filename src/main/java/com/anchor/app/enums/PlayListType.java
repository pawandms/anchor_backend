package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum PlayListType {

	Movie("Movie"),
	Music_Video("Music Video"),
	Music_Audio("Music Audio"),
	TV_Shows("TV Shows"),
	Trailar("Trailar");
	
	private String value;
    private static Map<String, PlayListType> map = new HashMap<String, PlayListType>();

	
    private PlayListType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (PlayListType type : PlayListType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static PlayListType getType(String type) {
        return (PlayListType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
