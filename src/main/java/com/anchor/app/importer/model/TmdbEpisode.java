package com.anchor.app.importer.model;

import java.util.Date;
import java.util.List;

import com.anchor.app.util.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TmdbEpisode {

	@JsonDeserialize(using=CustomDateDeserializer.class)
	private Date air_date;
	
	@JsonProperty("crew")
	private List<TmdbCrew> crewList;
	
	private int episode_number;
	
	@JsonProperty("guest_stars")
	private List<TmdbCast> guestStartsList;
	
	private String name;
	private String overview;
	
	@JsonProperty("id")
	private String tmdbId;

	private String production_code;
	
	private int season_number;
	
	private String still_path;
	
	private double vote_average;
	private int vote_count;
	public Date getAir_date() {
		return air_date;
	}
	public void setAir_date(Date air_date) {
		this.air_date = air_date;
	}
	public List<TmdbCrew> getCrewList() {
		return crewList;
	}
	public void setCrewList(List<TmdbCrew> crewList) {
		this.crewList = crewList;
	}
	public int getEpisode_number() {
		return episode_number;
	}
	public void setEpisode_number(int episode_number) {
		this.episode_number = episode_number;
	}
	public List<TmdbCast> getGuestStartsList() {
		return guestStartsList;
	}
	public void setGuestStartsList(List<TmdbCast> guestStartsList) {
		this.guestStartsList = guestStartsList;
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
	public String getTmdbId() {
		return tmdbId;
	}
	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}
	public String getProduction_code() {
		return production_code;
	}
	public void setProduction_code(String production_code) {
		this.production_code = production_code;
	}
	public int getSeason_number() {
		return season_number;
	}
	public void setSeason_number(int season_number) {
		this.season_number = season_number;
	}
	public String getStill_path() {
		return still_path;
	}
	public void setStill_path(String still_path) {
		this.still_path = still_path;
	}
	public double getVote_average() {
		return vote_average;
	}
	public void setVote_average(double vote_average) {
		this.vote_average = vote_average;
	}
	public int getVote_count() {
		return vote_count;
	}
	public void setVote_count(int vote_count) {
		this.vote_count = vote_count;
	}
	
	
	
	

}
