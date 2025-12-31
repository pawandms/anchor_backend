package com.anchor.app.media.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ExternalSystemType;
import com.anchor.app.enums.GenderType;
import com.anchor.app.enums.MediaFormatType;
import com.anchor.app.enums.MediaGenreType;
import com.anchor.app.enums.MediaType;
import com.anchor.app.enums.StatusType;
import com.anchor.app.model.Genre;
import com.anchor.app.model.MediaLanguage;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection= "media")
public class Media {

	@Id
	private String id;

	private MediaType mediaType;
	private boolean active;
	private boolean isAdult;
	
	private String name;
	private String title;
	private String originalTitle;
	private String tagLine;
	private String homePage;
	private String overview;
	private double popularity;
	private Date release_date;
	private double runtime;
	private StatusType status;
	private double revenue;
	private double budget;
	private double vote_average;
	private int vote_count;
	private int view_count;
	private MediaLanguage originalLanguage;
	
	// Applicable to Maintain Media Order into Collection
	private int order;
	
	
	// Applicable for TV Shows
	private String tvId;
	private int seasonNo;
	private int episodeNo;
	private Date airDate;
	
	
	// Language Spoken in this Movie
	private List<MediaLanguage> spokenLanguageList;
	
	// Available Images for Media
	private List<MediaImage> imageList;
	
	private List<MediaGenreType> generList;

	private MetaData metaData;
	
	@JsonIgnore
	private ExternalSystemType sourceSystemType;
	
	@JsonIgnore
	private Map<String,String> externalSystemIdentifierMap;
	
	private boolean isVideoPresent;
	private String bucketName;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;

	private MediaFormatType mediaFormat;
	
	/**
	 * Unique ID for Content Store
	 */
	private String contentID;

	private long sizeInBytes; 

	public Media() {
		this.spokenLanguageList = new ArrayList<MediaLanguage>() ;
		this.imageList = new ArrayList<MediaImage>();
		this.generList = new ArrayList<MediaGenreType>();
		this.externalSystemIdentifierMap = new HashMap<String,String>();
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
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

	
	
	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public MediaLanguage getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(MediaLanguage originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getTvId() {
		return tvId;
	}

	public void setTvId(String tvId) {
		this.tvId = tvId;
	}

	public int getSeasonNo() {
		return seasonNo;
	}

	public void setSeasonNo(int seasonNo) {
		this.seasonNo = seasonNo;
	}

	public int getEpisodeNo() {
		return episodeNo;
	}

	public void setEpisodeNo(int episodeNo) {
		this.episodeNo = episodeNo;
	}

	public Date getAirDate() {
		return airDate;
	}

	public void setAirDate(Date airDate) {
		this.airDate = airDate;
	}

	public boolean isVideoPresent() {
		return isVideoPresent;
	}

	public void setVideoPresent(boolean isVideoPresent) {
		this.isVideoPresent = isVideoPresent;
	}
	
	
	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
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

	public List<MediaLanguage> getSpokenLanguageList() {
		return spokenLanguageList;
	}

	public List<MediaImage> getImageList() {
		return imageList;
	}

	
	
	public List<MediaGenreType> getGenerList() {
		return generList;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public ExternalSystemType getSourceSystemType() {
		return sourceSystemType;
	}

	public void setSourceSystemType(ExternalSystemType sourceSystemType) {
		this.sourceSystemType = sourceSystemType;
	}

	public Map<String, String> getExternalSystemIdentifierMap() {
		return externalSystemIdentifierMap;
	}

	
	
	public MediaFormatType getMediaFormat() {
		return mediaFormat;
	}

	public void setMediaFormat(MediaFormatType mediaFormat) {
		this.mediaFormat = mediaFormat;
	}

	public String getContentID() {
		return contentID;
	}

	public void setContentID(String contentID) {
		this.contentID = contentID;
	}

	public long getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
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
		Media other = (Media) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
	
	
	
}
