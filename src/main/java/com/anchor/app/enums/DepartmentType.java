package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum DepartmentType {

	Costume_MakeUp("Costume & Make-Up"),
	Visual_Effects("Visual Effects"),
	Acting("Acting"),
	Crew("Crew"),
	Editing("Editing"),
	Directing("Directing"),
	Production("Production"),
	Camera("Camera"),
	Lighting("Lighting"),
	Art("Art"),
	Writing("Writing"),
	Sound("Sound"),
	Unknown("Unknown");
	
	
	private String value;
    private static Map<String, DepartmentType> map = new HashMap<String, DepartmentType>();

	
    private DepartmentType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (DepartmentType type : DepartmentType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static DepartmentType getType(String type) {
        return (DepartmentType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
