package com.anchor.app.importer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anchor.app.model.Genre;
import com.anchor.app.model.MediaLanguage;
import com.anchor.app.model.Production_Company;
import com.anchor.app.model.Production_Country;
import com.anchor.app.model.Translation;
import com.anchor.app.util.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TmdbMovie {
	
	private String id;
	
	@JsonProperty("id")
	private String tmdbId;
	private boolean adult;
	private String backdrop_path;
	private String poster_path;
	private String backdrop_pathId;
	private String poster_pathId;
	
	@JsonProperty("belongs_to_collection")
	private BelongsToCollection belongs_to_collection;
	private float budget;
	
	@JsonProperty("genres")
	private List<Genre> genres;
	
	private String homepage;
	
	@JsonProperty("imdb_id")
	private String imdbId;
	
	private String original_language;
	
	private MediaLanguage orgMediaLanguage;
	
	private String original_title;
	private String overview;
	private float popularity;
	
	@JsonProperty("production_companies")
	private List<Production_Company> production_companies;
	
	@JsonProperty("production_countries")
	private List<Production_Country> production_countries;
	
	 @JsonDeserialize(using=CustomDateDeserializer.class)
	 private Date release_date;
	 
	 private float revenue;
	 
	 private float runtime;
	
	 @JsonProperty("spoken_languages")
	 private List<MediaLanguage> spoken_languages;
	 
	 private String status;
	 private String tagline;
	 private String title;
	 private float vote_average;
	 private float vote_count;
	
	 /** 
	  * Translation for Respective Movie Which will be Fetch from Transaltion API 
	  */
	 private Translation translation;
	 
	 public TmdbMovie() {
		super();
		
		 this.genres = new ArrayList<Genre>();
		 this.production_companies = new ArrayList<Production_Company>(); 
		 this.production_countries = new ArrayList<Production_Country>() ;
		 this.spoken_languages = new ArrayList<MediaLanguage>(); 
	}

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


	
	public boolean isAdult() {
		return adult;
	}


	public void setAdult(boolean adult) {
		this.adult = adult;
	}


	public String getBackdrop_path() {
		return backdrop_path;
	}


	public void setBackdrop_path(String backdrop_path) {
		this.backdrop_path = backdrop_path;
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


	public BelongsToCollection getBelongs_to_collection() {
		return belongs_to_collection;
	}


	public void setBelongs_to_collection(BelongsToCollection belongs_to_collection) {
		this.belongs_to_collection = belongs_to_collection;
	}


	public float getBudget() {
		return budget;
	}


	public void setBudget(float budget) {
		this.budget = budget;
	}


	public List<Genre> getGenres() {
		return genres;
	}


	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}


	public String getHomepage() {
		return homepage;
	}


	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}


	public String getImdbId() {
		return imdbId;
	}


	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}


	public String getOriginal_language() {
		return original_language;
	}


	public void setOriginal_language(String original_language) {
		this.original_language = original_language;
	}

	public MediaLanguage getOrgMediaLanguage() {
		return orgMediaLanguage;
	}



	public void setOrgMediaLanguage(MediaLanguage orgMediaLanguage) {
		this.orgMediaLanguage = orgMediaLanguage;
	}



	public String getOriginal_title() {
		return original_title;
	}


	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}


	public String getOverview() {
		return overview;
	}


	public void setOverview(String overview) {
		this.overview = overview;
	}


	public float getPopularity() {
		return popularity;
	}


	public void setPopularity(float popularity) {
		this.popularity = popularity;
	}


	public String getPoster_path() {
		return poster_path;
	}


	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}


	public List<Production_Company> getProduction_companies() {
		return production_companies;
	}


	public void setProduction_companies(List<Production_Company> production_companies) {
		this.production_companies = production_companies;
	}


	public List<Production_Country> getProduction_countries() {
		return production_countries;
	}


	public void setProduction_countries(List<Production_Country> production_countries) {
		this.production_countries = production_countries;
	}


	public Date getRelease_date() {
		return release_date;
	}


	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}


	public float getRevenue() {
		return revenue;
	}


	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}


	public float getRuntime() {
		return runtime;
	}


	public void setRuntime(float runtime) {
		this.runtime = runtime;
	}


	public List<MediaLanguage> getSpoken_languages() {
		return spoken_languages;
	}


	public void setSpoken_languages(List<MediaLanguage> spoken_languages) {
		this.spoken_languages = spoken_languages;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
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


	public float getVote_average() {
		return vote_average;
	}


	public void setVote_average(float vote_average) {
		this.vote_average = vote_average;
	}


	public float getVote_count() {
		return vote_count;
	}


	public void setVote_count(float vote_count) {
		this.vote_count = vote_count;
	}



	public Translation getTranslation() {
		return translation;
	}



	public void setTranslation(Translation translation) {
		this.translation = translation;
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
		TmdbMovie other = (TmdbMovie) obj;
		if (tmdbId == null) {
			if (other.tmdbId != null)
				return false;
		} else if (!tmdbId.equals(other.tmdbId))
			return false;
		return true;
	}


	
	 
	 
	 
}

