package com.anchor.app.importer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbCollection {

	private String id;
	
	@JsonProperty("id")
	private String tmdbId;

	private String name;
	private String overview;
	private String backdrop_path;
	private String poster_path;
	private String backdrop_pathId;
	private String poster_pathId;
	private float popularity;

	@JsonProperty("parts")
	private List<TmdbMovie> partList;

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

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getBackdrop_path() {
		return backdrop_path;
	}

	public void setBackdrop_path(String backdrop_path) {
		this.backdrop_path = backdrop_path;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}

	public String getBackdrop_pathId() {
		return backdrop_pathId;
	}

	public void setBackdrop_pathId(String backdrop_pathId) {
		this.backdrop_pathId = backdrop_pathId;
	}

	public String getPoster_pathId() {
		return poster_pathId;
	}

	public void setPoster_pathId(String poster_pathId) {
		this.poster_pathId = poster_pathId;
	}

	public float getPopularity() {
		return popularity;
	}

	public void setPopularity(float popularity) {
		this.popularity = popularity;
	}

	public List<TmdbMovie> getPartList() {
		return partList;
	}

	public void setPartList(List<TmdbMovie> partList) {
		this.partList = partList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tmdbId == null) ? 0 : tmdbId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TmdbCollection other = (TmdbCollection) obj;
		if (tmdbId == null) {
			if (other.tmdbId != null)
				return false;
		} else if (!tmdbId.equals(other.tmdbId))
			return false;
		return true;
	}
	
	
	
	
}
