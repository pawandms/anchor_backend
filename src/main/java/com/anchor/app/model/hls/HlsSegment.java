package com.anchor.app.model.hls;

import java.io.File;
import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.ExtensionType;
import com.anchor.app.enums.MediaStreamType;
import com.anchor.app.enums.RepresentationType;

@Document(collection= "hlssegment")
public class HlsSegment {
	
	@Id
	private String id;
	private String movieId;
	private String playListId;
	private int sequenceNo;
	private double duration;
	private String name;
	private ExtensionType extension;
	private String fullName;
	
	//Actual Media file in Binary Format
	private Binary data;
	
	// Media Stream type HLS / DASH
	private MediaStreamType streatype;
	
	// Media Quality Type i.e. 1080P, 720P
	private RepresentationType reptype;
	
	
	private String createdBy;
	private Date createdOn;
	private String modifiedBy;
	private Date modifiedOn;
	
	@Transient
	private File segFile;
	
	
	public HlsSegment(String movieId, String playListId, String fullName) {
		super();
		this.movieId = movieId;
		this.playListId = playListId;
		this.fullName = fullName;
		this.id = this.movieId+"_"+this.fullName;
	}

	public String getId() {
		return id;
	}



	public String getMovieId() {
		return movieId;
	}

	public String getPlayListId() {
		return playListId;
	}


	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}


	public double getDuration() {
		return duration;
	}


	public void setDuration(double duration) {
		this.duration = duration;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ExtensionType getExtension() {
		return extension;
	}


	public void setExtension(ExtensionType extension) {
		this.extension = extension;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	
	public Binary getData() {
		return data;
	}


	public void setData(Binary data) {
		this.data = data;
	}



	public MediaStreamType getStreatype() {
		return streatype;
	}


	public void setStreatype(MediaStreamType streatype) {
		this.streatype = streatype;
	}


	public RepresentationType getReptype() {
		return reptype;
	}


	public void setReptype(RepresentationType reptype) {
		this.reptype = reptype;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public Date getModifiedOn() {
		return modifiedOn;
	}


	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	



	
	public File getSegFile() {
		return segFile;
	}

	public void setSegFile(File segFile) {
		this.segFile = segFile;
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
		HlsSegment other = (HlsSegment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Segment [id=" + id + ", movieId=" + movieId + ", duration=" + duration + ", name=" + name
				+ ", extension=" + extension + ", fullName=" + fullName + ", streatype=" + streatype
				+ ", reptype=" + reptype + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedBy="
				+ modifiedBy + ", modifiedOn=" + modifiedOn + "]";
	}
	
	
	
	

}
