package com.anchor.app.model;

import org.springframework.data.annotation.Id;

import com.anchor.app.enums.MediaStreamType;
import com.anchor.app.enums.PlayListType;

@Deprecated
public class PlayList {

	@Id
	private String id;
	private PlayListType type;
	private MediaStreamType stream_type;
	private String path;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PlayListType getType() {
		return type;
	}
	public void setType(PlayListType type) {
		this.type = type;
	}
	public MediaStreamType getStream_type() {
		return stream_type;
	}
	public void setStream_type(MediaStreamType stream_type) {
		this.stream_type = stream_type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	
}
