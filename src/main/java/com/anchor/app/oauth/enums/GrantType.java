package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum GrantType {

	PASSWORD("password"),
	AUTHORIZATION_CODE("authorization_code"),
	REFRESH_TOKEN("refresh_token"),
	CLIENT_CREDENTIALS("client_credentials"),
	IMPLICIT("implicit");
	
	
	private String value;
    private static Map<String, GrantType> map = new HashMap<String, GrantType>();

	
    private GrantType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (GrantType type : GrantType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static GrantType getType(String type) {
        return (GrantType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
