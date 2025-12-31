package com.anchor.app.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.DepartmentType;
import com.anchor.app.enums.PeopleType;

/**
 * Crew can be part of movie / TV Show or Songs
 * as they work to Product this Entity i.e. Movie / TV Show or Song
 * @author pawan
 *
 */
@Document(collection= "crew")
public class Crew extends Cast {

	private DepartmentType department;
	private String job;
	
	public Crew() {
		super();
		super.setType(PeopleType.Crew);
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
	
	
}
