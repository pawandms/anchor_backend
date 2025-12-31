package com.anchor.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.MediaStreamType;
import com.anchor.app.enums.StatusType;
import com.anchor.app.importer.model.BelongsToCollection;

import jakarta.validation.constraints.NotNull;

@Document(collection= "movie")
public class Movie {
	
	@Id
	private String id;
	
	private String tmdbId;
	private boolean isAdult;
	
	@NotNull
	private String name;
	private String title;
	private String tagLine;
	private String homePage;
	private String originalTitle;
	private String imdbId;
	
	// Original Language of Movie
	private MediaLanguage originalLanguage;
	
	private BelongsToCollection belongstocollection;
	
	// Language Spoken in this Movie
	private List<MediaLanguage> spokenLanguageList;
	
	private String backdropImageId;
	private String postarImageId;
	
	/**
	 * id for collection this movies is part of
	 */
	private String collectionId;
	
	private List<Genre> generList;
	
	private String overview;
	private double popularity;
	private Date release_date;
	private double runtime;
	private StatusType status;
	private double revenue;
	private double budget;
	private double vote_average;
	private int vote_count;
	
	private List<Production_Company> productionCompanieList;
	private List<Production_Country> productionCountryList;
	
	// Flag to Indicate is Given Movie is present into System
	private boolean isVideoPresent;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	private boolean iscreditProcessed;
		
	
	public Movie() {
		super();
		this.generList = new ArrayList<Genre>();
		this.spokenLanguageList = new ArrayList<MediaLanguage>(); 
		this.productionCompanieList = new ArrayList<Production_Company>() ;
		this.productionCountryList = new ArrayList<Production_Country>();
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
		return isAdult;
	}



	public void setAdult(boolean isAdult) {
		this.isAdult = isAdult;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getTagLine() {
		return tagLine;
	}



	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}



	public String getHomePage() {
		return homePage;
	}



	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}



	public String getOriginalTitle() {
		return originalTitle;
	}



	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}



	public String getImdbId() {
		return imdbId;
	}



	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}



	public MediaLanguage getOriginalLanguage() {
		return originalLanguage;
	}



	public void setOriginalLanguage(MediaLanguage originalLanguage) {
		this.originalLanguage = originalLanguage;
	}


	public BelongsToCollection getBelongstocollection() {
		return belongstocollection;
	}



	public void setBelongstocollection(BelongsToCollection belongstocollection) {
		this.belongstocollection = belongstocollection;
	}



	public String getBackdropImageId() {
		return backdropImageId;
	}



	public void setBackdropImageId(String backdropImageId) {
		this.backdropImageId = backdropImageId;
	}



	public String getPostarImageId() {
		return postarImageId;
	}



	public void setPostarImageId(String postarImageId) {
		this.postarImageId = postarImageId;
	}



	public String getCollectionId() {
		return collectionId;
	}



	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
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



	public Date getRelease_date() {
		return release_date;
	}



	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}



	public double getRuntime() {
		return runtime;
	}



	public void setRuntime(double runtime) {
		this.runtime = runtime;
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



	public List<MediaLanguage> getSpokenLanguageList() {
		return spokenLanguageList;
	}


	public List<Genre> getGenerList() {
		return generList;
	}


	public List<Production_Company> getProductionCompanieList() {
		return productionCompanieList;
	}


	public List<Production_Country> getProductionCountryList() {
		return productionCountryList;
	}

	
	public boolean isVideoPresent() {
		return isVideoPresent;
	}



	public void setVideoPresent(boolean isVideoPresent) {
		this.isVideoPresent = isVideoPresent;
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


	

	public boolean isIscreditProcessed() {
		return iscreditProcessed;
	}



	public void setIscreditProcessed(boolean iscreditProcessed) {
		this.iscreditProcessed = iscreditProcessed;
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
		Movie other = (Movie) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
	
}
