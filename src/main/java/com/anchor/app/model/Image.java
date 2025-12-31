package com.anchor.app.model;

import java.io.Serializable;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;

@Document(collection= "image")
public class Image implements Serializable{

	@Id
	private String id;
	private EntityType entityType;

	//Reeference Entity for which this Image is Belongs to
	private String entityId;
	private int width;
	private int height;
	private ImageType imageType;
	
	//Actual Media file in Binary Format
	//private Binary data;
	
	public Image(EntityType entityType, ImageType imageType, String entityId) 
	{
		super();
		this.entityType = entityType;
		this.imageType = imageType;
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

	public EntityType getEntityType() {
		return entityType;
	}

	
	
	public String getEntityId() {
		return entityId;
	}

	public ImageType getImageType() {
		return imageType;
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
		Image other = (Image) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

	
	
}
