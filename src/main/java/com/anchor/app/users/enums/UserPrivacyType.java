package com.anchor.app.users.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserPrivacyType {

	EveryOne("EveryOne"),
	Contacts("Contacts"),
	NoBody("NoBody");
	
	
	private String value;
    private static Map<String, UserPrivacyType> map = new HashMap<String, UserPrivacyType>();

	
    private UserPrivacyType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (UserPrivacyType type : UserPrivacyType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserPrivacyType getType(String type) {
        return (UserPrivacyType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
