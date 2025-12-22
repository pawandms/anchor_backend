package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum MediaGenreType {

	ALL("All"),
	NONE("None"),
	ACTION("Action"),
	HISTORY("History"),
	HORROR("Horror"),
	MUSIC("Music"),
	MYSTERY("Mystery"),
	ROMANCE("Romance"),
	SCIENCE_FICTION("Science Fiction"),
	TV_MOVIE("TV Movie"),
	THRILLER("Thriller"),
	WAR("War"),
	WESTERN("Western"),
	ADVENTURE("Adventure"),
	ANIMATION("Animation"),
	COMEDY("Comedy"),
	CRIME("Crime"),
	DOCUMENTRY("Documentary"),
	DRAMA("Drama"),
	FAMILY("Family"),
	FANTASY("Fantasy"),
	ROCK("Rock"),
	JAZZ("Jazz"),
	POP("Pop"),
	CLASSICIAL("Classical"),
	FILMI("Filmi");
	
	private String value;
    private static Map<String, MediaGenreType> map = new HashMap<String, MediaGenreType>();

	
    private MediaGenreType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (MediaGenreType type : MediaGenreType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static MediaGenreType getType(String type) {
        return (MediaGenreType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
