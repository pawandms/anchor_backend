package com.anchor.app.importer.model;

import com.anchor.app.enums.CollectionType;

public class BelongsToCollection {

	
	private String id;
	
	// Type of Collection, Movie, Music, 
	private CollectionType type;
	private String tmdbId;
	private String name;
	private String backdropImageId;
	private String postarImageId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTmdbId() {
		return tmdbId;
	}
	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBackdropImageId() {
		return backdropImageId;
	}
	public void setBackdropImageId(String backdropImageId) {
		this.backdropImageId = backdropImageId;
	}
	public String getPostarImageId() {
		return postarImageId;
	}
	public void setPostarImageId(String postarImageId) {
		this.postarImageId = postarImageId;
	}
	
	
	
}
