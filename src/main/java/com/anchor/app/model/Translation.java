package com.anchor.app.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.EntityType;

/**
 * JSON Property in this class used to Import from TMDB API
 * @author pawan
 *
 */

@Document(collection= "translation")
public class Translation {
	
	@Id
	private String id;
	
	private EntityType type;
	
	/**
	 * Entity ID will be Same as ID if we have some For Movie & TV_SHOWS  
	 */
	private String entityId;
	
	private String tmdbId;
	
	private List<TranslationData> dataList;
	
	public Translation()
	{
		this.dataList = new ArrayList<TranslationData>();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	
	
	public String getTmdbId() {
		return tmdbId;
	}

	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
	}

	
	
	public List<TranslationData> getDataList() {
		return dataList;
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
		Translation other = (Translation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	

}
