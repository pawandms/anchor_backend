package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum PeopleType {

	Cast("Cast"),
	Crew("Crew");
	
	private String value;
    private static Map<String, PeopleType> map = new HashMap<String, PeopleType>();

	
    private PeopleType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (PeopleType type : PeopleType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static PeopleType getType(String type) {
        return (PeopleType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
