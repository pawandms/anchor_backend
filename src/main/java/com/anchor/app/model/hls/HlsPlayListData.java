package com.anchor.app.model.hls;

public class HlsPlayListData {
	
	private String id;
	private String mediaId;
	
	private String uri;
	private HlsStreamInfo streamInfo;
	
	public HlsPlayListData(String id, String mediaId) {
		super();
		this.id = id;
		this.mediaId = mediaId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public HlsStreamInfo getStreamInfo() {
		return streamInfo;
	}

	public void setStreamInfo(HlsStreamInfo streamInfo) {
		this.streamInfo = streamInfo;
	}

	public String getId() {
		return id;
	}

	public String getMediaId() {
		return mediaId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		HlsPlayListData other = (HlsPlayListData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		return true;
	}

	
	
}
