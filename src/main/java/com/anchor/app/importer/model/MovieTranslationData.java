package com.anchor.app.importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieTranslationData {

	@JsonProperty("homepage")
	private String homePage;
	
	private String overview;
	
	private float runtime;
	
	private String tagline;
	
	private String title;

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public float getRuntime() {
		return runtime;
	}

	public void setRuntime(float runtime) {
		this.runtime = runtime;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
