package com.anchor.app.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.anchor.app.exception.HlsMediaException;

@Component
@Scope("singleton")
public class HlsUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

/*	
	public HlsMasterPlayList createMasterPlayList(MediaConverionRequest req) throws HlsProcessingException
	{
		HlsMasterPlayList hmpl = null;
		
		hmpl = new HlsMasterPlayList(req.getId(),req.getExtension());
		hmpl.setExtended(req.isExtended());
		hmpl.setName(req.getName());
		
		if(req.getMstp().hasStartData())
		{
			HlsStartData hsd = new HlsStartData();
			hsd.setPrecise(req.getMstp().getStartData().isPrecise());
			hsd.setTimeOffset(req.getMstp().getStartData().getTimeOffset());
			
			hmpl.setStartData(hsd);
			
		}
		
		if((null == req.getMstp().getPlaylists()) || ( req.getMstp().getPlaylists().isEmpty()))
		{
			throw new HlsProcessingException("PlayList can not be Empty for MasterPlayList");
			
		}
		populatePlayListData(hmpl, req.getMstp().getPlaylists());
		
		if((null != req.getMstp().getIFramePlaylists()) && ( !req.getMstp().getIFramePlaylists().isEmpty()))
		{
			
			populateHlsIFrameStreamInfo(hmpl, req.getMstp().getIFramePlaylists()); 
		}
		
		if((null != req.getMstp().getMediaData()) && ( !req.getMstp().getMediaData().isEmpty()))
		{
			
			populateHlsMediaData(hmpl, req.getMstp().getMediaData());
		}
		
		if(req.getMstp().hasUnknownTags())
		{
			hmpl.getUnknownTagList().addAll(req.getMstp().getUnknownTags());
		}
		
		return hmpl;
		
	}
	
	*/
	
	/*
	private void populatePlayListData(HlsMasterPlayList hmpl, List<PlaylistData> pldList) throws HlsProcessingException
	{
		if(pldList.isEmpty())
		{
			throw new HlsProcessingException("PlayListData can not be Empty for MasterPlayList");
		}
		
		pldList.stream().forEach(pld -> {
			try {
				
				HlsPlayListData hpld = createHlsPlayListData(hmpl.getMediaId(), pld);
					if ( null != hpld)
					{
						hmpl.getPlayListDataList().add(hpld);
					}	
				
				
				
				
			} catch (Exception e) {
				// Swallowing Parallel Stream exception
				logger.error("Populate PlayListData Error for :"+pld.getUri()+", ERROR:"+e.getMessage());
			}
		});	
	}
	
	*/
	
	
	/*
	private HlsPlayListData createHlsPlayListData(String mediaId, PlaylistData pd)
	{
		String hpl_id = mediaId+"_"+pd.getUri();
		
		
		HlsPlayListData hpld = new HlsPlayListData(hpl_id, mediaId);
		hpld.setUri(pd.getUri());
		
		// Create HlsStreamInfo
		HlsStreamInfo hsi = new HlsStreamInfo();
		hsi.setId(hpl_id);
		
		
		if(pd.getStreamInfo().hasAudio())
		{
			hsi.setAudio(pd.getStreamInfo().getAudio());
				
		}
		if(pd.getStreamInfo().hasAverageBandwidth())
		{
			hsi.setAverageBandwidth(pd.getStreamInfo().getAverageBandwidth());
				
		}
		
		hsi.setBandwidth(pd.getStreamInfo().getBandwidth());
		if(pd.getStreamInfo().hasClosedCaptions())
		{
			hsi.setClosedCaptions(pd.getStreamInfo().getClosedCaptions());
				
		}
		
		if(pd.getStreamInfo().hasFrameRate())
		{
			hsi.setFrameRate(pd.getStreamInfo().getFrameRate());
			
				
		}
		
		if(pd.getStreamInfo().hasResolution())
		{
			
			// Create Resolution
			
			HlsResolution hr = new HlsResolution(pd.getStreamInfo().getResolution().width, pd.getStreamInfo().getResolution().height);
			hsi.setResolution(hr);		
		}
		
		
		if(pd.getStreamInfo().hasSubtitles())
		{
			hsi.setSubtitles(pd.getStreamInfo().getSubtitles());
		}
		
		if(pd.getStreamInfo().hasVideo())
		{
			hsi.setVideo(pd.getStreamInfo().getVideo());
		}
		
		if(pd.getStreamInfo().hasAudio())
		{
			hsi.setAudio(pd.getStreamInfo().getAudio());
		}
		
		if(pd.getStreamInfo().hasCodecs())
		{
			hsi.getCodecList().addAll(pd.getStreamInfo().getCodecs());
		}
		
		
		hpld.setStreamInfo(hsi);
		
		return hpld;
	}
	
	*/
	
	
	/*
	private void populateHlsIFrameStreamInfo(HlsMasterPlayList hmpl, List<IFrameStreamInfo> ifpList) throws HlsProcessingException
	{
		if(ifpList.isEmpty())
		{
			throw new HlsProcessingException("IFrameStreamInfo can not be Empty for MasterPlayList");
		}
		
		ifpList.stream().forEach(ifi -> {
			try {
				
				HlsIFrameStreamInfo hifi = createHlsIFrameStreamInfo(hmpl.getMediaId(), ifi);
					if ( null != hifi)
					{
						hmpl.getiFrameStreamInfolist().add(hifi);
					}	
			} catch (Exception e) {
				// Swallowing Parallel Stream exception
				logger.error("Populate IFrameStreamInfo Error for :"+ifi.getUri()+", ERROR:"+e.getMessage());
			}
		});	
	}
	
	*/
	
	
	/*
	private HlsIFrameStreamInfo createHlsIFrameStreamInfo(String mediaId, IFrameStreamInfo ifi)
	{
		String hidi_id = mediaId+"_"+ifi.getUri();
		HlsIFrameStreamInfo hifi = new HlsIFrameStreamInfo();
		hifi.setId(hidi_id);
		
		hifi.setBandwidth(ifi.getBandwidth());
		hifi.setUri(ifi.getUri());
		
		if(ifi.hasAverageBandwidth())
		{
			hifi.setAverageBandwidth(ifi.getAverageBandwidth());
		}
		
		if(ifi.hasCodecs())
		{
			hifi.getCodecs().addAll(ifi.getCodecs());
		}
		
		if(ifi.hasFrameRate())
		{
			hifi.setFrameRate(ifi.getFrameRate());
		}
		
		if(ifi.hasResolution())
		{
			HlsResolution hr = new HlsResolution(ifi.getResolution().width, ifi.getResolution().height);
			hifi.setResolution(hr);		
		}
		
		if(ifi.hasVideo())
		{
			hifi.setVideo(ifi.getVideo());
		}
		
		
		return hifi;
		
	}
	
	*/
	
	/*
	private void populateHlsMediaData(HlsMasterPlayList hmpl, List<MediaData> mdList) throws HlsProcessingException
	{
		if(mdList.isEmpty())
		{
			throw new HlsProcessingException("MediaDataList  can not be Empty for MasterPlayList");
		}
		
		mdList.stream().forEach(md -> {
			try {
				
				HlsMediaData hmd =createHlsMediaData(hmpl.getMediaId(),md);
					if ( null != hmd)
					{
						hmpl.getMediaList().add(hmd);
					}	
			} catch (Exception e) {
				// Swallowing Parallel Stream exception
				logger.error("Populate MediaData Error for :"+md.getGroupId()+", ERROR:"+e.getMessage());
			}
		});	
	}
	
	*/
	
	
	/*
	private HlsMediaData createHlsMediaData(String mediaId, MediaData md)
	{
		String hmd_id = mediaId+"_"+md.getGroupId();
		HlsMediaData hmd = new HlsMediaData();
		
		hmd.setId(hmd_id);
		hmd.setMediaId(mediaId);
		if(md.hasAssociatedLanguage())
		{
			hmd.setAssociatedLanguage(md.getAssociatedLanguage());
			
		}
		
		if(md.hasChannels())
		{
			hmd.setChannels(md.getChannels());
		}
		
		if(md.hasCharacteristics())
		{
			hmd.getCharacteristicsList().addAll(md.getCharacteristics());
		}
		
		if(md.hasInStreamId())
		{
			hmd.setInStreamId(md.getInStreamId());
		}
		
		if(md.hasLanguage())
		{
			hmd.setLanguage(md.getLanguage());
		}
		
		if(md.hasUri())
		{
			hmd.setUri(md.getUri());
		}
		
		hmd.setDefault(md.isDefault());
		hmd.setForced(md.isForced());
		hmd.setGroupId(md.getGroupId());
		hmd.setMediaType(HlsMediaType.valueOf(md.getType().getValue()));
		hmd.setName(md.getName());
	
		return hmd;
	}
	
	*/
	
	
	
	
	/*
	private void populateHlsMediaPlayListDetails(String basePath, HlsMasterPlayList hmst) throws HlsProcessingException
	{
		if(hmst.getPlayListDataList().isEmpty())
		{
			throw new HlsProcessingException("PlayListDataList can not be Empty for MasterPlayList");
		}
		
		hmst.getPlayListDataList().stream().forEach(md -> {
			try {
				
				
			//	HlsMediaData hmd =createHlsMediaData(hmpl.getMediaId(),md);
				//	if ( null != hmd)
				//	{
				//		hmpl.getMediaList().add(hmd);
			//		}
				//	
						
			} catch (Exception e) {
				// Swallowing Parallel Stream exception
				logger.error("Populate MediaData Error for :"+md.getId()+", ERROR:"+e.getMessage());
			}
		});	
	}
	
	*/
	
	public String getHlsPlayList(String mediaId, String fileName) throws HlsMediaException
	{
		String result = null;
		
		try {
			Path mediaLocation = env.getYoutube_StaggingLocation();
			String hlsLocation = env.getHlsFolderName();
			
			
			Path playListPath = Paths.get(mediaLocation.toString(), mediaId, hlsLocation, fileName);
			
			 if(!Files.exists(playListPath, LinkOption.NOFOLLOW_LINKS))
				{
				 	 throw new HlsMediaException("Unable to find Media PlayList on Location:"+playListPath.toString());
						
				}

			  result = FileUtils.readFileToString(playListPath.toFile(), "UTF-8");
	
		}
		catch(Exception e)
		{
			throw new HlsMediaException("Unable to load Media PlayList with Name:"+fileName);	
		}
				
		return result;
	}
	
	public InputStream getSegmentSetream(String mediaId, String segLocation, String fileName) throws HlsMediaException
	{
		InputStream segStrem = null;
		try {
			Path mediaLocation = env.getYoutube_StaggingLocation();
			String hlsLocation = env.getHlsFolderName();
			
			
			Path playListPath = Paths.get(mediaLocation.toString(), mediaId, hlsLocation, segLocation,  fileName);
			
			 if(!Files.exists(playListPath, LinkOption.NOFOLLOW_LINKS))
				{
				 	 throw new HlsMediaException("Unable to find Media PlayList on Location:"+playListPath.toString());
						
				}

			 segStrem = FileUtils.openInputStream(playListPath.toFile());
	
		}
		catch(Exception e)
		{
			throw new HlsMediaException("Unable to load Media PlayList with Name:"+fileName);	
		}
		
		return segStrem;
	}
}
