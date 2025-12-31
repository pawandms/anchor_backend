package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserConnectionStatusType {

	Add("Approve"),
	Block("Reject"),
	
	;
	
	
	private String value;
    private static Map<String, UserConnectionStatusType> map = new HashMap<String, UserConnectionStatusType>();

	
    private UserConnectionStatusType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (UserConnectionStatusType type : UserConnectionStatusType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserConnectionStatusType getType(String type) {
        return (UserConnectionStatusType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
