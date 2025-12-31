package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventLogSubType {

	UC_ADD("UserConnection Add"),
	UC_Remove("UserConnection Remove"),
	UC_Block("UserConnection Block "),
	Name("Name"),
	GeoLocation("GeoLocation"),
	Meta_Tag("Meta_Tag"),
	Meta_Data("Meta_Data"),
	MSG_ADD_REC("Msg_Reaction_Add"),
	
	;
	
	
	private String value;
    private static Map<String, EventLogSubType> map = new HashMap<String, EventLogSubType>();

	
    private EventLogSubType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (EventLogSubType type : EventLogSubType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static EventLogSubType getType(String type) {
        return (EventLogSubType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
