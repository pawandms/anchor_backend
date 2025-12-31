package com.anchor.app.importer.model.vo;

import com.anchor.app.enums.MetaDataRelationType;

public class MetaDataEntryVo {
	
	private String mediaId;
	
	private MetaDataRelationType relationType;
	private String relationKey;
	private String relationValue;
	private String description;

	
	
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public MetaDataRelationType getRelationType() {
		return relationType;
	}
	public void setRelationType(MetaDataRelationType relationType) {
		this.relationType = relationType;
	}
	public String getRelationKey() {
		return relationKey;
	}
	public void setRelationKey(String relationKey) {
		this.relationKey = relationKey;
	}
	public String getRelationValue() {
		return relationValue;
	}
	public void setRelationValue(String relationValue) {
		this.relationValue = relationValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relationKey == null) ? 0 : relationKey.hashCode());
		result = prime * result + ((relationType == null) ? 0 : relationType.hashCode());
		result = prime * result + ((relationValue == null) ? 0 : relationValue.hashCode());
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
		MetaDataEntryVo other = (MetaDataEntryVo) obj;
		if (relationKey == null) {
			if (other.relationKey != null)
				return false;
		} else if (!relationKey.equals(other.relationKey))
			return false;
		if (relationType != other.relationType)
			return false;
		if (relationValue == null) {
			if (other.relationValue != null)
				return false;
		} else if (!relationValue.equals(other.relationValue))
			return false;
		return true;
	}
	
	

}
