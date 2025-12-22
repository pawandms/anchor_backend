package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum ImageType {

	PersonImage("PersonImage"),
	CompanyLogo("CompanyLogo"),
	CollectionPoster("CollectionPoster"),
	CollectionBackDrop("CollectionBackDrop"),
	MediaPoster("MediaPoster"),
	MediaBackDrop("MediaBackDrop"),
	ChannelLogo("ChannelLogo"),
	ChannelImage("ChannelImage'"),
	Other("Other");
	
	private String value;
    private static Map<String, ImageType> map = new HashMap<String, ImageType>();

	
    private ImageType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ImageType type : ImageType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ImageType getType(String type) {
        return (ImageType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
