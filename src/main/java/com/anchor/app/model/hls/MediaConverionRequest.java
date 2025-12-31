package com.anchor.app.model.hls;

import com.anchor.app.enums.ExtensionType;
import com.iheartradio.m3u8.data.MasterPlaylist;

public class MediaConverionRequest {
	
	private String id;
	private String mediaId;
	private String name;
	private ExtensionType extension;
	private String fullName;
	private int version;
	private boolean isExtended;
	private MasterPlaylist mstp;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
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
	public boolean isExtended() {
		return isExtended;
	}
	public void setExtended(boolean isExtended) {
		this.isExtended = isExtended;
	}
	public MasterPlaylist getMstp() {
		return mstp;
	}
	public void setMstp(MasterPlaylist mstp) {
		this.mstp = mstp;
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
		MediaConverionRequest other = (MediaConverionRequest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
