package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum MsgReactionType {

	Like("Like"),
	Love("Love"),
	Surprised("Surprised"),
	Happy("Happy"),
	Sad("Sad"),
	Angery("Angery")
	
	;
	
	
	private String value;
    private static Map<String, MsgReactionType> map = new HashMap<String, MsgReactionType>();

	
    private MsgReactionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MsgReactionType type : MsgReactionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MsgReactionType getType(String type) {
        return (MsgReactionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
