package com.anchor.app.importer.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.anchor.app.enums.MediaType;
import com.anchor.app.util.CustomDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class YouTubeMediaInfo {

	
	@Id
	private String id;

	private MediaType mediaType;
	
	private String youTubeId;
	
	@JsonProperty("webpage_url")
	private String webPageUrl;
	
	@JsonProperty("display_id")
	private String displayId;
	
	private String title;
	
	@JsonProperty("fulltitle")
	private String fullTitle;
	
	
	@JsonProperty("channel_id")
	private String channelId;
	
	@JsonProperty("channel_url")
	private String channelUrl;
	
	@JsonProperty("alt_title")
	private String alternateTitle;
	
	private String creator;
	private String artist;
	
	
	@JsonProperty("description")
	private String overview;
	
	@JsonProperty("uploader_id")
	private String uploaderId;
	
	@JsonDeserialize(using=CustomDateDeserializer.class)
	@JsonProperty("upload_date")
	private Date uploadDate;
	
	@JsonDeserialize(using=CustomDateDeserializer.class)
	@JsonProperty("release_date")
	private Date releaseDate;
	
	private int duration;

	@JsonProperty("thumbnails")
	private List<YouTubeMediaImage> thumbnails;
	
	
	public YouTubeMediaInfo() {
		super();
		// Default Media Type
		this.mediaType = MediaType.Music_Audio;
		
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

	public String getYouTubeId() {
		return youTubeId;
	}

	public void setYouTubeId(String youTubeId) {
		this.youTubeId = youTubeId;
	}
	
	

	public String getWebPageUrl() {
		return webPageUrl;
	}

	public void setWebPageUrl(String webPageUrl) {
		this.webPageUrl = webPageUrl;
	}

	public String getDisplayId() {
		return displayId;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullTitle() {
		return fullTitle;
	}

	public void setFullTitle(String fullTitle) {
		this.fullTitle = fullTitle;
	}


	public String getChannelUrl() {
		return channelUrl;
	}

	public void setChannelUrl(String channelUrl) {
		this.channelUrl = channelUrl;
	}
	
	

	


	public String getAlternateTitle() {
		return alternateTitle;
	}

	public void setAlternateTitle(String alternateTitle) {
		this.alternateTitle = alternateTitle;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(String uploaderId) {
		this.uploaderId = uploaderId;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<YouTubeMediaImage> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(List<YouTubeMediaImage> thumbnails) {
		this.thumbnails = thumbnails;
	}
	
	 
	
	
}
