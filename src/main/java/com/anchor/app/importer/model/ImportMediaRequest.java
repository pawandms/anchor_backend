package com.anchor.app.importer.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.MediaType;

@Document(collection= "importMediaRequest")
public class ImportMediaRequest {

	@Id
	private String id;

	private String mediaId;
	
	// ID of YouTubeImportRequest
	private String requestId;

	private String name;
	private String title;
	private MediaType mediaType;

	
	private String importedPath;
	private String importFileName;
	
	private String hlsMediaPath;
	private String masterPlayListName;
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;

	private String bucketName;
	/**
	 * Unique ID for Content Store
	 */
	private String contentID;

	private boolean isProcessed;
	private boolean isVideoProcessed;
	private boolean isCreditProcessed;
	private boolean isVideoDownloaded;
	private boolean isHlsStreamCreated;
	private boolean isMasterPlayListCreated;
	private boolean isMediaPlayListCreated;
	private boolean isSegmentMediaUploaded;
	private boolean isHlsStreamPersisted;
	// If Images i.e Thumbnail Related to YouTube Media Persisted to DB
	private boolean isImagesPersisted;

	// If Respective Media Object create and Stored into DB
	private boolean isMediaPersisted;

	private int durationInSecond;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	
	public String getImportedPath() {
		return importedPath;
	}

	public void setImportedPath(String importedPath) {
		this.importedPath = importedPath;
	}

	public String getImportFileName() {
		return importFileName;
	}

	public void setImportFileName(String importFileName) {
		this.importFileName = importFileName;
	}

	public String getHlsMediaPath() {
		return hlsMediaPath;
	}

	public void setHlsMediaPath(String hlsMediaPath) {
		this.hlsMediaPath = hlsMediaPath;
	}

	public String getMasterPlayListName() {
		return masterPlayListName;
	}

	public void setMasterPlayListName(String masterPlayListName) {
		this.masterPlayListName = masterPlayListName;
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

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	

	public String getContentID() {
		return contentID;
	}

	public void setContentID(String contentID) {
		this.contentID = contentID;
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public boolean isVideoProcessed() {
		return isVideoProcessed;
	}

	public void setVideoProcessed(boolean isVideoProcessed) {
		this.isVideoProcessed = isVideoProcessed;
	}

	public boolean isCreditProcessed() {
		return isCreditProcessed;
	}

	public void setCreditProcessed(boolean isCreditProcessed) {
		this.isCreditProcessed = isCreditProcessed;
	}

	public boolean isVideoDownloaded() {
		return isVideoDownloaded;
	}

	public void setVideoDownloaded(boolean isVideoDownloaded) {
		this.isVideoDownloaded = isVideoDownloaded;
	}

	public boolean isHlsStreamCreated() {
		return isHlsStreamCreated;
	}

	public void setHlsStreamCreated(boolean isHlsStreamCreated) {
		this.isHlsStreamCreated = isHlsStreamCreated;
	}

	public boolean isMasterPlayListCreated() {
		return isMasterPlayListCreated;
	}

	public void setMasterPlayListCreated(boolean isMasterPlayListCreated) {
		this.isMasterPlayListCreated = isMasterPlayListCreated;
	}

	public boolean isMediaPlayListCreated() {
		return isMediaPlayListCreated;
	}

	public void setMediaPlayListCreated(boolean isMediaPlayListCreated) {
		this.isMediaPlayListCreated = isMediaPlayListCreated;
	}

	public boolean isSegmentMediaUploaded() {
		return isSegmentMediaUploaded;
	}

	public void setSegmentMediaUploaded(boolean isSegmentMediaUploaded) {
		this.isSegmentMediaUploaded = isSegmentMediaUploaded;
	}

	public boolean isHlsStreamPersisted() {
		return isHlsStreamPersisted;
	}

	public void setHlsStreamPersisted(boolean isHlsStreamPersisted) {
		this.isHlsStreamPersisted = isHlsStreamPersisted;
	}

	public boolean isImagesPersisted() {
		return isImagesPersisted;
	}

	public void setImagesPersisted(boolean isImagesPersisted) {
		this.isImagesPersisted = isImagesPersisted;
	}

	public boolean isMediaPersisted() {
		return isMediaPersisted;
	}

	public void setMediaPersisted(boolean isMediaPersisted) {
		this.isMediaPersisted = isMediaPersisted;
	}

	
	
	public int getDurationInSecond() {
		return durationInSecond;
	}

	public void setDurationInSecond(int durationInSecond) {
		this.durationInSecond = durationInSecond;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
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
		ImportMediaRequest other = (ImportMediaRequest) obj;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		return true;
	}

	
	
	
}
