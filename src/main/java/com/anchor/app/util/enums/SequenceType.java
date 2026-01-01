package com.anchor.app.util.enums;

import java.util.HashMap;
import java.util.Map;

public enum SequenceType {

	Media("Media"),
	User("User"),
	Client("Client"),
	Metadata("Metadata"),
	UserVerifyToken("UserVerifyToken"),
	Channel("Channel"),
	
	;
	
	
	private String value;
    private static Map<String, SequenceType> map = new HashMap<String, SequenceType>();

	
    private SequenceType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (SequenceType type : SequenceType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static SequenceType getType(String type) {
        return (SequenceType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
