package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum AuthParamType {

	USERNAME ("username"),
	PASSWORD("password"),
	;

	private String value;
    private static Map<String, AuthParamType> map = new HashMap<String, AuthParamType>();


    private AuthParamType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (AuthParamType type : AuthParamType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static AuthParamType getType(String type) {
        return (AuthParamType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
