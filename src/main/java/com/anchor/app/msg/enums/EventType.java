package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventType {

	Chnl_Add_User("Add user to channel"),
	Chnl_Remove_User("Remove user from channel"),
	Chnl_Add_Msg("Add Msg to Chnl"),
	Chnl_Remove_msg("Msg removed from chnl"),
	Add_Friend("Add_Friend"),
	Remove_Friend("Add_Friend"),
	User_Status("User Status"),
	Chnl_Msg_Add_Reaction("Chnl_Msg_Add_Reaction"),
	;
	
	
	private String value;
    private static Map<String, EventType> map = new HashMap<String, EventType>();

	
    private EventType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (EventType type : EventType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static EventType getType(String type) {
        return (EventType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
