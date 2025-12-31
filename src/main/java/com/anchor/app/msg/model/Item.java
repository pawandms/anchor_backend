package com.anchor.app.msg.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "Item")
public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1428456313049279341L;
	@Id
	private String id;
	private String name;
    private String category;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

    
	
}
