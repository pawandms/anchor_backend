package com.anchor.app.importer.model;

public class TmdbCrew extends TmdbCast {

	private String department;
	private String job;
	
	public TmdbCrew() {
		super();
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
	
	
	
}
