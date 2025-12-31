package com.anchor.app.messaging.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserPresenceStatus {

	ONLINE("ONLINE"),
	OFFLINE("OFFLINE"),
	AWAY("AWAY"),
	
	;
	
	
	private String value;
    private static Map<String, UserPresenceStatus> map = new HashMap<String, UserPresenceStatus>();

	
    private UserPresenceStatus(String value )
    {
    this.value =value;	
    }
    
    static {
        for (UserPresenceStatus type : UserPresenceStatus.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserPresenceStatus getType(String type) {
        return (UserPresenceStatus) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
