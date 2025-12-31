package com.anchor.app.importer.service;

import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.enums.MediaType;
import com.anchor.app.importer.exception.ImportMediaServiceException;
import com.anchor.app.importer.model.ImportMediaRequest;
import com.anchor.app.media.model.Media;

public interface ImportMediaService {
	
	/**
	 * Upload Physical Media File to Object Store
	 * @param name
	 * @param title
	 * @param mediaType
	 * @param createdBy
	 * @param mediaFile
	 * @return
	 * @throws ImportMediaServiceException
	 */
	public Media importMedia(String name, String title,MediaType mediaType, String createdBy, MultipartFile mediaFile, String mediaFileServerPath, boolean isServerFile ) throws ImportMediaServiceException;

	/**
	 * Process Imported Media by Creating HLS Stream and Store in Object Store 
	 * @return
	 * @throws ImportMediaServiceException
	 */
	public int processImportedMedia()throws ImportMediaServiceException;
}
