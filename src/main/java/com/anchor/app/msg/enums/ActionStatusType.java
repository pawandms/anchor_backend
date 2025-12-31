package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ActionStatusType {

	Approve("Approve"),
	Reject("Reject"),
	
	;
	
	
	private String value;
    private static Map<String, ActionStatusType> map = new HashMap<String, ActionStatusType>();

	
    private ActionStatusType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ActionStatusType type : ActionStatusType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ActionStatusType getType(String type) {
        return (ActionStatusType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
