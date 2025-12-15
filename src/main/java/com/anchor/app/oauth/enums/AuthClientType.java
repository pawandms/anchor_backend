package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum AuthClientType {

	Internal("Internal"),
	External("External"),
	;

	private String value;
    private static Map<String, AuthClientType> map = new HashMap<String, AuthClientType>();


    private AuthClientType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (AuthClientType type : AuthClientType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static AuthClientType getType(String type) {
        return (AuthClientType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
