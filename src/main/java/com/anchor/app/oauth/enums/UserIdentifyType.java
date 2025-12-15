package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserIdentifyType {

	MOBILE(1),
	EMAIL(2),
	USERNAME(3);
	
	
	private int value;
    private static Map<Integer, UserIdentifyType> map = new HashMap<Integer, UserIdentifyType>();

	
    private UserIdentifyType(int value )
    {
    this.value =value;	
    }
    
    static {
        for (UserIdentifyType type : UserIdentifyType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserIdentifyType getType(String type) {
        return (UserIdentifyType) map.get(type);
    }

    
    public int getValue() {
        return value;
    }


}
