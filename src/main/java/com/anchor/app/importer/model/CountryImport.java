package com.anchor.app.importer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection= "import_country")
public class CountryImport {

	@Id
	private String id;
	private String name;
	
	@Field("alpha-2")
	private String alpha2;
	
	@Field("alpha-3")
	private String alpha3;
	
	@Field("country-code")
	private String countryCode;
	
	@Field("iso_3166-2")
	private String iso3166Code;
	
	private String region;

	@Field("sub-region")
	private String subRegion;
	
	@Field("intermediate-region")
	private String intermediateRegion;
	
	@Field("region-code")
	private String regionCode;
	
	@Field("sub-region-code")
	private String subRegionCode;
	
	@Field("intermediate-region-code")
	private String intermediateRegionCode;

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

	public String getAlpha2() {
		return alpha2;
	}

	public void setAlpha2(String alpha2) {
		this.alpha2 = alpha2;
	}

	public String getAlpha3() {
		return alpha3;
	}

	public void setAlpha3(String alpha3) {
		this.alpha3 = alpha3;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getIso3166Code() {
		return iso3166Code;
	}

	public void setIso3166Code(String iso3166Code) {
		this.iso3166Code = iso3166Code;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSubRegion() {
		return subRegion;
	}

	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}

	public String getIntermediateRegion() {
		return intermediateRegion;
	}

	public void setIntermediateRegion(String intermediateRegion) {
		this.intermediateRegion = intermediateRegion;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getSubRegionCode() {
		return subRegionCode;
	}

	public void setSubRegionCode(String subRegionCode) {
		this.subRegionCode = subRegionCode;
	}

	public String getIntermediateRegionCode() {
		return intermediateRegionCode;
	}

	public void setIntermediateRegionCode(String intermediateRegionCode) {
		this.intermediateRegionCode = intermediateRegionCode;
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
		CountryImport other = (CountryImport) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
