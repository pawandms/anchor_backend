package com.anchor.app.importer.model;

import java.util.Date;

import com.anchor.app.util.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TmdbSeason {

	@JsonDeserialize(using=CustomDateDeserializer.class)
	private Date air_date;
	
	private int episode_count;
	
	@JsonProperty("id")
	private String tmdbId;
	
	private String name;
	private String overview;
	private String poster_path;
	private int season_number;
	public Date getAir_date() {
		return air_date;
	}
	public void setAir_date(Date air_date) {
		this.air_date = air_date;
	}
	public int getEpisode_count() {
		return episode_count;
	}
	public void setEpisode_count(int episode_count) {
		this.episode_count = episode_count;
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
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	public String getPoster_path() {
		return poster_path;
	}
	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}
	public int getSeason_number() {
		return season_number;
	}
	public void setSeason_number(int season_number) {
		this.season_number = season_number;
	}
	
	
	
}
