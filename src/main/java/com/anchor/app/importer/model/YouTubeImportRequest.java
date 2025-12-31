package com.anchor.app.importer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.ImportStatusType;
import com.anchor.app.enums.MediaType;

/**
 * YouTube Download Request to Download
 * Video's From YouTube
 * @author pawan
 *
 */

@Document(collection= "youtubeImportRequest")
public class YouTubeImportRequest {
	
	@Id
	private String id;
	private String youtubeUrl;
	private String channelUrl;
	private String channelId;
	
	private ImportStatusType importStatus;
	
	// MediaType received from Rest Request else it Default will be MediaType.Music_Audio
	private MediaType mediaType;
	
	// Flag to Indicate download single video or all video of respective url channel
	private boolean downlaodChanngel;
	
	private int totalVideoCount;
	private int downloadedVideoCount;
	
	/**
	 * YouTubeMedia ID Map Extracted from YouTube URL
	 * if downloadChannel = ture then it will contains all the ID's of the Video belongs to Channel
	 * Key : MediaID, Value : Media Title
	 */
	
	
	private Map<String,String> youTubeIdMap;
	private Map<String,String> logMap;
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	
	private boolean isProcessed;

	@Transient
	private List<ImportMediaIds> importMediaIdsList;
	
	@Transient
	private List<YouTubeMedia> youttubeMediaList;
	
	public YouTubeImportRequest() {
		super();
		this.logMap = new HashMap<>();
		this.youTubeIdMap = new HashMap<>();
		importMediaIdsList = new ArrayList<>();
		youttubeMediaList = new ArrayList<>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYoutubeUrl() {
		return youtubeUrl;
	}
	public void setYoutubeUrl(String youtubeUrl) {
		this.youtubeUrl = youtubeUrl;
	}
	
	public String getChannelUrl() {
		return channelUrl;
	}
	public void setChannelUrl(String channelUrl) {
		this.channelUrl = channelUrl;
	}
	
	
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public ImportStatusType getImportStatus() {
		return importStatus;
	}
	public void setImportStatus(ImportStatusType importStatus) {
		this.importStatus = importStatus;
	}
	public MediaType getMediaType() {
		return mediaType;
	}
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	
	public boolean isDownlaodChanngel() {
		return downlaodChanngel;
	}
	public void setDownlaodChanngel(boolean downlaodChanngel) {
		this.downlaodChanngel = downlaodChanngel;
	}
	public int getTotalVideoCount() {
		return totalVideoCount;
	}
	public void setTotalVideoCount(int totalVideoCount) {
		this.totalVideoCount = totalVideoCount;
	}
	public int getDownloadedVideoCount() {
		return downloadedVideoCount;
	}
	public void setDownloadedVideoCount(int downloadedVideoCount) {
		this.downloadedVideoCount = downloadedVideoCount;
	}

	public Map<String, String> getLogMap() {
		return logMap;
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
	public boolean isProcessed() {
		return isProcessed;
	}
	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	
	public Map<String, String> getYouTubeIdMap() {
		return youTubeIdMap;
	}
	
	public List<ImportMediaIds> getImportMediaIdsList() {
		return importMediaIdsList;
	}
	public List<YouTubeMedia> getYouttubeMediaList() {
		return youttubeMediaList;
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
		YouTubeImportRequest other = (YouTubeImportRequest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
