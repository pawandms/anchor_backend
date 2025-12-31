package com.anchor.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.CollectionType;

@Document(collection= "MovieCollection")
public class MovieCollection {
	
	@Id
	private String id;
	private String tmdbId;
	private String name;
	private String overview;
	private String backdropImageId;
	private String postarImageId;
	private CollectionType type;
	private double popularity;

	private List<Movie> movieList;
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;

	
	public MovieCollection() {
		super();
		
		movieList = new ArrayList<Movie>();
		
	}

	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getTmdbId() {
		return tmdbId;
	}




	public void setTmdbId(String tmdbId) {
		this.tmdbId = tmdbId;
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




	public String getBackdropImageId() {
		return backdropImageId;
	}




	public void setBackdropImageId(String backdropImageId) {
		this.backdropImageId = backdropImageId;
	}




	public String getPostarImageId() {
		return postarImageId;
	}




	public void setPostarImageId(String postarImageId) {
		this.postarImageId = postarImageId;
	}




	public CollectionType getType() {
		return type;
	}




	public void setType(CollectionType type) {
		this.type = type;
	}




	public double getPopularity() {
		return popularity;
	}




	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}




	public List<Movie> getMovieList() {
		return movieList;
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
		MovieCollection other = (MovieCollection) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	

}
