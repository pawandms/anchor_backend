package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventActionType {

	Change_ProfileType("Change_ProfileType"),
	Add_Friend("Add_Friend"),
	Add_Friend_Req_Sent("Add_Friend_Req_Sent"),
	
	;
	
	
	private String value;
    private static Map<String, EventActionType> map = new HashMap<String, EventActionType>();

	
    private EventActionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (EventActionType type : EventActionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static EventActionType getType(String type) {
        return (EventActionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
