package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserRoleType {

	SuperAdmin("SuperAdmin"),
	Admin("Admin"),
	Moderator("Moderator"),
	Author("Author"),
	Viewer("Viewer"),
	;
	
	
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
