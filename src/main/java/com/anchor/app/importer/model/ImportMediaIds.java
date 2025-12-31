package com.anchor.app.importer.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.ExternalSystemType;
import com.anchor.app.enums.ImportStatusType;
import com.anchor.app.enums.MediaType;

/**
 * To Identify if Video is Already Download
 * hence all YouTube Media ID's will be store into this Entity
 * @author pawan
 *
 */

@Document(collection= "importMediaID")
public class ImportMediaIds {
	
	@Id
	private String id;
	private String requestId;
	private String name;
	private ImportStatusType importStatus;
	private ExternalSystemType sourceSystem;
	private String mediaId;
	private MediaType mediaType;
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ImportStatusType getImportStatus() {
		return importStatus;
	}
	public void setImportStatus(ImportStatusType importStatus) {
		this.importStatus = importStatus;
	}
	public ExternalSystemType getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(ExternalSystemType sourceSystem) {
		this.sourceSystem = sourceSystem;
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
	public MediaType getMediaType() {
		return mediaType;
	}
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
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
		ImportMediaIds other = (ImportMediaIds) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
	
	

}
