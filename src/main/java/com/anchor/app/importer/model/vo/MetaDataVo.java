package com.anchor.app.importer.model.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.anchor.app.enums.MediaType;
import com.anchor.app.vo.BaseVo;

public class MetaDataVo extends BaseVo {
	
	private String mediaId;
	private MediaType mediaType;
	
	List<MetaDataEntryVo> entryList;
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	
	public MetaDataVo() {
		super();
		this.entryList = new ArrayList<>();
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

	public List<MetaDataEntryVo> getEntryList() {
		return entryList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((mediaId == null) ? 0 : mediaId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaDataVo other = (MetaDataVo) obj;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		return true;
	}
	
	
	

}
