package com.anchor.app.media.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing the type of media content.
 */
public enum MediaType {
    
    Audio("Audio", org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE),
	Video("Video", org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE),
	HlsVideo("HlsVideo", org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE),
	Image_JPEG("Image_JPEG", org.springframework.http.MediaType.IMAGE_JPEG_VALUE),
    Image_PNG("Image_PNG", org.springframework.http.MediaType.IMAGE_PNG_VALUE),
    Image_GIF("Image_GIF", org.springframework.http.MediaType.IMAGE_GIF_VALUE),
	MultiMedia("MultiMedia", org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE),
	Document("Document", org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE),
	Invalid ("Invalid", org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE),
	;
	
	
	private String value;
	private String contentType;
    private static Map<String, MediaType> map = new HashMap<String, MediaType>();

	
    private MediaType(String value, String contentType)
    {
    this.value = value;
    this.contentType = contentType;	
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
    
    public String getContentType() {
        return contentType;
    }

}
