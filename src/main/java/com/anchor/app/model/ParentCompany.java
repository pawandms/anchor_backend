package com.anchor.app.model;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public class ParentCompany {

	@Id
	private String id;
	
	@JsonProperty("tmdb_id")
	private String tmdb_id;
	
	@NotNull
	private String name;
	
	@JsonProperty("logo_path")
	private String tmdb_logo_path;
	
	private String image_id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTmdb_id() {
		return tmdb_id;
	}
	public void setTmdb_id(String tmdb_id) {
		this.tmdb_id = tmdb_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTmdb_logo_path() {
		return tmdb_logo_path;
	}
	public void setTmdb_logo_path(String tmdb_logo_path) {
		this.tmdb_logo_path = tmdb_logo_path;
	}
	public String getImage_id() {
		return image_id;
	}
	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}
	@Override
	public String toString() {
		return "ParentCompany [id=" + id + ", tmdb_id=" + tmdb_id + ", name=" + name + ", tmdb_logo_path="
				+ tmdb_logo_path + ", image_id=" + image_id + "]";
	}
	
	
	
}
