package com.anchor.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nonnull;

@Document(collection= "company")
public class Company {

	@Id
	private String id;

	@JsonProperty("tmdb_id")
	@Field("tmdb_id")
	private String tmdbid;
	
	@Nonnull
	private String name;
	private List<String> alternateNames;
	
	@JsonProperty("logo_path")
	private String tmdb_logo_path;
	private String image_id;
	private String description;
	private String headquarters;
	private String homepage;
	private String origin_country;
	//Flag for IsChild Company
	private boolean isChild;
	
	@JsonProperty("parent_company")
	private ParentCompany parent_company;
	
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	
	
	
	public Company() {
		super();
		this.alternateNames = new ArrayList<String>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTmdbid() {
		return tmdbid;
	}
	public void setTmdbid(String tmdbid) {
		this.tmdbid = tmdbid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTmdb_logo_path() {
		return tmdb_logo_path;
	}
	public void setTmdb_logo_path(String tmdb_logo_path) {
		this.tmdb_logo_path = tmdb_logo_path;
	}
	public String getImage_id() {
		return image_id;
	}
	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHeadquarters() {
		return headquarters;
	}
	public void setHeadquarters(String headquarters) {
		this.headquarters = headquarters;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getOrigin_country() {
		return origin_country;
	}
	public void setOrigin_country(String origin_country) {
		this.origin_country = origin_country;
	}
	
	public ParentCompany getParent_company() {
		return parent_company;
	}
	public void setParent_company(ParentCompany parent_company) {
		this.parent_company = parent_company;
	}
	
	public boolean isChild() {
		return isChild;
	}
	public void setChild(boolean isChild) {
		this.isChild = isChild;
	}
	
	public List<String> getAlternateNames() {
		return alternateNames;
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
		Company other = (Company) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Company [id=" + id + ", tmdb_id=" + tmdbid + ", name=" + name + ", tmdb_logo_path=" + tmdb_logo_path
				+ ", image_id=" + image_id + ", description=" + description + ", headquarters=" + headquarters
				+ ", homepage=" + homepage + ", origin_country=" + origin_country + ", isChild=" + isChild
				+ ", parent_company=" + parent_company + "]";
	}

	
	
	

}
