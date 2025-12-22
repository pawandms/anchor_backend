package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum ExtensionType {

	M3U8("m3u8"),
	TS("ts");
	
	
	private String value;
    private static Map<String, ExtensionType> map = new HashMap<String, ExtensionType>();

	
    private ExtensionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ExtensionType type : ExtensionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ExtensionType getType(String type) {
        return (ExtensionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
