package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserRoleType {

	READWRITE_USER("REDWRITE_USER"),
	ADMIN_USER("ADMIN_USER"),
	OPERATION_USER("OPERATION_USER"),
	READ_USER("READ_USER"),
	GENERAL_USER("GENERAL_USER");
	
	
	private String value;
    private static Map<String, UserRoleType> map = new HashMap<String, UserRoleType>();

	
    private UserRoleType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (UserRoleType type : UserRoleType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserRoleType getType(String type) {
        return (UserRoleType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
