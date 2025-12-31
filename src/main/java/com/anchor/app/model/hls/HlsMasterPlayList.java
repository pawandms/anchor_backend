package com.anchor.app.model.hls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.ExtensionType;
import com.iheartradio.m3u8.data.IFrameStreamInfo;

@Document(collection= "hls_mst_playlist")
public class HlsMasterPlayList {
	
	@Id
	private String id;
	private String mediaId;
	private String name;
	private ExtensionType extension;
	private int version;
	private boolean isExtended;
	private HlsStartData startData;
	private List<HlsMediaData> mediaList;
	private List<String> unknownTagList;
	private List<HlsPlayListData> playListDataList;
	private List<HlsIFrameStreamInfo> iFrameStreamInfolist;
	  

	public HlsMasterPlayList(String mediaId,ExtensionType extension) 
	{
		super();
		this.mediaId = mediaId;
		this.extension = extension;
		this.id = this.mediaId+"."+extension.getValue();
		this.mediaList = new ArrayList<HlsMediaData>();
		this.unknownTagList = new ArrayList<String>();
		this.playListDataList = new ArrayList<HlsPlayListData>();
		this.iFrameStreamInfolist = new ArrayList<HlsIFrameStreamInfo>();
		
	}

	public String getId() {
		return id;
	}

	

	public String getMediaId() {
		return mediaId;
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

	
		
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isExtended() {
		return isExtended;
	}

	public void setExtended(boolean isExtended) {
		this.isExtended = isExtended;
	}

	public HlsStartData getStartData() {
		return startData;
	}

	public void setStartData(HlsStartData startData) {
		this.startData = startData;
	}

	public List<HlsMediaData> getMediaList() {
		return mediaList;
	}

	public List<String> getUnknownTagList() {
		return unknownTagList;
	}

	public List<HlsPlayListData> getPlayListDataList() {
		return playListDataList;
	}

	public List<HlsIFrameStreamInfo> getiFrameStreamInfolist() {
		return iFrameStreamInfolist;
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
		HlsMasterPlayList other = (HlsMasterPlayList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
