package com.anchor.app.importer.model;

import java.util.ArrayList;
import java.util.List;

public class VideoQuality {
	
	private int Id;
	private String quality;
	private int width;
	private int height;
	private int resoluation;
	private int bitrateLow;
	private int bitrateHigh;
	private int bitrateAudio;
	private String audioLanguageCd;
	private String audioGroupname;
	
	// Stream ID for Video Stream in given Video
	private int videoStreamId;
	private int audioStreamId;
	private List<Integer> hlsQualityList;
	private int videoDurationInSecond;

	private boolean active;
	
	public VideoQuality(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.Id = width+height;
		this.resoluation = width * height;
		this.hlsQualityList = new ArrayList<Integer>();
		this.audioLanguageCd = "EN";
		this.audioGroupname = "audio128";
		this.active = true;
	}
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
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
	public int getBitrateLow() {
		return bitrateLow;
	}
	public void setBitrateLow(int bitrateLow) {
		this.bitrateLow = bitrateLow;
	}
	public int getBitrateHigh() {
		return bitrateHigh;
	}
	public void setBitrateHigh(int bitrateHigh) {
		this.bitrateHigh = bitrateHigh;
	}
	public int getBitrateAudio() {
		return bitrateAudio;
	}
	public void setBitrateAudio(int bitrateAudio) {
		this.bitrateAudio = bitrateAudio;
	}
	
	public String getAudioLanguageCd() {
		return audioLanguageCd;
	}


	public void setAudioLanguageCd(String audioLanguageCd) {
		this.audioLanguageCd = audioLanguageCd;
	}

	

	public String getAudioGroupname() {
		return audioGroupname;
	}


	public void setAudioGroupname(String audioGroupname) {
		this.audioGroupname = audioGroupname;
	}


	public int getResoluation() {
		return resoluation;
	}
	
	public List<Integer> getHlsQualityList() {
		return hlsQualityList;
	}

	public int getVideoStreamId() {
		return videoStreamId;
	}


	public void setVideoStreamId(int videoStreamId) {
		this.videoStreamId = videoStreamId;
	}


	public int getAudioStreamId() {
		return audioStreamId;
	}


	public void setAudioStreamId(int audioStreamId) {
		this.audioStreamId = audioStreamId;
	}

	

	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}

	
	

	public int getVideoDurationInSecond() {
		return videoDurationInSecond;
	}


	public void setVideoDurationInSecond(int videoDurationInSecond) {
		this.videoDurationInSecond = videoDurationInSecond;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Id;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VideoQuality other = (VideoQuality) obj;
		if (Id != other.Id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "VideoQuality [Id=" + Id + ", quality=" + quality + ", width=" + width + ", height=" + height
				+ ", resoluation=" + resoluation + ", bitrateLow=" + bitrateLow + ", bitrateHigh=" + bitrateHigh
				+ ", bitrateAudio=" + bitrateAudio + ", videoStreamId=" + videoStreamId + ", audioStreamId="
				+ audioStreamId + ", hlsQualityList=" + hlsQualityList + "]";
	}


	
	

}
