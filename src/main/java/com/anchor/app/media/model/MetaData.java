package com.anchor.app.media.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MetaData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870652846897300952L;
	private String mediaId;
	private List<String> metaDataList;
	
	
	
	public MetaData() {
		super();
		this.metaDataList = new ArrayList<>();
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public List<String> getMetaDataList() {
		return metaDataList;
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
		MetaData other = (MetaData) obj;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		return true;
	}
	
	
	
	
}
