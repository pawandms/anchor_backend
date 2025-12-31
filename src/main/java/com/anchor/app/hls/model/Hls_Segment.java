package com.anchor.app.hls.model;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "hls_segment")
public class Hls_Segment {

	// ID is mediaId_{playListName} i.e 100_master.m3u8 or 100_
	
		@Id
		private String id;
		private String mediaId;
		private String mediaName;
		
		// Name of Folder name present in  m3u8 File i.e.  stream_0 or stream_1 
		private String segmentLocation; 
		
		// Name of Segment i.e. data000000.ts or  data000001.ts in stream_0.m3u8
		private String segmentName;

		//Actual Media file in Binary Format
		private Binary data;
		
		@Transient
		private Path segPath;
		
		public Hls_Segment(String mediaId, String segmentLocation, String segmentName) {
			super();
			this.mediaId = mediaId;
			this.segmentLocation = segmentLocation;
			this.segmentName = segmentName;
			this.id = mediaId+"_"+segmentLocation+"_"+segmentName;
		}


		public String getMediaName() {
			return mediaName;
		}


		public void setMediaName(String mediaName) {
			this.mediaName = mediaName;
		}


		public Binary getData() {
			return data;
		}


		public void setData(Binary data) {
			this.data = data;
		}


	

		public Path getSegPath() {
			return segPath;
		}


		public void setSegPath(Path segPath) {
			this.segPath = segPath;
		}


		public String getId() {
			return id;
		}


		public String getMediaId() {
			return mediaId;
		}


		public String getSegmentLocation() {
			return segmentLocation;
		}


		public String getSegmentName() {
			return segmentName;
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
			Hls_Segment other = (Hls_Segment) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
		
		 
		
}
