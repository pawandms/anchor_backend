package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ContactSource {

	MUTUAL_FRIEND("MUTUAL_FRIEND"),
	CHANNEL_MEMBER("CHANNEL_MEMBER"),
	PHONE_CONTACT("PHONE_CONTACT"),
	MANUAL("MANUAL"),
	
	;
	
	
	private String value;
    private static Map<String, ContactSource> map = new HashMap<String, ContactSource>();

	
    private ContactSource(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ContactSource type : ContactSource.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ContactSource getType(String type) {
        return (ContactSource) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
