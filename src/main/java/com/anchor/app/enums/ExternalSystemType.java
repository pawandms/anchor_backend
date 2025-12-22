package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Source of Medium from Which Respective Media is Imported 
 * @author pawan
 *
 */
public enum ExternalSystemType {

	TMDB("TMDB"),
	YOUTUBE("YouTube");
	
	private String value;
    private static Map<String, ExternalSystemType> map = new HashMap<String, ExternalSystemType>();

	
    private ExternalSystemType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ExternalSystemType type : ExternalSystemType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ExternalSystemType getType(String type) {
        return (ExternalSystemType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
