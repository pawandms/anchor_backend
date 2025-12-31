package com.anchor.app.model.hls;

import java.util.ArrayList;
import java.util.List;

import com.anchor.app.hls.model.Hls_PlayList;

/**
 * Java Representation of HLS Media Component
 * @author pawan
 *
 */
public class HlsVideo {
	
	private String id;
	private HlsMasterPlayList mstPlayList;
	
	private List<HlsSegment> segment;

	public HlsVideo(String id) {
		super();
		this.id = id;
		this.segment = new ArrayList<HlsSegment>();
	}

	

	public HlsMasterPlayList getMstPlayList() {
		return mstPlayList;
	}



	public void setMstPlayList(HlsMasterPlayList mstPlayList) {
		this.mstPlayList = mstPlayList;
	}



	public String getId() {
		return id;
	}


	public List<HlsSegment> getSegment() {
		return segment;
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
		HlsVideo other = (HlsVideo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
