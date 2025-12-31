package com.anchor.app.importer.model;

import java.util.ArrayList;
import java.util.List;

import com.anchor.app.model.PersonCredit;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbMovieCredit {

	@JsonProperty("id")
	private String tmdbId;
	
	@JsonProperty("cast")
	private List<TmdbCast> castList;
	
	@JsonProperty("crew")
	private List<TmdbCrew> crewList;

	// PersonCredtiList Popuplate during Transformation during storing into DB
	private List<PersonCredit> perCreditList;
	
	
	
	
	public TmdbMovieCredit() {
		super();
		this.perCreditList = new ArrayList<PersonCredit>();
	}
	public String getTmdbId() {
		return tmdbId;
	}
	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}
	public List<TmdbCast> getCastList() {
		return castList;
	}
	public void setCastList(List<TmdbCast> castList) {
		this.castList = castList;
	}
	public List<TmdbCrew> getCrewList() {
		return crewList;
	}
	public void setCrewList(List<TmdbCrew> crewList) {
		this.crewList = crewList;
	}
	
	
	
}
