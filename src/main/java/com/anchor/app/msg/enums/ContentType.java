package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ContentType {

	Profile("Profile"),
	MsgAttachment("MsgAttachment"),
	
	;
	
	
	private String value;
    private static Map<String, ContentType> map = new HashMap<String, ContentType>();

	
    private ContentType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ContentType type : ContentType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ContentType getType(String type) {
        return (ContentType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
