package com.anchor.app.media.enums;

/**
 * Enum representing MIME types for media content.
 * Each enum constant contains the standard MIME type string value.
 */
public enum MediaMimeType {
    
    // Image MIME Types
    /**
     * JPEG image format
     */
    IMAGE_JPEG("image/jpeg", "JPEG Image"),
    
    /**
     * PNG image format
     */
    IMAGE_PNG("image/png", "PNG Image"),
    
    /**
     * GIF image format
     */
    IMAGE_GIF("image/gif", "GIF Image"),
    
    /**
     * WebP image format
     */
    IMAGE_WEBP("image/webp", "WebP Image"),
    
    /**
     * BMP image format
     */
    IMAGE_BMP("image/bmp", "BMP Image"),
    
    /**
     * SVG image format
     */
    IMAGE_SVG("image/svg+xml", "SVG Image"),
    
    // Video MIME Types
    /**
     * MP4 video format
     */
    VIDEO_MP4("video/mp4", "MP4 Video"),
    
    /**
     * WebM video format
     */
    VIDEO_WEBM("video/webm", "WebM Video"),
    
    /**
     * OGG video format
     */
    VIDEO_OGG("video/ogg", "OGG Video"),
    
    /**
     * AVI video format
     */
    VIDEO_AVI("video/x-msvideo", "AVI Video"),
    
    /**
     * MPEG video format
     */
    VIDEO_MPEG("video/mpeg", "MPEG Video"),
    
    /**
     * QuickTime video format
     */
    VIDEO_QUICKTIME("video/quicktime", "QuickTime Video"),
    
    // Audio MIME Types
    /**
     * MP3 audio format
     */
    AUDIO_MP3("audio/mpeg", "MP3 Audio"),
    
    /**
     * WAV audio format
     */
    AUDIO_WAV("audio/wav", "WAV Audio"),
    
    /**
     * OGG audio format
     */
    AUDIO_OGG("audio/ogg", "OGG Audio"),
    
    /**
     * WebM audio format
     */
    AUDIO_WEBM("audio/webm", "WebM Audio"),
    
    /**
     * AAC audio format
     */
    AUDIO_AAC("audio/aac", "AAC Audio"),
    
    /**
     * Opus audio format
     */
    AUDIO_OPUS("audio/opus", "Opus Audio"),
    
    // Document MIME Types
    /**
     * PDF document format
     */
    APPLICATION_PDF("application/pdf", "PDF Document"),
    
    /**
     * Microsoft Word document (legacy)
     */
    APPLICATION_DOC("application/msword", "Word Document"),
    
    /**
     * Microsoft Word document (modern)
     */
    APPLICATION_DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Word Document (DOCX)"),
    
    /**
     * Microsoft Excel spreadsheet (legacy)
     */
    APPLICATION_XLS("application/vnd.ms-excel", "Excel Spreadsheet"),
    
    /**
     * Microsoft Excel spreadsheet (modern)
     */
    APPLICATION_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "Excel Spreadsheet (XLSX)"),
    
    /**
     * Microsoft PowerPoint presentation (legacy)
     */
    APPLICATION_PPT("application/vnd.ms-powerpoint", "PowerPoint Presentation"),
    
    /**
     * Microsoft PowerPoint presentation (modern)
     */
    APPLICATION_PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "PowerPoint Presentation (PPTX)"),
    
    /**
     * Plain text document
     */
    APPLICATION_TXT("text/plain", "Text Document"),
    
    /**
     * JSON document
     */
    APPLICATION_JSON("application/json", "JSON Document"),
    
    /**
     * XML document
     */
    APPLICATION_XML("application/xml", "XML Document"),
    
    /**
     * ZIP archive
     */
    APPLICATION_ZIP("application/zip", "ZIP Archive"),
    
    // Other
    /**
     * Generic binary data
     */
    APPLICATION_OCTET_STREAM("application/octet-stream", "Binary Data");
    
    private final String mimeType;
    private final String description;
    
    MediaMimeType(String mimeType, String description) {
        this.mimeType = mimeType;
        this.description = description;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get MediaMimeType from MIME type string value
     * @param mimeType the MIME type string to look up
     * @return the matching MediaMimeType, or APPLICATION_OCTET_STREAM as fallback
     */
    public static MediaMimeType fromMimeType(String mimeType) {
        if (mimeType == null || mimeType.trim().isEmpty()) {
            return APPLICATION_OCTET_STREAM;
        }
        
        for (MediaMimeType type : values()) {
            if (type.mimeType.equalsIgnoreCase(mimeType.trim())) {
                return type;
            }
        }
        return APPLICATION_OCTET_STREAM; // Default fallback
    }
    
    /**
     * Check if the given MIME type string is valid
     * @param mimeType the MIME type string to check
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String mimeType) {
        if (mimeType == null || mimeType.trim().isEmpty()) {
            return false;
        }
        
        for (MediaMimeType type : values()) {
            if (type.mimeType.equalsIgnoreCase(mimeType.trim())) {
                return true;
            }
        }
        return false;
    }
}
