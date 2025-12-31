package com.anchor.app.model.hls;

import java.util.ArrayList;
import java.util.List;

import com.anchor.app.enums.MediaType;
import com.anchor.app.hls.model.Hls_PlayList;
import com.anchor.app.hls.model.Hls_Segment;

public class Hls_MediaRepresentation {
	
	private String mediaId;
	private String mediaName;
	private MediaType mediaType;
	
	private List<Hls_PlayList> hlsPlayList;
	private List<Hls_Segment> hlsSegmentList;
	
	private int persistedPlayListCount;
	private int persistedSegmentCount;
	private boolean isplayListPersisted;
	private boolean issegmentsPersisted;

	// Location of Bucket where this Media is Stored
	private String bucketName;
	
	public Hls_MediaRepresentation(String mediaId) {
		super();
		this.mediaId = mediaId;
		this.hlsPlayList = new ArrayList<Hls_PlayList>();
		this.hlsSegmentList = new ArrayList<Hls_Segment>();
		
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaId() {
		return mediaId;
	}
	
	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public List<Hls_PlayList> getHlsPlayList() {
		return hlsPlayList;
	}

	public List<Hls_Segment> getHlsSegmentList() {
		return hlsSegmentList;
	}
	
	public boolean isIsplayListPersisted() {
		return isplayListPersisted;
	}

	public void setIsplayListPersisted(boolean isplayListPersisted) {
		this.isplayListPersisted = isplayListPersisted;
	}

	public boolean isIssegmentsPersisted() {
		return issegmentsPersisted;
	}

	public void setIssegmentsPersisted(boolean issegmentsPersisted) {
		this.issegmentsPersisted = issegmentsPersisted;
	}
	
	

	public int getPersistedPlayListCount() {
		return persistedPlayListCount;
	}

	public void setPersistedPlayListCount(int persistedPlayListCount) {
		this.persistedPlayListCount = persistedPlayListCount;
	}

	public int getPersistedSegmentCount() {
		return persistedSegmentCount;
	}

	public void setPersistedSegmentCount(int persistedSegmentCount) {
		this.persistedSegmentCount = persistedSegmentCount;
	}
	
	

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mediaId == null) ? 0 : mediaId.hashCode());
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
		Hls_MediaRepresentation other = (Hls_MediaRepresentation) obj;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		return true;
	}
	
	
	

}
