package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum ImportStatusType {

	Open("Open"),
	InProgress("InProgress"),
	Completed("Completed"),
	DuplicateRequest("Duplicate Request"),
	Error("Error");
	
	
	private String value;
    private static Map<String, ImportStatusType> map = new HashMap<String, ImportStatusType>();

	
    private ImportStatusType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ImportStatusType type : ImportStatusType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ImportStatusType getType(String type) {
        return (ImportStatusType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
