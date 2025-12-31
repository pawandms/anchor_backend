package com.anchor.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.DepartmentType;
import com.anchor.app.enums.GenderType;
import com.anchor.app.enums.PeopleType;

/**
 * Cast can be part of movie / TV Show or Songs
 * @author pawan
 *
 */
@Document(collection= "cast")
public class Cast {

	@Id
	private String id;
	
	private String tmdbId;

	// Name of Person who is playing this Cast role
	private String name;
		
	private GenderType genderType;
	private String personId;
	//Default type : Cast
	private PeopleType type;
	private int order;
	private String profileImageId;
	private DepartmentType knownForDepartment;
	private String character;
	
	private String tmdbcreditid;
	private String creditId;
	
	
	public Cast() {
		super();
		this.type = PeopleType.Cast;
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


	public GenderType getGenderType() {
		return genderType;
	}


	public void setGenderType(GenderType genderType) {
		this.genderType = genderType;
	}


	public String getPersonId() {
		return personId;
	}


	public void setPersonId(String personId) {
		this.personId = personId;
	}


	public PeopleType getType() {
		return type;
	}


	public void setType(PeopleType type) {
		this.type = type;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public String getProfileImageId() {
		return profileImageId;
	}


	public void setProfileImageId(String profileImageId) {
		this.profileImageId = profileImageId;
	}


	public DepartmentType getKnownForDepartment() {
		return knownForDepartment;
	}


	public void setKnownForDepartment(DepartmentType knownForDepartment) {
		this.knownForDepartment = knownForDepartment;
	}


	public String getCharacter() {
		return character;
	}


	public void setCharacter(String character) {
		this.character = character;
	}


	public String getTmdbcreditid() {
		return tmdbcreditid;
	}


	public void setTmdbcreditid(String tmdbcreditid) {
		this.tmdbcreditid = tmdbcreditid;
	}


	public String getCreditId() {
		return creditId;
	}


	public void setCreditId(String creditId) {
		this.creditId = creditId;
	}
	
	
		
}
