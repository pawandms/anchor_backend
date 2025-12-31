package com.anchor.app.importer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.MediaType;
import com.anchor.app.media.model.MediaImage;

@Document(collection= "youTubeMedia")
public class YouTubeMedia {

	@Id
	private String id;
	
	// ID of YouTubeImportRequest
	private String requestId;
	
	// MediaId mapped to our System
	private String mediaId;
	private String youtubeUrl;
	
	private String youtubeId;
	
	// MediaType received from Rest Request else it Default will be MediaType.Music_Audio
	private MediaType mediaType;

	private String importedPath;
	private String importFileName;
	private String hlsMediaPath;
	private String masterPlayListName;
	
	private YouTubeMediaInfo ytmInfo;
	
	// Order no if its belongs to PlayList or Channel
	private int order;
	private String iso1LanguageCode;

	
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
	
	private List<MediaImage> imageList; 
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;

	private String bucketName;
	private String contentID;
	
	public YouTubeMedia() {
		super();
		// Default Language Code of Media
		this.iso1LanguageCode = "en";
		imageList = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getYoutubeUrl() {
		return youtubeUrl;
	}

	public void setYoutubeUrl(String youtubeUrl) {
		this.youtubeUrl = youtubeUrl;
	}
	
	
	public String getYoutubeId() {
		return youtubeId;
	}

	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
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

	public List<MediaImage> getImageList() {
		return imageList;
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

	public YouTubeMediaInfo getYtmInfo() {
		return ytmInfo;
	}

	public void setYtmInfo(YouTubeMediaInfo ytmInfo) {
		this.ytmInfo = ytmInfo;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getIso1LanguageCode() {
		return iso1LanguageCode;
	}

	public void setIso1LanguageCode(String iso1LanguageCode) {
		this.iso1LanguageCode = iso1LanguageCode;
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
		YouTubeMedia other = (YouTubeMedia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
