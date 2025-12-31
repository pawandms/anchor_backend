package com.anchor.app.importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbGenre {

	@JsonProperty("id")
	private String tmdbId;
	private String name;
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
	
	
}

