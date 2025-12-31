package com.anchor.app.model.hls;

import java.util.List;

import org.springframework.data.annotation.Id;


public class HlsIFrameStreamInfo {

	// This is Child of MasterPlayList hence id will be masterId_{number}
	@Id
	private String id;
    private int bandwidth;
    private int averageBandwidth;
    private List<String> codecs;
    private HlsResolution resolution;
    private float frameRate;
    private String video;
    private String uri;
    
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
	public List<String> getCodecs() {
		return codecs;
	}
	public void setCodecs(List<String> codecs) {
		this.codecs = codecs;
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
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
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
		HlsIFrameStreamInfo other = (HlsIFrameStreamInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    
}
