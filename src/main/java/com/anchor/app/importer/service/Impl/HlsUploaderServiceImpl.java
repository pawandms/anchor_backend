package com.anchor.app.importer.service.Impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.exception.HlsProcessingException;
import com.anchor.app.hls.model.Hls_PlayList;
import com.anchor.app.hls.model.Hls_Segment;
import com.anchor.app.importer.model.ImportMediaRequest;
import com.anchor.app.importer.model.YouTubeMedia;
import com.anchor.app.importer.service.HlsUploaderService;
import com.anchor.app.minio.MinioService;
import com.anchor.app.model.hls.Hls_MediaRepresentation;
import com.iheartradio.m3u8.Encoding;
import com.iheartradio.m3u8.Format;
import com.iheartradio.m3u8.ParsingMode;
import com.iheartradio.m3u8.PlaylistParser;
import com.iheartradio.m3u8.data.MasterPlaylist;
import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.PlaylistData;
import com.iheartradio.m3u8.data.TrackData;

@Service
public class HlsUploaderServiceImpl implements HlsUploaderService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MinioService minioService;
	
	
	@Override
	public void uploadYouTubeGeneratedHls(YouTubeMedia ytm) throws HlsProcessingException {
		
		try {
			// Create MediaPlayList along with Path of Segment Data so it can be uploaded to DB
			
			Hls_MediaRepresentation hlsmp =  prepareMediaRepresentaion_in_hls(ytm);
			
			// Upload Generate PlayList and Segment Data to DB
			
			minioService.saveHlsStreamData(hlsmp);
		
			// Save Bucket name where the media is stored
			ytm.setBucketName(hlsmp.getBucketName());
			ytm.setContentID(hlsmp.getMediaId());
			ytm.setHlsStreamPersisted(true);
		}
		catch(Exception e)
		{
			throw new HlsProcessingException(e.getMessage(), e);	
		}
			
	}
	
	/**
	 * In case of Error remove Partiallay Inserted Segment From DB
	 * @param mediaID
	 */
	private void rolebackHlsSegmentInsert(String mediaID)
	{
		
	}
	
	private Hls_MediaRepresentation prepareMediaRepresentaion_in_hls(YouTubeMedia ytm) throws HlsProcessingException
	{
		Hls_MediaRepresentation hmrp = null;
	
		try {
			Path path = Paths.get(ytm.getHlsMediaPath(),ytm.getMasterPlayListName());
		
			InputStream fstream = new FileInputStream(path.toFile());
			
			String fullName = FilenameUtils.getName(ytm.getMasterPlayListName());
			String name = FilenameUtils.getBaseName(ytm.getMasterPlayListName());
			//String extn = FilenameUtils.getExtension(ytm.getMasterPlayListName());
		
			
			 PlaylistParser parser = new PlaylistParser(fstream, Format.EXT_M3U, Encoding.UTF_8, ParsingMode.LENIENT);
			
			 Playlist playlist = parser.parse();
			 
			 if( null == ytm.getMediaId())
			 {
				 throw new HlsProcessingException("Media ID can not be null for HLS PlayList");
					
			 }
			 
			 hmrp = new Hls_MediaRepresentation(ytm.getMediaId());
			 hmrp.setMediaType(ytm.getYtmInfo().getMediaType());
			 hmrp.setMediaName(ytm.getYtmInfo().getTitle());
			 	
			 // Step 1 : Create MasterPlayList Object
			 
			  createPlayList(hmrp, path);
			 
			 
			
			 if(!playlist.hasMasterPlaylist())
			 {
				 throw new HlsProcessingException("No HLS Master PlayList found on Location:"+path.toString());
			 }
			 
			 if(playlist.hasMasterPlaylist())
			 { 
				 MasterPlaylist mstp = playlist.getMasterPlaylist();
				 
				 if((null == mstp.getPlaylists()) || (mstp.getPlaylists().isEmpty()))
				{
					 throw new HlsProcessingException("No HLS PlayList found in MasterPlayList on Location:"+path.toString());
				}
				 
				 // Create MediaConversion Request to Convert into System Formated Object and upload to DB
				 createMediaPlayList(ytm.getHlsMediaPath(),hmrp, mstp.getPlaylists());
						 
				 
			 }
			
			 
			 
		}
		catch(Exception e)
		{
			throw new HlsProcessingException(e.getMessage(),e);
		}
	
		
		return hmrp;
	}
	
	private void createMediaPlayList(String basePath, Hls_MediaRepresentation hmrp, List<PlaylistData> mPlaylists )
	{
		
		mPlaylists.stream().forEach(plist -> {
				try {
				
					Path plPath = Paths.get(basePath,plist.getUri());
					createPlayList(hmrp, plPath);
					
					// Create Segment List for Each of MediPlayList
					crateSegmentList(basePath,hmrp,plist);
					
				//	printMediaPlayList(basePath, plist.getUri());
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+plist.getUri(), e);
				}
			});


	}
	
	
	/**
	 * Create Hls_Segment List from MediaPlayList Object
	 * @param basePath
	 * @param hmrp
	 * @param playlist
	 */
	private void crateSegmentList(String basePath, Hls_MediaRepresentation hmrp, PlaylistData pld)
	{
		
		try {
			Path mppath = Paths.get(basePath,pld.getUri());
			 InputStream fstream = new FileInputStream(mppath.toFile());
			 
			 PlaylistParser parser = new PlaylistParser(fstream, Format.EXT_M3U, Encoding.UTF_8, ParsingMode.LENIENT);
			
			 Playlist playlist = parser.parse();
			 
			 if(!playlist.hasMediaPlaylist())
			 {
				 throw new HlsProcessingException("No HLS MediaPlayList found on Location:"+mppath.toString());
				
			 }
			 
			 MediaPlaylist mdList = playlist.getMediaPlaylist();
			 
			 if(!mdList.hasTracks())
				{
				 throw new HlsProcessingException("Segment Information not found in HLS MediaPlayList found on Location:"+mppath.toString());
					
				}
			
			 createSegmentList(basePath, hmrp, mdList.getTracks() );
			
			
		}
		catch(Exception e)
		{
			System.out.println("Exception in Parsing MediaPlayList Error:"+e.getMessage());
		}

		
	}
	
	private void createSegmentList(String basePath, Hls_MediaRepresentation hmrp, List<TrackData> mTracks )
	{
		
		mTracks.stream().forEach(track -> {
				try {
				
					 creatSegment(basePath, hmrp,track);
					 
					
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+track.getUri(), e);
				}
			});


	}
	
	private void creatSegment(String basePath, Hls_MediaRepresentation hmrp, TrackData td) throws HlsProcessingException
	{
		
		Path segPath = Paths.get(basePath,td.getUri());
		
		 if(!Files.exists(segPath, LinkOption.NOFOLLOW_LINKS))
			{
			 	 throw new HlsProcessingException("Segment not found at location:"+segPath.toString());
					
			}
		

		 String segmentName = FilenameUtils.getName(td.getUri());
		 String segmentLocation = FilenameUtils.getPathNoEndSeparator(td.getUri());
	
		 Hls_Segment seg = new Hls_Segment(hmrp.getMediaId(),segmentLocation, segmentName);
		 seg.setSegPath(segPath);
		hmrp.getHlsSegmentList().add(seg);
		
		
	}
	
	
	
	private void createPlayList(Hls_MediaRepresentation hmrp, Path playListPath) throws IOException 
	{
		Hls_PlayList hp = null;
		
		 String playListTxt = FileUtils.readFileToString(playListPath.toFile(), "UTF-8");
		 String fullName = FilenameUtils.getName(playListPath.toString());
		
		hp = new Hls_PlayList(hmrp.getMediaId(), fullName);
		hp.setMediaName(hmrp.getMediaName());
		hp.setPlayListTxt(playListTxt);
		hp.setPlayListPath(playListPath);
		
		hmrp.getHlsPlayList().add(hp);
		
		
		
	}

	@Override
	public void uploadImportMediaGeneratedHls(ImportMediaRequest imr) throws HlsProcessingException {
		
		try {
			// Create MediaPlayList along with Path of Segment Data so it can be uploaded to DB
			
			Hls_MediaRepresentation hlsmp =  prepareMediaRepresentaion_in_hls(imr);
			
			// Upload Generate PlayList and Segment Data to DB
			
			minioService.saveHlsStreamData(hlsmp);
		
			// Save Bucket name where the media is stored
			imr.setBucketName(hlsmp.getBucketName());
			imr.setContentID(hlsmp.getMediaId());
			imr.setHlsStreamPersisted(true);
		}
		catch(Exception e)
		{
			throw new HlsProcessingException(e.getMessage(), e);	
		}
	
		
	}
	
	
	private Hls_MediaRepresentation prepareMediaRepresentaion_in_hls(ImportMediaRequest imr) throws HlsProcessingException
	{
		Hls_MediaRepresentation hmrp = null;
	
		try {
			Path path = Paths.get(imr.getHlsMediaPath(),imr.getMasterPlayListName());
		
			InputStream fstream = new FileInputStream(path.toFile());
			
			String fullName = FilenameUtils.getName(imr.getMasterPlayListName());
			String name = FilenameUtils.getBaseName(imr.getMasterPlayListName());
			//String extn = FilenameUtils.getExtension(ytm.getMasterPlayListName());
		
			
			 PlaylistParser parser = new PlaylistParser(fstream, Format.EXT_M3U, Encoding.UTF_8, ParsingMode.LENIENT);
			
			 Playlist playlist = parser.parse();
			 
			 if( null == imr.getMediaId())
			 {
				 throw new HlsProcessingException("Media ID can not be null for HLS PlayList");
					
			 }
			 
			 hmrp = new Hls_MediaRepresentation(imr.getMediaId());
			 hmrp.setMediaType(imr.getMediaType());
			 hmrp.setMediaName(imr.getName());
			 	
			 // Step 1 : Create MasterPlayList Object
			 
			  createPlayList(hmrp, path);
			 
			 
			
			 if(!playlist.hasMasterPlaylist())
			 {
				 throw new HlsProcessingException("No HLS Master PlayList found on Location:"+path.toString());
			 }
			 
			 if(playlist.hasMasterPlaylist())
			 { 
				 MasterPlaylist mstp = playlist.getMasterPlaylist();
				 
				 if((null == mstp.getPlaylists()) || (mstp.getPlaylists().isEmpty()))
				{
					 throw new HlsProcessingException("No HLS PlayList found in MasterPlayList on Location:"+path.toString());
				}
				 
				 // Create MediaConversion Request to Convert into System Formated Object and upload to DB
				 createMediaPlayList(imr.getHlsMediaPath(),hmrp, mstp.getPlaylists());
						 
				 
			 }
			
			 
			 
		}
		catch(Exception e)
		{
			throw new HlsProcessingException(e.getMessage(),e);
		}
	
		
		return hmrp;
	}

	

}
