package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum HLSPropertyType {

	HLS_MST_PLAYLIST_NAME("playlist.m3u8"),
	HLS_PLAYLIST_EXTENSION("m3u8");
	
	
	private String value;
    private static Map<String, HLSPropertyType> map = new HashMap<String, HLSPropertyType>();

	
    private HLSPropertyType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (HLSPropertyType type : HLSPropertyType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static HLSPropertyType getType(String type) {
        return (HLSPropertyType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
