package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum AuthorityType {

	READWRITE_SCOPE("REDWRITE_SCOPE"),
	ADMIN_SCOPE("ADMIN_SCOPE"),
	OPERATION_SCOPE("OPERATION_SCOPE"),
	READ_SCOPE("READ_SCOPE"),
	READ("read"),
    WRITE("write"),
    TRUST("trust"),
    ROLE_TEST("ROLE_TEST"),
    ROLE_OAUTH_CLIENT("ROLE_OAUTH_CLIENT"),
    ;


	
	private String value;
    private static Map<String, AuthorityType> map = new HashMap<String, AuthorityType>();

	
    private AuthorityType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (AuthorityType type : AuthorityType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static AuthorityType getType(String type) {
        return (AuthorityType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
