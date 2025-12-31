package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventEntityType {

	Channel("Channel"),
	User("User"),
	Message("Message"),
	User_Connection("User_Connection"),
	Meta_Data("Meta_Data"),
	Msg_Chnl("Msg_Chnl"),
	;
	
	
	private String value;
    private static Map<String, EventEntityType> map = new HashMap<String, EventEntityType>();

	
    private EventEntityType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (EventEntityType type : EventEntityType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static EventEntityType getType(String type) {
        return (EventEntityType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
