package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum HlsMediaType {

	AUDIO("AUDIO"),
    VIDEO("VIDEO"),
    SUBTITLES("SUBTITLES"),
    CLOSED_CAPTIONS("CLOSED-CAPTIONS");
	
	
	private String value;
    private static Map<String, HlsMediaType> map = new HashMap<String, HlsMediaType>();

	
    private HlsMediaType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (HlsMediaType type : HlsMediaType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static HlsMediaType getType(String type) {
        return (HlsMediaType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
