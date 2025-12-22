package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public enum MediaStreamType {
	
	HLS(1),
	DASH(2);
	
	private int value;
    private static Map<Integer, MediaStreamType> map = new HashMap<Integer, MediaStreamType>();
    
    
    private MediaStreamType(int value )
    {
    this.value =value;	
    }
    
    static {
        for (MediaStreamType type : MediaStreamType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MediaStreamType valueOf(int type) {
        return (MediaStreamType) map.get(type);
    }

    
    public int getValue() {
        return value;
    }
}
