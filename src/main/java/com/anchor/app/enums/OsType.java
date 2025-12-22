package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum OsType {

	Windows("Windows"),
	Unix("Unix"),
	Mac("Mac"),
	Solaris("Solaris");
	
	private String value;
    private static Map<String, OsType> map = new HashMap<String, OsType>();

	
    private OsType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (OsType type : OsType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static OsType getType(String type) {
        return (OsType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
