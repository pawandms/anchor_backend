package com.anchor.app.model.hls;

import java.util.ArrayList;
import java.util.List;


public class HlsStreamInfo {
	
	private String id;
	private int bandwidth;
	private int averageBandwidth;
	private List<String> codecList;
	private HlsResolution resolution;
	private float frameRate;
	private String audio;
	private String video;
	private String subtitles;
	private String closedCaptions;
	
	public HlsStreamInfo() {
		super();
		this.codecList = new ArrayList<String>();
	
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}
	public int getAverageBandwidth() {
		return averageBandwidth;
	}
	public void setAverageBandwidth(int averageBandwidth) {
		this.averageBandwidth = averageBandwidth;
	}
	public HlsResolution getResolution() {
		return resolution;
	}
	public void setResolution(HlsResolution resolution) {
		this.resolution = resolution;
	}
	public float getFrameRate() {
		return frameRate;
	}
	public void setFrameRate(float frameRate) {
		this.frameRate = frameRate;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getSubtitles() {
		return subtitles;
	}
	public void setSubtitles(String subtitles) {
		this.subtitles = subtitles;
	}
	public String getClosedCaptions() {
		return closedCaptions;
	}
	public void setClosedCaptions(String closedCaptions) {
		this.closedCaptions = closedCaptions;
	}
	public List<String> getCodecList() {
		return codecList;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		HlsStreamInfo other = (HlsStreamInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
		

	
}
