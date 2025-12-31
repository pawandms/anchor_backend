package com.anchor.app.model.hls;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.ExtensionType;
import com.anchor.app.enums.HlsPlayListType;
import com.anchor.app.enums.RepresentationType;

@Document(collection= "playlist")
public class HlsPlayList {
	
	@Id
	private String id;
	private String movieId;
	private String name;
	private ExtensionType extension;
	private String fullName;
	private int version;
	private int targetDuration;
	private HlsPlayListType type;
	private int mediaSegmentCount;
	private HlsResolution resolution;
	// Segment List For Java Representation
	private int segmentCount;
	private long bandWidth;
	private int avgBandWidth;
	private List<String> codecList;
	private float frameRate;
	private String audio;
	private String video;
	private String subtitles;
	private String closedCaptions;
	private RepresentationType representationType;
	
	@Transient
	private Binary data;

	public HlsPlayList(String movieId, String fullName) {
		super();
		this.movieId = movieId;
		this.fullName = fullName;
		this.id = this.movieId+"_"+this.fullName;
		codecList = new ArrayList<String>();
		//this.segmentList = new ArrayList<HlsSegment>();
	}

	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getMovieId() {
		return movieId;
	}




	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public ExtensionType getExtension() {
		return extension;
	}




	public void setExtension(ExtensionType extension) {
		this.extension = extension;
	}




	public String getFullName() {
		return fullName;
	}




	public void setFullName(String fullName) {
		this.fullName = fullName;
	}




	public int getVersion() {
		return version;
	}




	public void setVersion(int version) {
		this.version = version;
	}




	public int getTargetDuration() {
		return targetDuration;
	}




	public void setTargetDuration(int targetDuration) {
		this.targetDuration = targetDuration;
	}




	public HlsPlayListType getType() {
		return type;
	}




	public void setType(HlsPlayListType type) {
		this.type = type;
	}




	public int getMediaSegmentCount() {
		return mediaSegmentCount;
	}




	public void setMediaSegmentCount(int mediaSegmentCount) {
		this.mediaSegmentCount = mediaSegmentCount;
	}




	public HlsResolution getResolution() {
		return resolution;
	}




	public void setResolution(HlsResolution resolution) {
		this.resolution = resolution;
	}




	public int getSegmentCount() {
		return segmentCount;
	}




	public void setSegmentCount(int segmentCount) {
		this.segmentCount = segmentCount;
	}




	public long getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(long bandWidth) {
		this.bandWidth = bandWidth;
	}

	public int getAvgBandWidth() {
		return avgBandWidth;
	}




	public void setAvgBandWidth(int avgBandWidth) {
		this.avgBandWidth = avgBandWidth;
	}




	public List<String> getCodecList() {
		return codecList;
	}




	public void setCodecList(List<String> codecList) {
		this.codecList = codecList;
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




	public RepresentationType getRepresentationType() {
		return representationType;
	}




	public void setRepresentationType(RepresentationType representationType) {
		this.representationType = representationType;
	}




	public Binary getData() {
		return data;
	}




	public void setData(Binary data) {
		this.data = data;
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
		HlsPlayList other = (HlsPlayList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



}
