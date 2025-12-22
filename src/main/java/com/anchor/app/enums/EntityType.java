package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum EntityType {

	Media("Media"),
	Movie("Movie"),
	Person("Person"),
	Tv_Show("TV Show"),
	MusicVideo("Music Video"),
	Collection("Collection"),
	Company("Company"),
	Channel("Channel"),
	UserProfile("UserProfile"),
	;
	
	
	private String value;
    private static Map<String, EntityType> map = new HashMap<String, EntityType>();

	
    private EntityType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (EntityType type : EntityType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static EntityType getType(String type) {
        return (EntityType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
