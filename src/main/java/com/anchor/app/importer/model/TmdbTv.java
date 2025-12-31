package com.anchor.app.importer.model;

import java.util.Date;
import java.util.List;

import com.anchor.app.model.Genre;
import com.anchor.app.model.MediaLanguage;
import com.anchor.app.model.Production_Country;
import com.anchor.app.util.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TmdbTv {

	@JsonProperty("id")
	private String tmdbId;
	
	private String backdrop_path;
	
	@JsonProperty("created_by")
	private List<PersonCredit> createdBy;

	@JsonProperty("episode_run_time")
	private List<Double> episodeRunTime;

	@JsonDeserialize(using=CustomDateDeserializer.class)
	private Date first_air_date;
	
	@JsonProperty("genres")
	private List<TmdbGenre> genreList;

	private String homepage;
	
	private boolean in_production;
	
	@JsonProperty("languages")
	private List<String> languages;
	
	@JsonProperty("last_episode_to_air")
	private TmdbEpisode last_episode_to_air;
	
	private String name;
	
	@JsonProperty("next_episode_to_air")
	private TmdbEpisode next_episode_to_air;
	
	@JsonProperty("networks")
	private List<TmdbCompany> networkList;
	
	private int number_of_episodes;
	
	private int number_of_seasons;
	
	@JsonProperty("origin_country")
	private List<String> origin_countryList;
	
	private String original_language;
	
	private String original_name;
	private String overview;
	
	private double popularity;
	private String poster_path;
	
	@JsonProperty("production_companies")
	private List<TmdbCompany> productionCompanyList;
	
	@JsonProperty("production_countries")
	private List<Production_Country> productionCountryList;
	
	@JsonProperty("seasons")
	private List<TmdbSeason> seasonList;
	
	@JsonProperty("spoken_languages")
	private List<MediaLanguage> spokenLanguageList;
	
	private String status;
	private String tagline;
	private String type;
	private double vote_average;
	private int vote_count;
	public String getTmdbId() {
		return tmdbId;
	}
	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}
	public String getBackdrop_path() {
		return backdrop_path;
	}
	public void setBackdrop_path(String backdrop_path) {
		this.backdrop_path = backdrop_path;
	}
	public List<PersonCredit> getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(List<PersonCredit> createdBy) {
		this.createdBy = createdBy;
	}
	public List<Double> getEpisodeRunTime() {
		return episodeRunTime;
	}
	public void setEpisodeRunTime(List<Double> episodeRunTime) {
		this.episodeRunTime = episodeRunTime;
	}
	public Date getFirst_air_date() {
		return first_air_date;
	}
	public void setFirst_air_date(Date first_air_date) {
		this.first_air_date = first_air_date;
	}
	public List<TmdbGenre> getGenreList() {
		return genreList;
	}
	public void setGenreList(List<TmdbGenre> genreList) {
		this.genreList = genreList;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public boolean isIn_production() {
		return in_production;
	}
	public void setIn_production(boolean in_production) {
		this.in_production = in_production;
	}
	public List<String> getLanguages() {
		return languages;
	}
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	public TmdbEpisode getLast_episode_to_air() {
		return last_episode_to_air;
	}
	public void setLast_episode_to_air(TmdbEpisode last_episode_to_air) {
		this.last_episode_to_air = last_episode_to_air;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TmdbEpisode getNext_episode_to_air() {
		return next_episode_to_air;
	}
	public void setNext_episode_to_air(TmdbEpisode next_episode_to_air) {
		this.next_episode_to_air = next_episode_to_air;
	}
	public List<TmdbCompany> getNetworkList() {
		return networkList;
	}
	public void setNetworkList(List<TmdbCompany> networkList) {
		this.networkList = networkList;
	}
	public int getNumber_of_episodes() {
		return number_of_episodes;
	}
	public void setNumber_of_episodes(int number_of_episodes) {
		this.number_of_episodes = number_of_episodes;
	}
	public int getNumber_of_seasons() {
		return number_of_seasons;
	}
	public void setNumber_of_seasons(int number_of_seasons) {
		this.number_of_seasons = number_of_seasons;
	}
	public List<String> getOrigin_countryList() {
		return origin_countryList;
	}
	public void setOrigin_countryList(List<String> origin_countryList) {
		this.origin_countryList = origin_countryList;
	}
	public String getOriginal_language() {
		return original_language;
	}
	public void setOriginal_language(String original_language) {
		this.original_language = original_language;
	}
	public String getOriginal_name() {
		return original_name;
	}
	public void setOriginal_name(String original_name) {
		this.original_name = original_name;
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	public double getPopularity() {
		return popularity;
	}
	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	public String getPoster_path() {
		return poster_path;
	}
	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}
	public List<TmdbCompany> getProductionCompanyList() {
		return productionCompanyList;
	}
	public void setProductionCompanyList(List<TmdbCompany> productionCompanyList) {
		this.productionCompanyList = productionCompanyList;
	}
	public List<Production_Country> getProductionCountryList() {
		return productionCountryList;
	}
	public void setProductionCountryList(List<Production_Country> productionCountryList) {
		this.productionCountryList = productionCountryList;
	}
	public List<TmdbSeason> getSeasonList() {
		return seasonList;
	}
	public void setSeasonList(List<TmdbSeason> seasonList) {
		this.seasonList = seasonList;
	}
	public List<MediaLanguage> getSpokenLanguageList() {
		return spokenLanguageList;
	}
	public void setSpokenLanguageList(List<MediaLanguage> spokenLanguageList) {
		this.spokenLanguageList = spokenLanguageList;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
