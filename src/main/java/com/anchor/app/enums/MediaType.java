package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaType {

	Movie("Movie"),
	Music_Video("MusicVideo"),
	Music_Audio("MusicAudio"),
	TV_Shows("TVShows"),
	Trailar("Trailar");
	
	private String value;
    private static Map<String, MediaType> map = new HashMap<String, MediaType>();

	
    private MediaType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MediaType type : MediaType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MediaType getType(String type) {
        return (MediaType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
