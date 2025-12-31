package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelActionType {

	AddUser("AddUser"),
	RemoveUser("RemoveUser"),
	RemoveSelf("RemoveSelf"),
	AddUnread("AddUnread"),
	SetUnread("SetUnread"),
	;
	
	
	private String value;
    private static Map<String, ChannelActionType> map = new HashMap<String, ChannelActionType>();

	
    private ChannelActionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ChannelActionType type : ChannelActionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ChannelActionType getType(String type) {
        return (ChannelActionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
