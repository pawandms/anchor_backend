package com.anchor.app.media.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.CollectionType;
import com.anchor.app.enums.EntityType;

/**
 * Media Belongs to Collection
 * Media can Belongs to Multiple Collection i.e. Music Belongs to Single PlayList/Collection, Music Company PlayList Collection 
 * @author pawan
 *
 */

@Document(collection= "mediaCollection")
public class MediaCollection {
	
	@Id
	private String id;

	private CollectionType type;
	private String name;
	private String overview;

	private EntityType mediaType;
	
	// Media Entity List Belongs to this Collection
	private List<String> mediaIdList;
	
	// Available Images for Media
	private List<MediaImage> imageList;
	
	private double popularity;

	
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	
	public MediaCollection() {
		super();
		this.mediaIdList = new ArrayList<String>();
		this.imageList = new ArrayList<MediaImage>();
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CollectionType getType() {
		return type;
	}

	public void setType(CollectionType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public EntityType getMediaType() {
		return mediaType;
	}

	public void setMediaType(EntityType mediaType) {
		this.mediaType = mediaType;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
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

	public List<String> getMediaIdList() {
		return mediaIdList;
	}

	public List<MediaImage> getImageList() {
		return imageList;
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
		MediaCollection other = (MediaCollection) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	


}
