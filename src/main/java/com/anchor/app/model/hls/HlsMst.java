package com.anchor.app.model.hls;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.enums.ExtensionType;

@Document(collection= "hls_mstpl")
public class HlsMst {

	@Id
	private String id;
	
	private String mediaId;
	private String name;
	private ExtensionType extension;
	private String fullName;
	private int version;
	
	/**
	 * List of #EXT-X-MEDIA in HLS Master PlayList
	 */
	private List<HlsMediaData> mediaList;
	
}
