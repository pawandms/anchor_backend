package com.anchor.app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.CollectionType;
import com.anchor.app.enums.DepartmentType;
import com.anchor.app.enums.PeopleType;

@Document(collection= "personCredit")
public class PersonCredit {
	
	@Id
	private String id;

	// Person Details
	private PeopleType type;
	private String personId;
	private String personName;
	
	// Media Relatef to Person 
	private CollectionType mediaType;	
	private String mediaId;
	private String mediaCreditId;
	private String mediaCharacter;
	private String mediaName;
	private String mediaTitle;
	private String mediaTagLine;
	private String mediaBackdropId;
	private String mediaPostarId;
	private Date release_date;
	private DepartmentType department;
	private String job;

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
	public PeopleType getType() {
		return type;
	}
	public void setType(PeopleType type) {
		this.type = type;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	public CollectionType getMediaType() {
		return mediaType;
	}
	public void setMediaType(CollectionType mediaType) {
		this.mediaType = mediaType;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	public String getMediaCreditId() {
		return mediaCreditId;
	}
	public void setMediaCreditId(String mediaCreditId) {
		this.mediaCreditId = mediaCreditId;
	}
	
	public String getMediaCharacter() {
		return mediaCharacter;
	}
	public void setMediaCharacter(String mediaCharacter) {
		this.mediaCharacter = mediaCharacter;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getMediaTitle() {
		return mediaTitle;
	}
	public void setMediaTitle(String mediaTitle) {
		this.mediaTitle = mediaTitle;
	}
	public String getMediaTagLine() {
		return mediaTagLine;
	}
	public void setMediaTagLine(String mediaTagLine) {
		this.mediaTagLine = mediaTagLine;
	}
	public String getMediaBackdropId() {
		return mediaBackdropId;
	}
	public void setMediaBackdropId(String mediaBackdropId) {
		this.mediaBackdropId = mediaBackdropId;
	}
	public String getMediaPostarId() {
		return mediaPostarId;
	}
	public void setMediaPostarId(String mediaPostarId) {
		this.mediaPostarId = mediaPostarId;
	}
	public Date getRelease_date() {
		return release_date;
	}
	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}
	public DepartmentType getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentType department) {
		this.department = department;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
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
		PersonCredit other = (PersonCredit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	

}
