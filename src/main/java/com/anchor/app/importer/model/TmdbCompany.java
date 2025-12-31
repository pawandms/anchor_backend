package com.anchor.app.importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbCompany {

	private String headquarters;
	private String homepage;
	
	@JsonProperty("id")
	private String tmdbId;
	
	private String logo_path;
	
	private String name;
	private String origin_country;
	public String getHeadquarters() {
		return headquarters;
	}
	public void setHeadquarters(String headquarters) {
		this.headquarters = headquarters;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getTmdbId() {
		return tmdbId;
	}
	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}
	public String getLogo_path() {
		return logo_path;
	}
	public void setLogo_path(String logo_path) {
		this.logo_path = logo_path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrigin_country() {
		return origin_country;
	}
	public void setOrigin_country(String origin_country) {
		this.origin_country = origin_country;
	}
	
	
	
}
