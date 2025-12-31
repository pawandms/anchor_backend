package com.anchor.app.importer.service.Impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.anchor.app.enums.OsType;
import com.anchor.app.exception.HlsProcessingException;
import com.anchor.app.exception.MediaException;
import com.anchor.app.importer.model.ImportMedia;
import com.anchor.app.importer.model.ImportMediaRequest;
import com.anchor.app.importer.model.VideoQuality;
import com.anchor.app.importer.model.YouTubeMedia;
import com.anchor.app.importer.service.HlsConversionService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;
import com.iheartradio.m3u8.Encoding;
import com.iheartradio.m3u8.Format;
import com.iheartradio.m3u8.ParsingMode;
import com.iheartradio.m3u8.PlaylistParser;
import com.iheartradio.m3u8.data.Playlist;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

@Service
public class HlsConversionServiceImpl implements HlsConversionService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private HelperBean helper;
	
	Map<Integer,VideoQuality> vqMap;
	String ffmpeg_dir_path;
	private String hlsFolderName;
	
	
	@PostConstruct
	public void init() 
	{
		populateVideoQualityMap();
		
		Path ffmpegPath = env.getFfmpegPath();
		ffmpeg_dir_path = ffmpegPath.toString();
		hlsFolderName = env.getHlsFolderName();
	}

	private void populateVideoQualityMap()
	{
		vqMap = new LinkedHashMap<Integer,VideoQuality>();

		
		
		VideoQuality p_240 = createVideoFormat(426, 240);
		p_240.setActive(false);
		p_240.getHlsQualityList().add(p_240.getId());
		
		vqMap.put(p_240.getId(), p_240);
		
		
		
		VideoQuality p_360 = createVideoFormat(640, 360);
		p_360.setActive(false);
		p_360.getHlsQualityList().add(p_240.getId());
		p_360.getHlsQualityList().add(p_360.getId());
		
		vqMap.put(p_360.getId(), p_360);
		
		
		
		VideoQuality p_480 = createVideoFormat(854, 480);
		//p_480.getHlsQualityList().add(p_240.getId());
		p_480.getHlsQualityList().add(p_360.getId());
		p_480.getHlsQualityList().add(p_480.getId());
		
		vqMap.put(p_480.getId(), p_480);
		
		VideoQuality p_540 = createVideoFormat(960, 540);
		//p_540.getHlsQualityList().add(p_240.getId());
		//p_540.getHlsQualityList().add(p_360.getId());
		p_540.getHlsQualityList().add(p_480.getId());
		p_540.getHlsQualityList().add(p_540.getId());
		
		vqMap.put(p_540.getId(), p_540);
		
		VideoQuality p_720 = createVideoFormat(1280, 720);
		//p_720.getHlsQualityList().add(p_240.getId());
		//p_720.getHlsQualityList().add(p_360.getId());
		//p_720.getHlsQualityList().add(p_480.getId());
		p_720.getHlsQualityList().add(p_540.getId());
		p_720.getHlsQualityList().add(p_720.getId());
		
		vqMap.put(p_720.getId(), p_720);
		
		VideoQuality p_1080 = createVideoFormat(1920, 1080);
		//p_1080.getHlsQualityList().add(p_240.getId());
		//p_1080.getHlsQualityList().add(p_360.getId());
		//p_1080.getHlsQualityList().add(p_480.getId());
		p_1080.getHlsQualityList().add(p_540.getId());
		p_1080.getHlsQualityList().add(p_720.getId());
		p_1080.getHlsQualityList().add(p_1080.getId());
		
		vqMap.put(p_1080.getId(), p_1080);
		
		VideoQuality p_4k = createVideoFormat(4096, 2160);
		//p_4k.getHlsQualityList().add(p_240.getId());
		//p_4k.getHlsQualityList().add(p_360.getId());
		//p_4k.getHlsQualityList().add(p_480.getId());
		p_4k.getHlsQualityList().add(p_540.getId());
		p_4k.getHlsQualityList().add(p_720.getId());
		p_4k.getHlsQualityList().add(p_1080.getId());
		p_4k.getHlsQualityList().add(p_4k.getId());
		
		vqMap.put(p_4k.getId(), p_4k);
	}
	
	private VideoQuality createVideoFormat(int width, int height)
	{
		VideoQuality vq = new VideoQuality(width, height);
			int vqId = width+height;
			
			switch(vqId)
			{
			case 666 :
				vq.setQuality("240P");
				vq.setBitrateLow(400);
				vq.setBitrateHigh(600);
				vq.setBitrateAudio(64);
				//vq.setId(vqId);
				break;
			
			case 1000 :
				vq.setQuality("360P");
				vq.setBitrateLow(700);
				vq.setBitrateHigh(900);
				vq.setBitrateAudio(96);
				//vq.setId(vqId);
				break;
				
			case 1334 :
				vq.setQuality("480P");
				vq.setBitrateLow(1250);
				vq.setBitrateHigh(1600);
				vq.setBitrateAudio(128);
				//vq.setId(vqId);
				break;	
				
		
			case 1500 :
				vq.setQuality("540P");
				vq.setBitrateLow(2400);
				vq.setBitrateHigh(2640);
				vq.setBitrateAudio(128);
				//vq.setId(vqId);
				break;		
				
			
			case 2000 :
				vq.setQuality("720P");
				vq.setBitrateLow(2500);
				vq.setBitrateHigh(3200);
				vq.setBitrateAudio(128);
				//vq.setId(vqId);
				break;		
				
			case 3000 :
				vq.setQuality("1080P");
				vq.setBitrateLow(4500);
				vq.setBitrateHigh(5300);
				vq.setBitrateAudio(192);
				//vq.setId(vqId);
				break;
				
			case 6000 :
				vq.setQuality("4K");
				vq.setBitrateLow(14000);
				vq.setBitrateHigh(18200);
				vq.setBitrateAudio(192);
				//vq.setId(vqId);
				break;				
			}
		
		
	return vq;	
	}
	

	
	@Override
	public void createHlsStramForMedia(YouTubeMedia ytm) throws HlsProcessingException
	{
		
		try {
			// Create HLS Stream only if not created earlier
			if(!ytm.isHlsStreamCreated())
			{
				String videoPath = ytm.getImportedPath()+File.separator+ytm.getImportFileName();
			
				logger.info("Create HLS Stream VideoPath :"+videoPath);
				
				
				ImportMedia im = create_hlsStram_from_Video(videoPath, ytm.getMasterPlayListName());
			
				ytm.setHlsStreamCreated(true);
				ytm.setHlsMediaPath(im.getHlsMediaPath());
			
			}
				
		}
		catch(Exception e)
		{
			throw new HlsProcessingException("Exception in Creating HLS Stream for YouTube Media:"+ytm.getId()
					+" , ERROR:"+e.getMessage());
		}
		
	}
	
	
	private ImportMedia create_hlsStram_from_Video(String videoPath, String masterPlayListName) throws MediaException
	{	ImportMedia im = null;
	
		try {
			
			if ( null == videoPath)
			{
				throw new MediaException("Import Media Path Can not be null");
			}
			
			im = prepareImportMedia_from_Path(videoPath);
			im.setMasterPlayListName(masterPlayListName);
			
			logger.info("Create HLS Stream Media Preparation Done for :"+videoPath);
			
			create_hlsTransform_Cmd(im);
			
			logger.info("Create HLS Stream For Video Command :"+im.getHlsCmd());
			
			
			ExecuteCmd(im);
			
			Path hlsPath = Paths.get(im.getPath(), this.hlsFolderName);
			
			// Verify HLS Conversion Happen by Checking HLS Directory is not Empty 
			
			boolean isHlsDirectoryEmpty = isEmpty(hlsPath);
			
			if(isHlsDirectoryEmpty)
			{
				throw new MediaException("HLS Conversion Failed with Command :"+im.getHlsCmd()+" , in Path :"+hlsPath);
			}
		
			
			
		}
		catch(MediaException | HlsProcessingException | IOException e)
		{
			throw new MediaException("Exception creating HLS Stream for :"+videoPath+" , ERROR:"+e.getMessage());
		}
		
		return im;
	}
	
	public boolean isEmpty(Path path) throws IOException {
	    if (Files.isDirectory(path)) {
	        try (Stream<Path> entries = Files.list(path)) {
	            return !entries.findFirst().isPresent();
	        }
	    }
	        
	    return false;
	}
	
	private ImportMedia prepareImportMedia_from_Path(String videoPath) throws MediaException
	{
		ImportMedia im = null;
		
		try {
			if ( null == videoPath)
			{
				throw new MediaException("Import Media Path Can not be null");
			}
			
			Path vPath = Paths.get(videoPath);
			
			if(!Files.exists(vPath, LinkOption.NOFOLLOW_LINKS))
				{
				 	 throw new MediaException("Media File Not present for HLS Conversion at location:"+vPath.toString());
						
				}
			
			
			String fullName = FilenameUtils.getName(videoPath);
			String name = FilenameUtils.getBaseName(videoPath);
			String extn = FilenameUtils.getExtension(videoPath);
			String path = FilenameUtils.getFullPath(videoPath);
			
			im = new ImportMedia();
			im.setExtn(extn);
			im.setFullName(fullName);
			im.setId(fullName);
			im.setName(name);
			im.setPath(path);
			
			VideoQuality vq =  extractImportmediaInfo(videoPath);
			im.setVideoInfo(vq);
			im.setAudioStreamId(vq.getAudioStreamId());
			im.setHeight(vq.getHeight());
			im.setVidoeStreamId(vq.getVideoStreamId());
			im.setWidth(vq.getWidth());
			// Set Video Duration in Second
			im.setVideoDurationInSecond(vq.getVideoDurationInSecond());
			
		}
		catch(MediaException e)
		{
			throw e;
		}
			
		
		
		return im;
		
	}
	
	private void create_hlsTransform_Cmd(ImportMedia media) throws HlsProcessingException
	{
		String result = null;
		StringBuilder sb = new StringBuilder();
		
		sb.append("ffmpeg -i "+media.getPath()+File.separator+media.getFullName());
		//sb.append(" -max_muxing_queue_size 1024");
		sb.append(" -filter_complex ");
		String complexFilterStr = createComplexFilter_SplitStramCmd(media.getVideoInfo());
		
		sb.append(" \"");
		sb.append(complexFilterStr);
		sb.append(" \"");
		
		sb.append(" -preset slow -g 48 -keyint_min 48 -sc_threshold 0 ");
		
		String videoMapStr = createMapVideo_Cmd(media.getVideoInfo());
		sb.append(videoMapStr);
		
		// Audio Map cmd
		sb.append(" -map a:0 -c:a aac -b:a 128k -ac 2 ");
		
		//hls seqgment configuration
		sb.append(" -f hls -hls_time 6 -hls_playlist_type event -hls_flags independent_segments");
		
		// Mastet FileList Name
		sb.append(" -master_pl_name "+media.getMasterPlayListName());
		
		// Segment FileName Format
		sb.append(" -hls_segment_filename stream_%v/data%06d.ts");
		
		// Create Playlist Dir Config
		sb.append(" -use_localtime_mkdir 1");
		
		sb.append(" -var_stream_map");
		
		sb.append(" \"");
		
		sb.append("a:0,agroup:audio128,language:"+media.getVideoInfo().getAudioLanguageCd());
		
		String videooutStr = createMapVideoOutPut_Cmd(media.getVideoInfo());
		
		sb.append(videooutStr);
		
		sb.append(" \"");
		
		sb.append(" stream_%v.m3u8");
		
		result= sb.toString();
		
		media.setHlsCmd(result);
		
		
	}

	private String createComplexFilter_SplitStramCmd(VideoQuality sourceVq) throws HlsProcessingException
	{
		int splitcount = sourceVq.getHlsQualityList().size();
		StringBuilder sb = new StringBuilder();
		sb.append("[v:"+sourceVq.getVideoStreamId()+"]split="+splitcount);
		
		for ( int i = 1; i<= splitcount ; i++)
		{
			if(i < splitcount)
			{
			sb.append("[vtemp00"+i+"]");	
			}
			else {
				// final stream should be as is
				sb.append("[vout00"+i+"]");
			}
		}
		
		sb.append(";");
		
		List<VideoQuality> hlsvqList =getHlsSupportedVideoQualityList(sourceVq);
		if( hlsvqList.isEmpty())
		{
			throw new HlsProcessingException("Supported HLS Video Format List can not be empty for :"+sourceVq.getId());
		}
		int hlsvqId = 1;
		for (VideoQuality hlsvq : hlsvqList)
		{
			if ( hlsvqId < hlsvqList.size())
			{
				sb.append("[vtemp00"+hlsvqId+"]");
				sb.append("scale=w="+hlsvq.getWidth()+":h="+hlsvq.getHeight());
				sb.append("[vout00"+hlsvqId+"]");

				if(hlsvqId < hlsvqList.size()-1  )
				{
					sb.append(";");
						
				}
				
				
				hlsvqId = hlsvqId+1;
				
			}
			
		}
		
		return sb.toString();
	}


	private String createMapVideo_Cmd(VideoQuality sourceVq) throws HlsProcessingException
	{
		StringBuilder sb = new StringBuilder();
		List<VideoQuality> hlsvqList =getHlsSupportedVideoQualityList(sourceVq);
		if( hlsvqList.isEmpty())
		{
			throw new HlsProcessingException("Supported HLS Video Format List can not be empty for :"+sourceVq.getId());
		}
		int hlsvqId = 1;
		
		for (VideoQuality hlsvq : hlsvqList)
		{
			int vdId = hlsvqId -1 ;
			sb.append(" -map ");
			sb.append("\"");
			sb.append("[vout00"+hlsvqId+"]");
			sb.append("\"");
			sb.append(" -c:v:"+vdId+" libx264 -b:v:"+vdId+" ");
			sb.append(hlsvq.getBitrateLow()+"K");
			sb.append(" -maxrate:v:"+vdId+" ");
			sb.append(hlsvq.getBitrateHigh()+"K");
			sb.append(" -bufsize:v:"+vdId+" ");
			sb.append(hlsvq.getBitrateLow()+"K");
		
			hlsvqId = hlsvqId+1;
		}
		
		return sb.toString();
	}
	
	private String createMapVideoOutPut_Cmd(VideoQuality sourceVq) throws HlsProcessingException
	{
		StringBuilder sb = new StringBuilder();
		List<VideoQuality> hlsvqList =getHlsSupportedVideoQualityList(sourceVq);
		if( hlsvqList.isEmpty())
		{
			throw new HlsProcessingException("Supported HLS Video Format List can not be empty for :"+sourceVq.getId());
		}
		int hlsvqId = 1;
		
		for (VideoQuality hlsvq : hlsvqList)
		{
			int vdId = hlsvqId -1 ;
			sb.append(" v:"+vdId);
			sb.append(",agroup:"+sourceVq.getAudioGroupname());
			sb.append(" ");
			
			hlsvqId = hlsvqId+1;
		
		}
		
		return sb.toString();
	}

	
	private VideoQuality extractImportmediaInfo(String videoPath) throws MediaException
	{
		VideoQuality vq = null;

		try {
			List<FFmpegStream> streamList = null;
			FFprobe ffprobe = new FFprobe(ffmpeg_dir_path+File.separator+"ffprobe");
			
			logger.info("Create HLS Stream Media FFProb Prepration :"+ffprobe.getPath());
			
			
			FFmpegProbeResult probeResult = ffprobe.probe(videoPath);

			logger.info("Create HLS Stream Media FFProb Done for VideoPath :"+videoPath);
			
			
			if (null != probeResult)
			{
				
				if(probeResult.getStreams().isEmpty())
				{
				throw new HlsProcessingException("Audio Video Stream can not be Empty for :"+videoPath);
				
				}
	
				FFmpegFormat format = probeResult.getFormat();
				
				logger.info("FileName:"+format.filename+", Duration:"+format.duration+", Size:"+format.size);
				/*
				if(probeResult.getStreams().size() > 2)
				{
					int i = 0;
					for (FFmpegStream stream : probeResult.streams)
					{
						logger.info("Stream :"+i+" ,codeac Name: "+stream.codec_name+" , Profile"+stream.profile);
						i++;
					}
				
					throw new HlsProcessingException("Audio Video Stream can not be more than 2 for :"+videoPath);
						
				}
				*/
				
				//System.out.println("================================= Stream Size:"+probeResult.streams.size());
				streamList = probeResult.getStreams();
				
				if( null != streamList)
				{
					vq =  getSupportedhlsResolution(videoPath, streamList);	
					
					
				}
				
				
			}	
		}
		catch(IOException |HlsProcessingException e)
		{
			throw new MediaException("Error Processing Import Media for Path:"+videoPath+", ERROR:"+e.getMessage()); 
		}
		
		
		return vq;
	}

	private List<VideoQuality> getHlsSupportedVideoQualityList(VideoQuality sourceVq)
	{
		List<VideoQuality> vqList = new ArrayList<VideoQuality>();
		
		for (int vqId : sourceVq.getHlsQualityList())
		{
			VideoQuality vq = this.vqMap.get(vqId);
			vqList.add(vq);
		}
		
		return vqList;
		
	}
	
	
	/**
	 * Findout Supported VideoQuality for Given Resoulation of Video
	 * @param width
	 * @param height
	 * @return
	 */
	private VideoQuality getSupportedhlsResolution(int width, int height, int streamId)
	{
		VideoQuality vq = null;
		int resKey = width+height;
		for ( int id : this.vqMap.keySet())
		{
			if(resKey <= id)
			{
				vq = this.vqMap.get(id);
				vq.setVideoStreamId(streamId);
				break;
			}
		}
		
	return vq;	
	}
	
	private VideoQuality getSupportedhlsResolution(String videoPath, List<FFmpegStream> stream) throws HlsProcessingException
	{
		
		VideoQuality vq = null;
		int videoStreamId = 0;
		int audotStreamId = 0;
			
		if(stream.isEmpty())
		{
			throw new HlsProcessingException("Audio Video Stream can not be empty  for :"+videoPath); 
		}
		
		/*
		if(stream.size() > 2)
		{
			throw new HlsProcessingException("Audio Video Stream can not be grather than 2 for :"+videoPath); 
		}
		
		*/
		
		if ( (stream.get(0).width > 0 ) && (stream.get(0).height > 0))
		{
			videoStreamId = 0;
			audotStreamId = 1;
		}
		else if ( (stream.get(1).width > 0 ) && (stream.get(1).height > 0))
			  {
			videoStreamId = 1;
			audotStreamId = 0;
		}
		
		int videoWidth = stream.get(videoStreamId).width;
		int videoHeight = stream.get(videoStreamId).height;
		int video_durationInSecond = (int) stream.get(videoStreamId).duration;
		
		
		int resKey = videoWidth+videoHeight;
		for ( int id : this.vqMap.keySet())
		{
			if(resKey <= id)
			{
				// Check if its Active
				if(this.vqMap.get(id).isActive())
				{
					vq = this.vqMap.get(id);
					vq.setVideoStreamId(videoStreamId);
					vq.setAudioStreamId(audotStreamId);
					vq.setVideoDurationInSecond(video_durationInSecond);
					
					break;	
				}
				
			}
		}
		
	return vq;	
	}


	private void ExecuteCmd(ImportMedia media) throws HlsProcessingException
	{
		
		try {
			
			Path mediaPath = Paths.get(media.getPath(), this.hlsFolderName);
		
			 if(!Files.exists(mediaPath, LinkOption.NOFOLLOW_LINKS))
				{
					Files.createDirectories(mediaPath);
						
				}
		
			
			OsType operatingOs = helper.getOperatingOs();
			
			 if(operatingOs.equals(OsType.Unix))
			 {
				 runLinuxCommand(media.getHlsCmd(), mediaPath.toString());
			 }
			 else if (operatingOs.equals(OsType.Windows))
			 {
				 runWindowsCommand(media.getHlsCmd(), mediaPath.toString());
			 }
			 else if ((operatingOs.equals(OsType.Mac))
					 ||(operatingOs.equals(OsType.Solaris)) )
			 {
				 throw new HlsProcessingException("OS:"+operatingOs.name()+"  not supported");
			 }
			 
			    // Set HLS Media Location into Import Media
			    media.setHlsMediaPath(mediaPath.toString());
			
			    logger.info("Create HLS Stream For Video Command  Exeuction Command Competed For:"+media.getHlsCmd());
				
		}
		catch(Exception e)
		{
			logger.error("Error in Createing HLS Stream Error:"+e.getMessage(), e);
			throw new HlsProcessingException(e.getMessage(), e);
		}
	}
	
	
	
	public void runWindowsCommand(String command, String mPath) throws HlsProcessingException
	{
		try {
			Path mediaPath = Paths.get(mPath);
			
			 if(!Files.exists(mediaPath, LinkOption.NOFOLLOW_LINKS))
				{
					 throw new IOException("Unable to load Media Path");
						
				}
			
			CommandLine cl = CommandLine.parse(command);
		    DefaultExecutor exec = new DefaultExecutor();
		    exec.setExitValues(null);
		    exec.setWorkingDirectory(mediaPath.toFile());
		    int result = exec.execute(cl);
		
		}
		catch(Exception e)
		{
			throw new HlsProcessingException(e.getMessage(), e);
		}
		
	}

	public void runLinuxCommand(String command, String mPath) throws HlsProcessingException
	{
	    Runtime rt = Runtime.getRuntime();
        BufferedReader is = null;
        StringBuffer outputReport = new StringBuffer();
        try {
        
        	Path mediaPath = Paths.get(mPath);
			
			 if(!Files.exists(mediaPath, LinkOption.NOFOLLOW_LINKS))
				{
					 throw new IOException("Unable to load Media Path");
						
				}
		
        	
        	ProcessBuilder pb = new ProcessBuilder("sh", "-c", command);
        	 pb.directory(mediaPath.toFile());
        	 pb.redirectErrorStream(true);

            
        	 Process proc =	pb.start();

            // read output of the rpm process
            is = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String tempStr = "";
            while ((tempStr = is.readLine()) != null ) {
            //	System.out.println(tempStr);
                outputReport.append(tempStr.replaceAll(">", "/>\n"));
                tempStr = "";
            }
             int inBuffer ;
             while ((inBuffer = is.read()) != -1) {
                outputReport.append((char) inBuffer);
             }
            // rawVersion = is.readLine();
            // response.append(rawVersion);
            is.close();
            proc.destroy();

        } catch (IOException e) {
            throw new HlsProcessingException(e.getMessage(), e);

        } finally {
            try {
                is.close();
            } catch (final IOException ioe) {
                System.out.println("Cannot close BufferedStream" + ioe);
            }
        }
        
    
	}




	@Override
	public void uploadHlsStream(String playListPath) throws HlsProcessingException {
	
		// Upload Hls Stream to MongoDB and create Media Object For Same
	}
	
	
	private boolean verifyPathforMasterPlayList(String playListPath)
	{
		boolean result = false;
		
		try {
			Path path = Paths.get(playListPath);
			
			 if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS))
				{
				 	 throw new HlsProcessingException("HLS MasterPlayList not Found on given Location:"+playListPath);
						
				}
			
			 InputStream fstream = new FileInputStream(path.toFile());
			 PlaylistParser parser = new PlaylistParser(fstream, Format.EXT_M3U, Encoding.UTF_8, ParsingMode.LENIENT);
			 Playlist playlist = parser.parse();
			 
			 if(!playlist.hasMasterPlaylist())
			 {
				 throw new HlsProcessingException("HLS MasterPlayList not Found on given Location:"+playListPath);
			 }
			 	  
			 
		
		}
		catch(Exception e)
		{
			System.out.println("Error Parsing HLS PlayList:"+e.getMessage());	
		}

		
		return result;
	}

	@Override
	public void createHlsStramForImportMedia(ImportMediaRequest imr) throws HlsProcessingException {
		
		try {
			// Create HLS Stream only if not created earlier
			if(!imr.isHlsStreamCreated())
			{
				String videoPath = imr.getImportedPath()+File.separator+imr.getImportFileName();
			
				logger.info("Create HLS Stream VideoPath :"+videoPath);
				
				
				ImportMedia im = create_hlsStram_from_Video(videoPath, imr.getMasterPlayListName());
					
				imr.setHlsStreamCreated(true);
				imr.setHlsMediaPath(im.getHlsMediaPath());
				imr.setDurationInSecond(im.getVideoDurationInSecond());
			}
				
		}
		catch(Exception e)
		{
			throw new HlsProcessingException("Exception in Creating HLS Stream for Import Media:"+imr.getId()
					+" , ERROR:"+e.getMessage());
		}
		

		
	}

}
