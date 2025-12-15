package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserStatusType {

	Normal(2),
	Forbidden(3),
	Virtual(4),
	Operation(5),
	Unverfieid(6);
	
	
	private int value;
    private static Map<Integer, UserStatusType> map = new HashMap<Integer, UserStatusType>();

	
    private UserStatusType(int value )
    {
    this.value =value;	
    }
    
    static {
        for (UserStatusType type : UserStatusType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserStatusType getType(String type) {
        return (UserStatusType) map.get(type);
    }

    
    public int getValue() {
        return value;
    }


}
