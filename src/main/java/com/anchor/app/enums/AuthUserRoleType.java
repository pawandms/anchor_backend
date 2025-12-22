package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum AuthUserRoleType {

	SuperAdmin("SuperAdmin"),
	Admin("Admin"),
	Moderator("Moderator"),
	Author("Author"),
	Viewer("Viewer"),
	;
	
	
	private String value;
    private static Map<String, AuthUserRoleType> map = new HashMap<String, AuthUserRoleType>();

	
    private AuthUserRoleType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (AuthUserRoleType type : AuthUserRoleType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static AuthUserRoleType getType(String type) {
        return (AuthUserRoleType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
