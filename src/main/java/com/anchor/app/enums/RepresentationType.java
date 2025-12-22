package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum RepresentationType {
	
	R_1080P(1080),
	R_720P(720),
	R_480P(480),
	R_320P(320);
	
	private int value;
    private static Map<Integer, RepresentationType> map = new HashMap<Integer, RepresentationType>();
    
    
    private RepresentationType(int value )
    {
    this.value =value;	
    }
    
    static {
        for (RepresentationType type : RepresentationType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static RepresentationType valueOf(int type) {
        return (RepresentationType) map.get(type);
    }

    
    public int getValue() {
        return value;
    }
}
