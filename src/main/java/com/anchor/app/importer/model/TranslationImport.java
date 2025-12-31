package com.anchor.app.importer.model;

import com.anchor.app.enums.EntityType;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON Property in this class used to Import from TMDB API
 * @author pawan
 *
 */
public class TranslationImport {
	
	@JsonProperty("iso_3166_1")
	private String countryIsoCode1;

	@JsonProperty("iso_639_1")
	private String languageIsoCode1;
	
	@JsonProperty("name")
	private String languageNativeName;

	@JsonProperty("english_name")
	private String languageEnglishName;


	@JsonProperty("data")
	private MovieTranslationData data;


	public String getCountryIsoCode1() {
		return countryIsoCode1;
	}


	public void setCountryIsoCode1(String countryIsoCode1) {
		this.countryIsoCode1 = countryIsoCode1;
	}


	public String getLanguageIsoCode1() {
		return languageIsoCode1;
	}


	public void setLanguageIsoCode1(String languageIsoCode1) {
		this.languageIsoCode1 = languageIsoCode1;
	}


	public String getLanguageNativeName() {
		return languageNativeName;
	}


	public void setLanguageNativeName(String languageNativeName) {
		this.languageNativeName = languageNativeName;
	}


	public String getLanguageEnglishName() {
		return languageEnglishName;
	}


	public void setLanguageEnglishName(String languageEnglishName) {
		this.languageEnglishName = languageEnglishName;
	}


	public MovieTranslationData getData() {
		return data;
	}


	public void setData(MovieTranslationData data) {
		this.data = data;
	}
	

	

}
