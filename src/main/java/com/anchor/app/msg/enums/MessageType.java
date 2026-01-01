package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {

	TEXT("TEXT"),
	IMAGE("IMAGE"),
	VIDEO("VIDEO"),
	AUDIO("AUDIO"),
	VOICE("VOICE"),
	DOCUMENT("DOCUMENT"),
	LOCATION("LOCATION"),
	CONTACT("CONTACT"),
	STICKER("STICKER"),
	GIF("GIF"),
	POLL("POLL"),
	SYSTEM("SYSTEM"),
	
	;
	
	
	private String value;
    private static Map<String, MessageType> map = new HashMap<String, MessageType>();

	
    private MessageType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MessageType type : MessageType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MessageType getType(String type) {
        return (MessageType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
