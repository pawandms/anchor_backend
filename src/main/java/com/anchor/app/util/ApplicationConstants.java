package com.anchor.app.util;

import java.util.regex.Pattern;

public class ApplicationConstants {
    public static final String VIDEO = "/video";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int BYTE_RANGE = 1024;
    public static final int DEFAULT_BUFFER_SIZE  = 20480; // ..bytes = 20KB.
    public static final int BUFFER_LENGTH = 1024 * 16;
    public static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;
    public static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");
    public static final String EXPIRE_HEADER = "Expires";
    public static final String CACHE_CONTROL_HEADER = "Cache-Control";
    public static final String CACHE_CONTROL_VALUE = "max-age=31536000";

   

    private ApplicationConstants() {
    }
}
