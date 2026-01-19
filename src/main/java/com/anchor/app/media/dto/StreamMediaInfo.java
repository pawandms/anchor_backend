package com.anchor.app.media.dto;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.dto.BaseVo;
import com.anchor.app.media.enums.MediaType;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class StreamMediaInfo extends BaseVo {

    String mediaId;
    String userID;
    @JsonIgnore
    MultipartFile inputFile;
    InputStream mediaStream;
    MediaType mediaType;

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
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public MultipartFile getInputFile() {
        return inputFile;
    }
    public void setInputFile(MultipartFile inputFile) {
        this.inputFile = inputFile;
    }
    public String getMediaId() {
        return mediaId;
    }
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
    

}
