package com.anchor.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaLanguage {
	
	private String id;
	private String name;

	// Language Name in Unicode Format
	private String nativeName;

	
	@JsonProperty("english_name")
	private String englishName;
	
	@JsonProperty("iso_639_1")
	private String isoCode;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	
	public String getNativeName() {
		return nativeName;
	}

	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
