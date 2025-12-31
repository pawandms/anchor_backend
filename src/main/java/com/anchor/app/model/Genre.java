package com.anchor.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;


@Document(collection= "genre")
public class Genre {
	
	@Id
	private String id;
	private String name;
	
	@JsonProperty("tmdb_id")
	@Field("tmdb_id")
	private String tmdbid;
	
	
	
	public Genre() {
		super();
	}
	
	
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
	



	public String getTmdbid() {
		return tmdbid;
	}


	public void setTmdbid(String tmdbid) {
		this.tmdbid = tmdbid;
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
		Genre other = (Genre) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Genre [id=" + id + ", name=" + name + "]";
	}
	
	
	

}
