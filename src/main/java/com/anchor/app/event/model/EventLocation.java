package com.anchor.app.event.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EventLocation implements Serializable {

	private String type = "Point";
	
	@JsonIgnore
	private double longitude;
	
	@JsonIgnore
	private double latitude;
	
	private List<Double> coordinates = new ArrayList<Double>();
	
	public EventLocation(double longitude, double latitude ) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		
		this.coordinates.add(longitude);
		this.coordinates.add(latitude);
		
	}

	public String getType() {
		return type;
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}
	
	
	
	
	
	
	
}
