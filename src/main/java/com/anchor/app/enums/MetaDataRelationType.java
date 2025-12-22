package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum MetaDataRelationType {

	DepartmentType("DepartmentType"),
	Genre("Genre"),
	MediaType("MediaType"),
	Collection("Collection"),
	Company("Company"),
	Name("Name"),
	TagLine("TagLine"),
	Other("Other");
	
	
	private String value;
    private static Map<String, MetaDataRelationType> map = new HashMap<String, MetaDataRelationType>();

	
    private MetaDataRelationType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MetaDataRelationType type : MetaDataRelationType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MetaDataRelationType getType(String type) {
        return (MetaDataRelationType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
