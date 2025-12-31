package com.anchor.app.hls.service;

import java.io.InputStream;

import com.anchor.app.exception.HlsServiceException;
import com.anchor.app.model.hls.Hls_MediaRepresentation;

public interface HlsService {
	
	/*
	 * 
	 * Store HLS Stream Information to Database
	 */
	public void saveHlsStreamData(Hls_MediaRepresentation hlm) throws HlsServiceException;

	/**
	 * Delete HLS Segment for mediaID
	 * @param mediaId
	 * @throws HlsServiceException
	 */
	public void deleteHlsSegmentData(String mediaId)throws HlsServiceException;
	
	/**
	 * Get String representation of HLS PlayList by MediaID and PlayList ID
	 * @param mediaId
	 * @param playListId
	 * @return
	 * @throws HlsServiceException
	 */
	public String getMediaPlayList(String mediaId, String playListId)throws HlsServiceException;
	
	
	/**
	 * Get Input Stream of HLS Segment
	 * @param id
	 * @return
	 */
	public InputStream getSegmentStream(String mediaId, String segmentLocation, String segmentName) throws HlsServiceException;
	
	
	
}
