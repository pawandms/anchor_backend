package com.anchor.app.hls.model;

import java.nio.file.Path;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "hls_playlist")
public class Hls_PlayList {
	
	// ID is mediaId_{playListName} i.e 100_master.m3u8 or 100_
	
	@Id
	private String id;
	private String mediaId;
	private String mediaName;
	
	// Name of m3u8 File i.e. master.m3u8, stream_0.m3u8
	private String playListName;
	
	@Transient
	private Path playListPath;

	
	// .m3u8 File String representation
	private String playListTxt;

	public Hls_PlayList(String mediaId, String playListName) 
	{
		super();
		this.mediaId = mediaId;
		this.playListName = playListName;
		this.id = this.mediaId+"_"+playListName;
		
	}

	public String getId() {
		return id;
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

	public String getPlayListName() {
		return playListName;
	}

	public void setPlayListName(String playListName) {
		this.playListName = playListName;
	}

	public String getPlayListTxt() {
		return playListTxt;
	}

	public void setPlayListTxt(String playListTxt) {
		this.playListTxt = playListTxt;
	}
	
		
	public Path getPlayListPath() {
		return playListPath;
	}

	public void setPlayListPath(Path playListPath) {
		this.playListPath = playListPath;
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
		Hls_PlayList other = (Hls_PlayList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
