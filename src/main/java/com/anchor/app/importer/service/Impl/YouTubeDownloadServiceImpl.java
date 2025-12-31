package com.anchor.app.importer.service.Impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.enums.ImportStatusType;
import com.anchor.app.exception.YouTubeDownloadException;
import com.anchor.app.importer.model.YouTubeImportRequest;
import com.anchor.app.importer.model.YouTubeMedia;
import com.anchor.app.importer.model.YouTubeMediaInfo;
import com.anchor.app.importer.service.YouTubeDownloadService;
import com.anchor.app.model.Movie;
import com.anchor.app.util.EnvProp;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class YouTubeDownloadServiceImpl implements YouTubeDownloadService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;
	
	

	@Override
	public void downloadYouTubeMedia(YouTubeMedia ytm) throws YouTubeDownloadException {
		
		try {
			
			// Process only if Video is not yet imported earlier
			if(!ytm.isVideoDownloaded())
			{
				Path ytdDownloadPath = env.getYoutube_StaggingLocation();
				
				logger.info("YouTube Media Download Path:"+ytdDownloadPath);
				
				String downloadCmd = ytDownloadCmd(ytm, ytdDownloadPath);

				logger.info("YouTube Media Download Command:"+downloadCmd);

				
				// Step 1 : Download Infor and Media File to Desired Location
				executeYTDDownloadCmd(downloadCmd,ytdDownloadPath);
			
				// Verify YouTube Video Imported Sucessfully
				
				Path mediaPath = Paths.get(ytdDownloadPath.toString(), ytm.getId());
				
				 if(!Files.exists(mediaPath, LinkOption.NOFOLLOW_LINKS))
					{
					 	 throw new YouTubeDownloadException("Unable to load Media Path");
							
					}
				
				 boolean isDirectory = Files.isDirectory(mediaPath, LinkOption.NOFOLLOW_LINKS);
				 
				 if(!isDirectory)
				 {
					 throw new YouTubeDownloadException("Directory with Name:"+ytm.getId()+" , Not Generated at :"+ytdDownloadPath.toString());
				 }
				
				 // Check .mkv file created within Folder
				 String videofileName = ytm.getId()+".mkv";
				 Path videoPath = Paths.get(mediaPath.toString(), videofileName);
				 
				 if(!Files.exists(videoPath, LinkOption.NOFOLLOW_LINKS))
					{
					 	 throw new YouTubeDownloadException("YouTube Media File not found on:"+mediaPath.toString()+" , With Name:"+videofileName);
							
					}
				

				 
				// Read Json Info File to Java
				
				String fileName = ytm.getId()+".info.json";
				String folderName = ytm.getId();
				
				YouTubeMediaInfo ytmInfo =  readMediaInfor_from_JsonFile(ytdDownloadPath, folderName, fileName);
				
				
				if( null != ytm.getMediaType())
				{
					ytmInfo.setMediaType(ytm.getMediaType());
				}
				ytm.setImportedPath(mediaPath.toString());
				ytm.setImportFileName(videofileName);
				 ytm.setVideoDownloaded(true);
				ytm.setYtmInfo(ytmInfo);
				
			}

	
		}
		catch (Exception e)
		{
			logger.error("YouTube Download Error:"+e.getMessage(), e);
			throw new YouTubeDownloadException("Error Download YouTube Media , ERROR:"+e.getMessage());
		}
		
				
	}
	
	/**
	 * Check if .jpg extension thumbnail image is present into Download Directory 
	 * if yes then setup thumbnail fileName
	 * @param ytmInfo
	 */

	/*
	private void populateThumbImageName(Path mediaPath , YouTubeMediaInfo ytmInfo)
	{
		try {
			FilenameFilter filter = new FilenameFilter() {
	             @Override
	             public boolean accept(File f, String name) {
	                 // We want to find only .c files
	                return (( name.toUpperCase().endsWith(".JPG")) || ( name.toUpperCase().endsWith(".JPEG")));
	               
	             }
	         };
	         
	         // Note that this time we are using a File class as an array,
	         // instead of String
	         File[] files = mediaPath.toFile().listFiles(filter);
	         
	         if(files.length >= 1);
	         {
	        	 ytmInfo.setThumbnailName(files[0].getName()); 
	         }	
		}
		catch(Exception e)
		{
			logger.error("Error extracting thumbnail image from YouTubeMedia:"+ytmInfo.getId()+", ERROR:"+e.getMessage(), e);
		}
		 
	}
	*/
	
	private String ytDownloadCmd(YouTubeMedia ytm, Path ytdDownloadPath)
	{
		String result = null;
		StringBuilder sb = new StringBuilder();
		
		Path mediaPath = Paths.get(ytdDownloadPath.toString(), ytm.getId(), ytm.getId());
		
		//sb.append("youtube-dl");
		sb.append("yt-dlp");
		sb.append(" --retries "+env.getYouTube_retryCount());
		sb.append(" --no-overwrites");
		sb.append(" --call-home");
		sb.append(" --write-info-json");
		sb.append(" --write-description");
		sb.append(" --write-thumbnail");
		sb.append(" --all-subs");
		sb.append(" --convert-subs");
		sb.append(" \"");
		sb.append("srt");
		sb.append("\"");
		sb.append(" --write-annotations");
		sb.append(" --add-metadata");
		sb.append(" --embed-subs");
		sb.append(" --download-archive");
		sb.append(" \"");
		sb.append("downloaded.txt");
		sb.append("\"");
		sb.append(" --format ");
		sb.append(" \"");
		sb.append("bestvideo+bestaudio/best");
		sb.append("\"");
		sb.append(" --merge-output-format mkv ");
		sb.append(" --output");
		sb.append(" \"");
		sb.append(mediaPath.toString()+".%(ext)s");
		sb.append("\"");
		sb.append(" \"");
		if( null != ytm.getYoutubeUrl())
		{
			sb.append(ytm.getYoutubeUrl());
			
		}
		else {
			sb.append(ytm.getYoutubeId());
			
		}
		sb.append("\"");
		
		result= sb.toString();
		
		return result;

		
	}
	
	
	private void executeYTDDownloadCmd(String cmd, Path ytdDownlaodPath)
	{
		
		try {
			
			 if(!Files.exists(ytdDownlaodPath, LinkOption.NOFOLLOW_LINKS))
				{
					 throw new IOException("Unable to load Media Path");
						
				}
			
			 ByteArrayOutputStream stdout = new ByteArrayOutputStream();
			    PumpStreamHandler psh = new PumpStreamHandler(stdout);
			    CommandLine cl = CommandLine.parse(cmd);
			    DefaultExecutor exec = new DefaultExecutor();
			    exec.setWorkingDirectory(ytdDownlaodPath.toFile());
			    //exec.setStreamHandler(psh);
			    exec.execute(cl);
			   // System.out.println(stdout.toString());
			
		}
		catch(Exception e)
		{
			logger.error("ERROR Executing YouTube-DL ERROR:"+e.getMessage());
		
		}
		
	}
	
	private YouTubeMediaInfo readMediaInfor_from_JsonFile(Path srcPath, String folderName, String fileName) throws IOException
	{
		YouTubeMediaInfo ytinfo = null;
		
		// Check File is Present at Given Location
		
		
		Path infoPath = Paths.get(srcPath.toString(), folderName, fileName);
		
		 if(!Files.exists(infoPath, LinkOption.NOFOLLOW_LINKS))
			{
				 throw new IOException("Unable to load Media Path");
					
			}

		 ObjectMapper objectMapper = new ObjectMapper()
				 .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		 ytinfo = objectMapper.readValue(infoPath.toFile(), YouTubeMediaInfo.class);
		
		return ytinfo;
		
		
	}

	@Override
	public void downlaodYouTubeRequestData(YouTubeImportRequest ytReq) throws YouTubeDownloadException {

	try {
			
				Path ytdDownloadPath = env.getYoutube_StaggingLocation();
		
				String downloadCmd = ytDownlaodJsonCmd(ytReq, ytdDownloadPath);
				
				logger.info("1. YouTube Download Command :"+downloadCmd);
				
				
				 if(!Files.exists(ytdDownloadPath, LinkOption.NOFOLLOW_LINKS))
					{
						Files.createDirectories(ytdDownloadPath);
							
					}
			
				 logger.info("1. YouTube Download Path :"+ytdDownloadPath);
				
				
				// Step 1 : Download Infor and Media File to Desired Location
				executeYTDDownloadCmd(downloadCmd,ytdDownloadPath);
			
				// Verify YouTube Video Imported Sucessfully
				
				Path mediaPath = Paths.get(ytdDownloadPath.toString(), ytReq.getId());
				
				 if(!Files.exists(mediaPath, LinkOption.NOFOLLOW_LINKS))
					{
					 	 throw new YouTubeDownloadException("Unable to load Media Path");
							
					}
				
				 boolean isDirectory = Files.isDirectory(mediaPath, LinkOption.NOFOLLOW_LINKS);
				 
				 if(!isDirectory)
				 {
					 throw new YouTubeDownloadException("Directory with Name:"+ytReq.getId()+" , Not Generated at :"+ytdDownloadPath.toString());
				 }
				
				// Read Json Info File to Java
				
				String fileName = ytReq.getId()+".info.json";
				String folderName = ytReq.getId();
				
				
				 Path chIdPath = Paths.get(mediaPath.toString(), fileName);
				 
				 if(!Files.exists(chIdPath, LinkOption.NOFOLLOW_LINKS))
					{
					 	 throw new YouTubeDownloadException("YouTube Media info.jao file  not found on:"+chIdPath.toString()+" , With Name:"+fileName);
							
					}
				
				
				YouTubeMediaInfo ytmInfo =  readMediaInfor_from_JsonFile(ytdDownloadPath, folderName, fileName);
				
				ytReq.setChannelUrl(ytmInfo.getChannelUrl());
				ytReq.setChannelId(ytmInfo.getChannelId());
							 
				 // If Download All Channel then txt file with all the Id's Should be Created 
				if(ytReq.isDownlaodChanngel())
				{
					
					downloadYouTubeChanneMediaIds(ytReq);
				}
				else {
					ytReq.getYouTubeIdMap().put(ytmInfo.getId(), ytmInfo.getId());
				}
				
				ytReq.setTotalVideoCount(ytReq.getYouTubeIdMap().size());
	
		}
		catch (Exception e)
		{
			throw new YouTubeDownloadException("Error Download YouTube Media , ERROR:"+e.getMessage());
		}
	}
	
	
	/**
	 * Downlaod All the Video ID's Belongs to Channel and Store into youTubeIdMap 
	 * @param ytReq
	 * @throws YouTubeDownloadException 
	 */
	private void downloadYouTubeChanneMediaIds(YouTubeImportRequest ytReq) throws YouTubeDownloadException 
	{
		if(null == ytReq.getChannelUrl())
		{
			throw new YouTubeDownloadException("Channel URL Can not be Null");
		}
		
		Path ytdDownloadPath = env.getYoutube_StaggingLocation();
		
		String chnlCmd = ytDownlaodChannelVidoeIDsCmd(ytReq, ytdDownloadPath.toString());
		
		
		// Step 1 : Download Infor and Media File to Desired Location
		executeYTDDownloadCmd(chnlCmd,ytdDownloadPath);
	
		// Extract YtmInfo for All the Channel Video Info.Json file
		extractChannelVideoInfo(ytReq); 
	
		}
	
	private void extractChannelVideoInfo(YouTubeImportRequest ytReq) 
	{
		Path idPath = Paths.get(env.getYoutube_StaggingLocation().toString(), ytReq.getId());
		
		try (Stream<Path> filePathStream=Files.walk(idPath)) 
		{
		    filePathStream.forEach(filePath -> {
		        if (Files.isRegularFile(filePath)) {
		          String fileName = FilenameUtils.getBaseName(filePath.toString());
		          String ytID =fileName.replaceFirst(".info","");
		          if((!ytReq.getId().equalsIgnoreCase(ytID)) && (!ytID.equalsIgnoreCase(ytReq.getChannelId())))
		          {
		        	  ytReq.getYouTubeIdMap().put(ytID, ytID);  
		          }
		          
		          
		        }
		    });
		}
		catch(Exception e)
		{
			System.out.println("Excepiotn :"+e.getMessage());
		}
	}
	
	/**
	 * Download Info.json for Requested youtube URL
	 * @param ytReq
	 * @return
	 * @throws YouTubeDownloadException 
	 */
	private String ytDownlaodJsonCmd(YouTubeImportRequest ytReq, Path ytdDownloadPath) throws YouTubeDownloadException
	{
		
		if(null == ytReq.getYoutubeUrl())
		{
			throw new YouTubeDownloadException("YouTube Media URL Can not be Null");
		}
		
		Path mediaPath = Paths.get(ytdDownloadPath.toString(), ytReq.getId(), ytReq.getId());
		
		String result = null;
		StringBuilder sb = new StringBuilder();
		
			//sb.append("youtube-dl");
			sb.append("yt-dlp");
			sb.append(" --retries "+env.getYouTube_retryCount());
			sb.append(" --no-overwrites");
			sb.append(" --call-home");
			sb.append(" --write-info-json");
			sb.append(" --skip-download");
			sb.append(" --output");
			sb.append(" \"");
			sb.append(mediaPath.toString()+".%(ext)s");
			sb.append("\"");
			sb.append(" \"");
			sb.append(ytReq.getYoutubeUrl());
			sb.append("\"");
	
			result= sb.toString();
		
		return result;

		
	}
	
	
	/**
	 * Download All Video ID's of YouTube Channel URL
	 * @param ytReq
	 * @return
	 * @throws YouTubeDownloadException 
	 */
	private String ytDownlaodChannelVidoeIDsCmd(YouTubeImportRequest ytReq, String downloadPath) throws YouTubeDownloadException
	{
		String result = null;
		StringBuilder sb = new StringBuilder();
		
		/*
		 * Commented on 22-02-21 as Extracting ID only giving error if there are some private video
			sb.append("youtube-dl");
			sb.append(" --get-id ");
			sb.append(" \"");
			sb.append(url);
			sb.append("\"");
		
		*/
		
		//sb.append("youtube-dl");
		sb.append("yt-dlp");
		sb.append(" --retries "+env.getYouTube_retryCount());
		sb.append(" --no-overwrites");
		sb.append(" --call-home");
		sb.append(" --write-info-json");
		sb.append(" --skip-download");
		sb.append(" --output");
		sb.append(" \"");
		sb.append(downloadPath+File.separator+ytReq.getId()+File.separator+"%(id)s.%(ext)s");
		sb.append("\"");
		sb.append(" \"");
		sb.append(ytReq.getChannelUrl());
		sb.append("\"");
		
		result= sb.toString();
		
		return result;

		
	}
	
	/*
	private List<String> getVideoIdsFromChannelUrl(YouTubeImportRequest ytReq) throws YouTubeDownloadException
	{
		List<String>idList = null;
		String chnlCmd = ytDownlaodChannelVidoeIDsCmd(ytReq);
		idList = channelIdExtractorCmd(chnlCmd);
		
		return idList;
	}
	
	*/
	
	private List<String> channelIdExtractorCmd(String cmd)
	{
		List<String> idList = null;
		
		try {
			
			  StringBuilder sb = new StringBuilder();
			  ByteArrayOutputStream stdout = new ByteArrayOutputStream();
			    PumpStreamHandler psh = new PumpStreamHandler(stdout);
			    CommandLine cl = CommandLine.parse(cmd);
			    DefaultExecutor exec = new DefaultExecutor();
			    //exec.setWorkingDirectory(ytdDownlaodPath.toFile());
			    exec.setStreamHandler(psh);
			    exec.execute(cl);
			   // System.out.println(stdout.toString());
			
			    sb.append(stdout.toString());
			    
			    idList = Arrays.asList(sb.toString().split("\\r?\\n"));
			
		}
		catch(Exception e)
		{
			logger.error("ERROR Executing YouTube-DL ERROR:"+e.getMessage());
		
		}
		
		return idList;
	}
	
	
	
	
		
}
