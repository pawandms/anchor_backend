package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum MsgAttachmentType {

	Audio("Audio"),
	Video("Video"),
	HlsVideo("HlsVideo"),
	Image("Image"),
	MultiMedia("MultiMedia"),
	Document("Document"),
	Invalid ("Invalid"),
	;
	
	
	private String value;
    private static Map<String, MsgAttachmentType> map = new HashMap<String, MsgAttachmentType>();

	
    private MsgAttachmentType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MsgAttachmentType type : MsgAttachmentType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MsgAttachmentType getType(String type) {
        return (MsgAttachmentType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
