package com.anchor.app.media.dto;

import java.io.InputStream;

import com.anchor.app.dto.BaseVo;
import com.anchor.app.media.enums.MediaType;


public class StreamMediaInfo extends BaseVo {

    String Id;
    InputStream mediaStream;
    MediaType mediaType;


    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
    public InputStream getMediaStream() {
        return mediaStream;
    }
    public void setMediaStream(InputStream mediaStream) {
        this.mediaStream = mediaStream;
    }


    

}
