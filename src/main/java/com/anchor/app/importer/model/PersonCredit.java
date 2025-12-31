package com.anchor.app.importer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonCredit {

	@JsonProperty("id")
	private String tmdbId;

	private String peopleId;
	private boolean isPresent;
	
	
	private String credit_id;
	private String name;
	private int gender;
	private String profile_path;
	
	public String getTmdbId() {
		return tmdbId;
	}
	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}
	
	
	public String getPeopleId() {
		return peopleId;
	}
	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}
	public boolean isPresent() {
		return isPresent;
	}
	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}
	public String getCredit_id() {
		return credit_id;
	}
	public void setCredit_id(String credit_id) {
		this.credit_id = credit_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getProfile_path() {
		return profile_path;
	}
	public void setProfile_path(String profile_path) {
		this.profile_path = profile_path;
	}
	
	
	
}
