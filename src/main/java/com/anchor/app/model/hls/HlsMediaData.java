package com.anchor.app.model.hls;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.anchor.app.enums.HlsMediaType;

/**
 * Media related to HLS Master PlayList
 * @author pawan
 *
 */
public class HlsMediaData {
	
	@Id
	private String id;
	
	// Media ID for which this Media is Belongs to
	private String mediaId;

	private HlsMediaType mediaType;
	private String uri;
    private String groupId;
    private String language;
    private String associatedLanguage;
    private String name;
    private boolean isDefault;
    private boolean autoSelect;
    private boolean forced;
    private String inStreamId;
    private List<String> characteristicsList;
    private int channels;
	
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public HlsMediaType getMediaType() {
		return mediaType;
	}
	public void setMediaType(HlsMediaType mediaType) {
		this.mediaType = mediaType;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getAssociatedLanguage() {
		return associatedLanguage;
	}
	public void setAssociatedLanguage(String associatedLanguage) {
		this.associatedLanguage = associatedLanguage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public boolean isAutoSelect() {
		return autoSelect;
	}
	public void setAutoSelect(boolean autoSelect) {
		this.autoSelect = autoSelect;
	}
	public boolean isForced() {
		return forced;
	}
	public void setForced(boolean forced) {
		this.forced = forced;
	}
	public String getInStreamId() {
		return inStreamId;
	}
	public void setInStreamId(String inStreamId) {
		this.inStreamId = inStreamId;
	}
	public List<String> getCharacteristicsList() {
		return characteristicsList;
	}
	public void setCharacteristicsList(List<String> characteristicsList) {
		this.characteristicsList = characteristicsList;
	}
	public int getChannels() {
		return channels;
	}
	public void setChannels(int channels) {
		this.channels = channels;
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
		HlsMediaData other = (HlsMediaData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
