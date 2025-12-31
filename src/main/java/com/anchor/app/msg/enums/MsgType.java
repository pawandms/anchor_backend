package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum MsgType {

	Text("Text"),
	Html("Html"),
	Audio("Audio"),
	Video("Video"),
	Image("Image"),
	MultiMedia("MultiMedia"),
	Document("Document"),
	System("System"),
	Notification("Notification"),
	;
	
	
	private String value;
    private static Map<String, MsgType> map = new HashMap<String, MsgType>();

	
    private MsgType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MsgType type : MsgType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MsgType getType(String type) {
        return (MsgType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
