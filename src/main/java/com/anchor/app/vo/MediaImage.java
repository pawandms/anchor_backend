package com.anchor.app.vo;

import org.springframework.data.annotation.Id;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;


public class MediaImage {

	@Id
	private String id;
	private int width;
	private int height;
	private EntityType entityType;

	// Entity ID of Respective Collection for which this image is Belongs to
	private String entityId;
	private ImageType imageType;
	
	
	public MediaImage(String id, EntityType entityType, String entityId) {
		super();
		this.id = id;
		this.entityType = entityType;
		this.entityId = entityId;
	}
	
	public String getId() {
		return id;
	}
	
	
	
	public void setId(String id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public String getEntityId() {
		return entityId;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public ImageType getImageType() {
		return imageType;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageType == null) ? 0 : imageType.hashCode());
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
		MediaImage other = (MediaImage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageType != other.imageType)
			return false;
		return true;
	}

	
	
}
