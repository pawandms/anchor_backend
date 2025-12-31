package com.anchor.app.model.tv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.MediaStreamType;
import com.anchor.app.enums.StatusType;
import com.anchor.app.model.Company;
import com.anchor.app.model.Country;
import com.anchor.app.model.Genre;
import com.anchor.app.model.Language;

@Document(collection= "tv")
public class Tv {
	
	@Id
	private String id;
	private String tmdbId;
	private boolean isAdult;
	private String name;
	private String tagline;
	private String overview;
	private String type;
	private String homepage;
	
	private Language originalLanguage;
	private String originalName;
	private boolean inProduction;
	
	private int totalSeasons;
	private int totalEpisodes;
	
	private List<Company> networkList;
	private List<Company> productionCompanyList;
	private List<Country> productionCountryList;
	private List<Language> spokenLangagueList;
	private List<Genre> generList;
	
	private Date firstAirDate;
	private Date lastAirDate;
	
	private String posterId;
	private String backdropId;

	private double popularity;
	private StatusType status;
	private double revenue;
	private double budget;
	private double averageVote;
	private int voteCount;
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;

	
	public Tv() {
		super();
		this.networkList = new ArrayList<Company>();
		this.productionCompanyList = new ArrayList<Company>() ;
		this.productionCountryList = new ArrayList<Country>() ;
		this.spokenLangagueList = new ArrayList<Language>();
		this.generList = new ArrayList<Genre>();
		
		
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


	public boolean isAdult() {
		return isAdult;
	}


	public void setAdult(boolean isAdult) {
		this.isAdult = isAdult;
	}


	public String getHomepage() {
		return homepage;
	}


	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}


	public Language getOriginalLanguage() {
		return originalLanguage;
	}


	public void setOriginalLanguage(Language originalLanguage) {
		this.originalLanguage = originalLanguage;
	}


	public String getOriginalName() {
		return originalName;
	}


	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}


	public boolean isInProduction() {
		return inProduction;
	}


	public void setInProduction(boolean inProduction) {
		this.inProduction = inProduction;
	}


	public int getTotalSeasons() {
		return totalSeasons;
	}


	public void setTotalSeasons(int totalSeasons) {
		this.totalSeasons = totalSeasons;
	}


	public int getTotalEpisodes() {
		return totalEpisodes;
	}


	public void setTotalEpisodes(int totalEpisodes) {
		this.totalEpisodes = totalEpisodes;
	}


	public List<Genre> getGenerList() {
		return generList;
	}


	public void setGenerList(List<Genre> generList) {
		this.generList = generList;
	}


	public Date getFirstAirDate() {
		return firstAirDate;
	}


	public void setFirstAirDate(Date firstAirDate) {
		this.firstAirDate = firstAirDate;
	}


	public Date getLastAirDate() {
		return lastAirDate;
	}


	public void setLastAirDate(Date lastAirDate) {
		this.lastAirDate = lastAirDate;
	}


	public String getPosterId() {
		return posterId;
	}


	public void setPosterId(String posterId) {
		this.posterId = posterId;
	}


	public String getBackdropId() {
		return backdropId;
	}


	public void setBackdropId(String backdropId) {
		this.backdropId = backdropId;
	}


	public double getPopularity() {
		return popularity;
	}


	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}


	public StatusType getStatus() {
		return status;
	}


	public void setStatus(StatusType status) {
		this.status = status;
	}


	public double getRevenue() {
		return revenue;
	}


	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}


	public double getBudget() {
		return budget;
	}


	public void setBudget(double budget) {
		this.budget = budget;
	}


	public double getAverageVote() {
		return averageVote;
	}


	public void setAverageVote(double averageVote) {
		this.averageVote = averageVote;
	}


	public int getVoteCount() {
		return voteCount;
	}


	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public Date getModifiedOn() {
		return modifiedOn;
	}


	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}


	public List<Company> getNetworkList() {
		return networkList;
	}


	public List<Company> getProductionCompanyList() {
		return productionCompanyList;
	}


	public List<Country> getProductionCountryList() {
		return productionCountryList;
	}


	public List<Language> getSpokenLangagueList() {
		return spokenLangagueList;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Tv other = (Tv) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
