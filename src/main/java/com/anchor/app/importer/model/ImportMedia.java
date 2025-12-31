package com.anchor.app.importer.model;

public class ImportMedia {
	
	private String id;
	private String fullName;
	private String name;
	private String extn;
	private String path;
	private int width;
	private int height;
	private int vidoeStreamId;
	private int audioStreamId;
	private String hlsMediaPath;
	private String masterPlayListName;
	private String hlsCmd;
	private int videoDurationInSecond;
	
	private VideoQuality videoInfo;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExtn() {
		return extn;
	}
	public void setExtn(String extn) {
		this.extn = extn;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getVidoeStreamId() {
		return vidoeStreamId;
	}
	public void setVidoeStreamId(int vidoeStreamId) {
		this.vidoeStreamId = vidoeStreamId;
	}
	public int getAudioStreamId() {
		return audioStreamId;
	}
	public void setAudioStreamId(int audioStreamId) {
		this.audioStreamId = audioStreamId;
	}
	
	public String getHlsMediaPath() {
		return hlsMediaPath;
	}
	public void setHlsMediaPath(String hlsMediaPath) {
		this.hlsMediaPath = hlsMediaPath;
	}
	public String getMasterPlayListName() {
		return masterPlayListName;
	}
	public void setMasterPlayListName(String masterPlayListName) {
		this.masterPlayListName = masterPlayListName;
	}
	public VideoQuality getVideoInfo() {
		return videoInfo;
	}
	public void setVideoInfo(VideoQuality videoInfo) {
		this.videoInfo = videoInfo;
	}
	public String getHlsCmd() {
		return hlsCmd;
	}
	public void setHlsCmd(String hlsCmd) {
		this.hlsCmd = hlsCmd;
	}
	public int getVideoDurationInSecond() {
		return videoDurationInSecond;
	}
	public void setVideoDurationInSecond(int videoDurationInSecond) {
		this.videoDurationInSecond = videoDurationInSecond;
	}

	
	
	
	

}
