package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum MsgActionType {

	Add("Add"),
	Reply("Reply"),
	Forward("Forward"),
	Delete("Delete"),
	DeleteForAll("DeleteForAll"),
	View("View"),
	Update_UnRead("Update_UnRead"),
	MsgReaction("MsgReaction"),
	
	;
	
	
	private String value;
    private static Map<String, MsgActionType> map = new HashMap<String, MsgActionType>();

	
    private MsgActionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MsgActionType type : MsgActionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MsgActionType getType(String type) {
        return (MsgActionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
