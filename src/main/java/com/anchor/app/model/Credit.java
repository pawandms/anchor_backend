package com.anchor.app.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.CollectionType;
import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.PeopleType;

// Movie, TV Show or Songs to Cast and Crew Relation maintained here 
@Document(collection= "credit")
public class Credit {
	
	@Id
	private String id;
	private CollectionType collection;
	private EntityType entity_type;
	//ID of Movie, TV Show or Music Video from our System
	private String entity_id;
	// Name of Entity i.e. Name of Movie, TVShow or Music Video
	private String entity_name;

	private List<Cast> cast_list;
	private List<Crew> crew_list;
	private List<Company> production_companies_list;
	private List<Country> production_country_list;
	
	public Credit() {
		super();
		this.cast_list = new ArrayList<Cast>();
		this.crew_list =new ArrayList<Crew>();
		this.production_companies_list = new ArrayList<Company>();
		this.production_country_list = new ArrayList<Country>();
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CollectionType getCollection() {
		return collection;
	}

	public void setCollection(CollectionType collection) {
		this.collection = collection;
	}

	public EntityType getEntity_type() {
		return entity_type;
	}

	public void setEntity_type(EntityType entity_type) {
		this.entity_type = entity_type;
	}

	public String getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}

	public String getEntity_name() {
		return entity_name;
	}

	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}

	public List<Crew> getCrew_list() {
		return crew_list;
	}

	public void setCrew_list(List<Crew> crew_list) {
		this.crew_list = crew_list;
	}

	public List<Cast> getCast_list() {
		return cast_list;
	}

	public List<Company> getProduction_companies_list() {
		return production_companies_list;
	}

	public List<Country> getProduction_country_list() {
		return production_country_list;
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
		Credit other = (Credit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
