package com.anchor.app.messaging.enums;

import java.util.HashMap;
import java.util.Map;

public enum RelationshipType {

	FRIEND("FRIEND"),
	SUGGESTION("SUGGESTION"),
	REJECTED("REJECTED"),
	BLOCKED_BY_ME("BLOCKED_BY_ME"),
	BLOCKED_BY_THEM("BLOCKED_BY_THEM"),
	
	;
	
	
	private String value;
    private static Map<String, RelationshipType> map = new HashMap<String, RelationshipType>();

	
    private RelationshipType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (RelationshipType type : RelationshipType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static RelationshipType getType(String type) {
        return (RelationshipType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
