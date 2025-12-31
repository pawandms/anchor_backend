package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum MsgBoxType {

	Inbox("Inbox"),
	Sent("Sent"),
	;
	
	
	private String value;
    private static Map<String, MsgBoxType> map = new HashMap<String, MsgBoxType>();

	
    private MsgBoxType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MsgBoxType type : MsgBoxType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MsgBoxType getType(String type) {
        return (MsgBoxType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
