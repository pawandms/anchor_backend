package com.anchor.app.action.enums;

import java.util.HashMap;
import java.util.Map;

public enum ActionEntityType {

	Channel("Channel"),
	User("User"),
	Message("Message"),
	User_Connection("User_Connection"),
	Meta_Data("Meta_Data"),
	Msg_Chnl("Msg_Chnl"),
	;


	private String value;
    private static Map<String, ActionEntityType> map = new HashMap<String, ActionEntityType>();


    private ActionEntityType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ActionEntityType type : ActionEntityType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ActionEntityType getType(String type) {
        return (ActionEntityType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
