package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserActionStatusType {

	Auto_Approve("Auto_Approve"),
	Approve("Approve"),
	Reject("Reject"),
	Block("Block"),
    Pending("Pending"),
	
	;
	
	
	private String value;
    private static Map<String, UserActionStatusType> map = new HashMap<String, UserActionStatusType>();

	
    private UserActionStatusType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (UserActionStatusType type : UserActionStatusType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserActionStatusType getType(String type) {
        return (UserActionStatusType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
