package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum HlsPlayListType {

	VOD("VOD"),
	EVENT("EVENT");
	
	
	private String value;
    private static Map<String, HlsPlayListType> map = new HashMap<String, HlsPlayListType>();

	
    private HlsPlayListType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (HlsPlayListType type : HlsPlayListType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static HlsPlayListType getType(String type) {
        return (HlsPlayListType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
