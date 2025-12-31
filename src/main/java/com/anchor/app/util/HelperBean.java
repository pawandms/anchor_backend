package com.anchor.app.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.enums.CollectionType;
import com.anchor.app.enums.DepartmentType;
import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ExtensionType;
import com.anchor.app.enums.ExternalSystemType;
import com.anchor.app.enums.GenderType;
import com.anchor.app.enums.HLSPropertyType;
import com.anchor.app.enums.HlsPlayListType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.enums.ImportStatusType;
import com.anchor.app.enums.MediaGenreType;
import com.anchor.app.enums.MediaStreamType;
import com.anchor.app.enums.OsType;
import com.anchor.app.enums.PeopleType;
import com.anchor.app.enums.RepresentationType;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.enums.StatusType;
import com.anchor.app.event.model.EventLog;
import com.anchor.app.exception.ConversionException;
import com.anchor.app.exception.CountryException;
import com.anchor.app.exception.GenreException;
import com.anchor.app.exception.HlsProcessingException;
import com.anchor.app.exception.ImageException;
import com.anchor.app.exception.ImporterException;
import com.anchor.app.exception.InvalidMasterPlayListException;
import com.anchor.app.exception.InvalidMediaPathException;
import com.anchor.app.exception.LanguageException;
import com.anchor.app.exception.MediaException;
import com.anchor.app.exception.PeopleException;
import com.anchor.app.exception.SequencerException;
import com.anchor.app.exception.UserException;
import com.anchor.app.exception.UserServiceException;
import com.anchor.app.exception.ValidationException;
import com.anchor.app.importer.model.BelongsToCollection;
import com.anchor.app.importer.model.CompanyImport;
import com.anchor.app.importer.model.CountryImport;
import com.anchor.app.importer.model.GenreImport;
import com.anchor.app.importer.model.ImportMediaRequest;
import com.anchor.app.importer.model.LanguageImport;
import com.anchor.app.importer.model.MovieCollectionImport;
import com.anchor.app.importer.model.MovieImport;
import com.anchor.app.importer.model.MovieTranslationImport;
import com.anchor.app.importer.model.PersonImport;
import com.anchor.app.importer.model.TmdbCast;
import com.anchor.app.importer.model.TmdbCollection;
import com.anchor.app.importer.model.TmdbCrew;
import com.anchor.app.importer.model.TmdbMovie;
import com.anchor.app.importer.model.TmdbMovieCredit;
import com.anchor.app.importer.model.TmdbPerson;
import com.anchor.app.importer.model.TranslationImport;
import com.anchor.app.importer.model.YouTubeImportRequest;
import com.anchor.app.importer.model.YouTubeMedia;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.media.model.Person;
import com.anchor.app.model.Cast;
import com.anchor.app.model.Country;
import com.anchor.app.model.Crew;
import com.anchor.app.model.Genre;
import com.anchor.app.model.Image;
import com.anchor.app.model.Language;
import com.anchor.app.model.MediaLanguage;
import com.anchor.app.model.Movie;
import com.anchor.app.model.MovieCollection;
import com.anchor.app.model.MovieCredit;
import com.anchor.app.model.PersonCredit;
import com.anchor.app.model.Production_Country;
import com.anchor.app.model.Translation;
import com.anchor.app.model.TranslationData;
import com.anchor.app.model.hls.HlsMasterPlayList;
import com.anchor.app.model.hls.HlsPlayList;
import com.anchor.app.model.hls.HlsResolution;
import com.anchor.app.model.hls.HlsSegment;
import com.anchor.app.model.hls.HlsVideo;
import com.anchor.app.model.vo.ImageInfo;
import com.anchor.app.msg.enums.ChannelSubType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.enums.ChannelVisibility;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.EventType;
import com.anchor.app.msg.enums.MsgAttachmentType;
import com.anchor.app.msg.enums.MsgBoxType;
import com.anchor.app.msg.enums.UserActionType;
import com.anchor.app.msg.enums.UserConnectionStatusType;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.msg.enums.VisibilityType;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.model.ChannelMsgRelation;
import com.anchor.app.msg.model.ChannelParticipant;
import com.anchor.app.msg.model.ChnlMsgAttribute;
import com.anchor.app.msg.model.EventNotification;
import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.model.UserConnection;
import com.anchor.app.msg.vo.ChannelUser;
import com.anchor.app.msg.vo.CreateChannelVo;
import com.anchor.app.msg.vo.MessageActionVo;
import com.anchor.app.msg.vo.SearchChannelVo;
import com.anchor.app.msg.vo.SearchUserVo;
import com.anchor.app.msg.vo.SearchVo;
import com.anchor.app.msg.vo.UserActionVo;
import com.anchor.app.msg.vo.UserNotificationResp;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.vo.UserVo;
import com.anchor.app.repository.CountryRepository;
import com.anchor.app.repository.GenreRepository;
import com.anchor.app.repository.LanguageRepository;
import com.anchor.app.sequencer.service.Sequencer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lindstrom.m3u8.model.MasterPlaylist;
import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.model.PlaylistType;
import io.lindstrom.m3u8.model.Resolution;
import io.lindstrom.m3u8.model.Variant;
import io.lindstrom.m3u8.parser.MasterPlaylistParser;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;
import jakarta.annotation.PostConstruct;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

@Component
@Scope("singleton")
public class HelperBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private GenreRepository genreRep;
	
	@Autowired
	private LanguageRepository lanRep;
	
	@Autowired
	private CountryRepository conRep;
	
	@Autowired
    PasswordEncoder passwordEncoder;

	public static String DATE_FORMAT_WITHOUT_TIME = "yyyy-MM-dd";
    
	
	//private String mediaurl= "http://localhost:8102/api/media/";

//	private static HelperBean instance;
//	HashMap<Integer,String> nodeidMap;
	
	//SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
	
	Pattern nameVerificationPattern;

	//TmdbId->Genre HashMap
	Map<String,Genre> genreMap;

	Map<String,Genre> genreByNameMap;

	// isoCode1 -> Language HashMap
	Map<String,Language> languageMap = new HashMap<>();
	
	//isoCode1 -> Country Map
	Map<String,Country> countryMap;
	
	@PostConstruct
	public void initHelperBean() throws GenreException, LanguageException, CountryException
	{
		// Populate GenreMap with Key as TmdbID for Respective Genre
		//populateGenreMap();
		populateLanguageMap();
		//populateCountryMap();
	}
	
	
	private void populateGenreMap() throws GenreException
	{
		List<Genre> genreList = genreRep.findAll();
		if(genreList.isEmpty())
		{
			throw new GenreException("Genre List Fetched from DB is Empty");
		}
		
		this.genreMap = convertGenresToMap(genreList);
		
		this.genreByNameMap = convertGenresToMapByName(genreList);
	}
	
	private void populateLanguageMap() throws LanguageException
	{
		List<Language> lanList = lanRep.findAll();
		if(lanList.isEmpty())
		{
			logger.error("Language Map is Empty in DB");
		
			//throw new LanguageException("Language List Fetched from DB is Empty");
		}
		else {
			languageMap = convertLanguageToMap(lanList);	
		}
		
		
	}
	
	private void populateCountryMap() throws CountryException
	{
		List<Country> conList = conRep.findAll();
		if(conList.isEmpty())
		{
			throw new CountryException("Country List Fetched from DB is Empty");

		}
		countryMap = convertCountryToMap(conList);
	}
	
	
	 @Async
	 public Genre getGenrebyTmdbId(String tmdbId) throws GenreException
	 {
		 Genre gen = null;
		 
		 if( null == tmdbId)
		 {
			 throw new GenreException("Genre TmdbID Can not be Null");
		 }
		 if(genreMap.containsKey(tmdbId))
		 {
			 gen = genreMap.get(tmdbId);
		 }
		 else {
			 throw new GenreException("Genre for Requested TmdbID:"+tmdbId+" not Available in System");
		 }
			 
		 return gen;
	 }
	 
	 @Async
	 public Genre getGenrebyName(String name) throws GenreException
	 {
		 Genre gen = null;
		 
		 if( null == name)
		 {
			 throw new GenreException("Genre Name Can not be Null");
		 }
		 if(genreByNameMap.containsKey(name))
		 {
			 gen = genreMap.get(name);
		 }
		 else {
			 throw new GenreException("Genre for Requested TmdbID:"+name+" not Available in System");
		 }
			 
		 return gen;
	 }
	 
	 
	 @Async
	 public Language getLanguagebyisoCode1(String isoCode) throws LanguageException
	 {
		 Language lan = null;
		 
		 if( null == isoCode)
		 {
			 throw new LanguageException("Language Code Can not be Null");
		 }
		 if(languageMap.containsKey(isoCode))
		 {
			 lan = languageMap.get(isoCode);
		 }
			 
		 return lan;
	 }
	 
	 public MediaLanguage getMediaLanguagebyisoCode1(String isoCode) 
	 {
		 MediaLanguage mlan = new MediaLanguage();
		 try {
			 if( null == isoCode)
			 {
				 throw new LanguageException("Language Code Can not be Null"); 	 
			 }
			 
			 
			 Language lan = getLanguagebyisoCode1(isoCode);
			 
			 if ( null != lan)
			 {
				 mlan.setId(lan.getId());
				 mlan.setName(lan.getName());
				 mlan.setNativeName(lan.getNativeName());
				 mlan.setIsoCode(lan.getIsoCode1());
			 }
			 else {
				 mlan.setIsoCode(isoCode);
			 }
				 
		 }
		 catch(LanguageException e)
		 {
			 
		 }
		  
		 return mlan;
		 
	 }
	
	 public Production_Country getProductionCountrybyisoCode1(String isoCode) throws CountryException
	 {
		 Production_Country pc = null;
		 if( null == isoCode)
		 {
			 throw new CountryException("Country Code Can not be Null"); 	 
		 }
		 
		 Country con = getCountrybyisoCode1(isoCode);
		 
		 if ( null != con)
		 {
			pc = new Production_Country();
			pc.setId(con.getId());
			pc.setIsoCode2(con.getIsoCode2());
			pc.setName(con.getName());
		 }
		 
		 return pc;
		 
	 }
	
	 @Async
	 public Country getCountrybyisoCode1(String isoCode) throws CountryException
	 {
		 Country con = null;
		 
		 if( null == isoCode)
		 {
			 throw new CountryException("Country Code Can not be Null");
		 }
		 if(countryMap.containsKey(isoCode))
		 {
			 con = countryMap.get(isoCode);
		 }
		 else {
			 throw new CountryException("Country for Requested isoCode:"+isoCode+" not Available in System");
		 }
			 
		 return con;
	 }
	 /**
	  * Get System Id for Respective TmdbGenre
	  * @param tmdbId
	  * @return
	  */
	 @Async
	 public String getGenereId_forTmdbID(String tmdbId)
	 {
		 String result = null;
		 try {
			 Genre gen = getGenrebyTmdbId(tmdbId);
			result = gen.getId();	
		 }
		 catch(GenreException e)
		 {
			 
		 }
		 return result;
	 }

	 /**
	  * Populate GenreMap by tmdb_id
	  * @param genreList
	  * @return
	  */
	private Map<String, Genre> convertGenresToMap(List<Genre> genreList) {
			
		 Map<String, Genre> map = new HashMap<>();
		 
		 genreList.stream().forEach(gen -> {
				try {
				if(!map.containsKey(gen.getTmdbid()))
				{
					map.put(gen.getTmdbid(), gen);
				}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+gen.getId(), e);
				}
			});
			
		    return map;
		}
	
	
	/**
	 * Populate GenreMap by GenreName
	 * @param genreList
	 * @return
	 */
	private Map<String, Genre> convertGenresToMapByName(List<Genre> genreList) {
		
		 Map<String, Genre> map = new HashMap<>();
		 
		 genreList.stream().forEach(gen -> {
				try {
				if(!map.containsKey(gen.getName()))
				{
					map.put(gen.getName(), gen);
				}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Populate GenreMap Error for :"+gen.getId(), e);
				}
			});
			
		    return map;
		}

	private Map<String, Language> convertLanguageToMap(List<Language> lanList) {
		
		 Map<String, Language> map = new HashMap<>();
		 
		 lanList.stream().forEach(lan -> {
				try {
				if(!map.containsKey(lan.getIsoCode1()))
				{
					map.put(lan.getIsoCode1(), lan);
				}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+lan.getName(), e);
				}
			});
			
		    return map;
		}
	
	
	private Map<String, Country> convertCountryToMap(List<Country> conList) {
		
		 Map<String, Country> map = new HashMap<>();
		 
		 conList.stream().forEach(con -> {
				try {
				if(!map.containsKey(con.getIsoCode1()))
				{
					map.put(con.getIsoCode1(), con);
				}
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+con.getName(), e);
				}
			});
			
		    return map;
		}
	
	  /**
	   * Process HlsMediaFiles Location Path 
	   * @param videoPath
	 * @throws InvalidMediaPathException 
	 * @throws IOException 
	 * @throws InvalidMasterPlayListException 
	   */
	  public HlsVideo processHlsMediaFiles(String movieId, Path videoPath) throws HlsProcessingException 
	  {
		  String crBy = "SYSTEM";
		  Date crDate = new Date();
		  HlsVideo hlsvideo = null;
		 // HlsMasterPlayList hlsmst = null;
		  try {
			  Map<String,File> playListMap = new HashMap<String,File>();
			  Map<String,File> segmentListMap = new HashMap<String,File>();
			  if((!Files.exists(videoPath)) || (!videoPath.toFile().isDirectory()))
		        {
				  throw new InvalidMediaPathException("HLS Media Path not exits or not a directory");
		        }
		       
			  
			  List<File> fileList = Arrays.asList(videoPath.toFile().listFiles());
	        	
	        	int totalFiles = fileList.size();
	        	
	        	for (File file : fileList)
	        	{
	        		if(file.isFile())
	        		{
	        			ExtensionType filetype =  getHlsfileType(file);
	        			
	        			if(filetype.equals(ExtensionType.M3U8))
	        			{
	        				playListMap.put(file.getName(), file);
	        			}
	        			else if (filetype.equals(ExtensionType.TS))
	        			{
	        				segmentListMap.put(file.getName(), file);
	        			}
	        			
	        		}
	        		
	        		
	        	}
	        	if(!playListMap.isEmpty())
	        	{
	        		if(segmentListMap.isEmpty())
	        		{
	        			throw new InvalidMediaPathException("Unable to find HlsSegment Files at given Path:"+videoPath.toString());
	        		}
	        		hlsvideo = new HlsVideo(movieId);
	        		 parsehlsMasterPlaylist(hlsvideo, playListMap,segmentListMap, movieId, crBy,crDate);
	        		
	        		// Add HlsSegment File Reference to HlsSegment Object
	        	}  
		  }
		  catch (InvalidMasterPlayListException | IOException | InvalidMediaPathException e)
		  {
			  throw new HlsProcessingException(e.getMessage(), e);
		  }
		  
		  return hlsvideo;
		  
	  }
	  
	  
	  
		public void parsehlsMasterPlaylist(HlsVideo hlsvideo, Map<String,File> playListMap, Map<String,File> segmentListMap, String movieId, String crBy, Date crDate) throws InvalidMasterPlayListException, InvalidMediaPathException, IOException  
		{
			HlsMasterPlayList hlsmst = null;
				if(!playListMap.containsKey(HLSPropertyType.HLS_MST_PLAYLIST_NAME.getValue()))
				{
					throw new InvalidMediaPathException("PlayList Path is not having Master PlayList with Name:playlist.m3u8");
				}
				
				File mstFile = playListMap.get(HLSPropertyType.HLS_MST_PLAYLIST_NAME.getValue());
				
			    logger.info("MasterPlayList Path:"+mstFile.getName());  
				MasterPlaylistParser parser = new MasterPlaylistParser();

				// Parse playlist
				MasterPlaylist playlist = parser.readPlaylist(mstFile.toPath());

				if(playlist.variants().isEmpty())
				{
				throw new InvalidMasterPlayListException("MasterPlayList should have Varients Details");	
				}
				
				
				hlsmst = new HlsMasterPlayList(movieId, ExtensionType.M3U8);
				//hlsmst.setExtension(ExtensionType.M3U8);
				hlsmst.setName(getNameofFileName(mstFile.getName()));
				Optional<Integer> version = playlist.version();
				if(version.isPresent())
				{
					hlsmst.setVersion(version.get());	
				}
				else {
					// Set Default Version as 3
					hlsmst.setVersion(3);
					
				}
				
				
				for ( Variant var : playlist.variants())
				{
					
					if(!playListMap.containsKey(var.uri()))
					{
						throw new InvalidMasterPlayListException("Actual File Not Present on Path for Varient:"+var.uri());
					}
					File varFile = playListMap.get(var.uri());
					
					HlsPlayList hlsplaylist =	processMediaPlayListFile(hlsvideo, var, segmentListMap,hlsmst, varFile, crBy, crDate);
				
					//hlsmst.getPlayList().add(hlsplaylist);
				}
				
				hlsvideo.setMstPlayList(hlsmst);
			
		}
		
		
		private HlsPlayList processMediaPlayListFile(HlsVideo hlsvideo, Variant var, Map<String,File> segmentListMap,HlsMasterPlayList hlsmst, File file, String crBy, Date crDate) throws InvalidMasterPlayListException, IOException
		{
				
				HlsPlayList hlsplaylist = null;
				MediaPlaylistParser parser = new MediaPlaylistParser();

				
				// Parse playlist
				MediaPlaylist playlist = parser.readPlaylist(file.toPath());
				
				hlsplaylist = new HlsPlayList(hlsmst.getMediaId(), file.getName());
				//ExtensionType hlsextn = ExtensionType.getType(getExtensionofFileName(file.getName()));
				ExtensionType hlsextn = getHlsfileType(file);
				if( null == hlsextn)
				{
					// Default hls Extension
					hlsextn = ExtensionType.M3U8;
				}
				hlsplaylist.setExtension(hlsextn);
				hlsplaylist.setFullName(file.getName());
				hlsplaylist.setMediaSegmentCount(playlist.mediaSegments().size());
				hlsplaylist.setName(getNameofFileName(file.getName()));
				hlsplaylist.setTargetDuration(playlist.targetDuration());
				hlsplaylist.setType(HlsPlayListType.VOD);
				
				Optional<Integer> version = playlist.version();
				if(version.isPresent())
				{
					hlsplaylist.setVersion(version.get());	
				}
				else {
					// Set Default Version as 3
					hlsplaylist.setVersion(3);
					
				}
				
				// Add Varient Level Details to HLS PlayList
				
				hlsplaylist.setBandWidth(var.bandwidth());
				Optional<Resolution> res = var.resolution();
				if(!res.isPresent())
				{
					throw new InvalidMasterPlayListException("PlayList Resolution is Missing for :"+hlsplaylist.getFullName());
				}
				HlsResolution hlsres = new HlsResolution(res.get().width(), res.get().height());
			
				RepresentationType resType = RepresentationType.valueOf(hlsres.getHeight());
				hlsplaylist.setRepresentationType(resType);
				hlsplaylist.setResolution(hlsres);
				
				// Prepare Segment Map
				processHlsSegment(hlsvideo,segmentListMap,playlist, hlsplaylist, crBy,crDate);
				
				
				return hlsplaylist;
		}
		
		// Create HlsSegment from MediaPlayList Object
		private void processHlsSegment(HlsVideo hlsvideo, Map<String,File> segmentListMap,MediaPlaylist playlist, HlsPlayList hlsplaylist, String crBy, Date crDate) throws InvalidMasterPlayListException
		{
			if(playlist.mediaSegments().isEmpty())
			{
				throw new InvalidMasterPlayListException("Segment List can not be null for HlsPlayList");
			}
			
			int i = 0;
			for (MediaSegment segment : playlist.mediaSegments() )
			{
			
				HlsSegment seg = new HlsSegment(hlsplaylist.getMovieId(), hlsplaylist.getId(), segment.uri());
				seg.setSequenceNo(i);
				seg.setCreatedBy(crBy);
				seg.setCreatedOn(crDate);
				seg.setDuration(segment.duration());
				//ExtensionType segextn = ExtensionType.valueOf(getExtensionofFileName(segment.uri()));
				ExtensionType segextn = getHlsfileType(segment.uri());
				if( null == segextn)
				{
					// Default Segment Extension
					segextn = ExtensionType.TS;
				}
				
				seg.setExtension(segextn);
				seg.setFullName(segment.uri());
				seg.setName(getNameofFileName(segment.uri()));
				seg.setReptype(hlsplaylist.getRepresentationType());
				// Default HLS Media Stream
				seg.setStreatype(MediaStreamType.HLS);
				
				// Set Segment File Reference
				
				seg.setSegFile(segmentListMap.get(segment.uri()));
				
				// Add to HLS PlayList
				//hlsplaylist.getSegmentList().add(seg);
				hlsvideo.getSegment().add(seg);
				i = i+1;
				
			}
			
			hlsplaylist.setSegmentCount(playlist.mediaSegments().size());
			
			
		}
		
	    private String getFilePath() {
	        URL url = this.getClass().getResource("/hls");
	        return new File(url.getFile()).getAbsolutePath();
	    }

	    /**
	     * Get Extension of FileName
	     * @param filename
	     * @return
	     */
	    public String getExtensionofFileName(String fileName) {
		    return FilenameUtils.getExtension(fileName);
		}	
	    
	    /**
	     * Get Name of File Without Extension
	     * @param fileName
	     * @return
	     */
	    public String getNameofFileName(String fileName)
	    {
	    	return FilenameUtils.getName(fileName);
	    }
	    
	    /**
	     * Check if Given File is HLS PlayList file i.e Extension is m3u8
	     * @param file
	     * @return
	     */
	    private ExtensionType getHlsfileType(File file)
	    {
	    	ExtensionType type = null;
	    	String fileName = file.getName();
	    	String extension = getExtensionofFileName(fileName);
	    	type = ExtensionType.getType(extension);
	    	
	    	return type;
	    	
	    }
	    
	    private ExtensionType getHlsfileType(String fileName)
	    {
	    	ExtensionType type = null;
	    	String extension = getExtensionofFileName(fileName);
	    	type = ExtensionType.getType(extension);
	    	
	    	return type;
	    	
	    }
	    
	    /**
	     * Prepare HLS MasterPlaylist from DB Object
	     * @param hlsmstPlayList
	     * @return
	     */
	    public String prepareHlsMasterPlayListXML(HlsMasterPlayList hlsmstPlayList)
	    {
	    	String result = null;
	    	MasterPlaylist mstplaylist = null;
	    	MasterPlaylistParser parser = new MasterPlaylistParser();
	    	
	    	if(null != hlsmstPlayList)
	    	{
	    		List<Variant> varList =  prepareVarientList(hlsmstPlayList);
	    		
	    		mstplaylist = MasterPlaylist.builder()
	    				.version(hlsmstPlayList.getVersion())
	    				.addAllVariants(varList)
	    				.build();
	    			
	    	}
	    	result = parser.writePlaylistAsString(mstplaylist);
	    	logger.info("Master PlayList XML:"+result);
	    	return result;
	    }
	    
	    private List<Variant> prepareVarientList(HlsMasterPlayList hlsmstPlayList)
	    {
	    	List<Variant> varList = new ArrayList<Variant>();
	    	
	    	String playListURI = env.getPlaylistUrl();
	   
	    	/*
	    	for (HlsPlayList playlist : hlsmstPlayList.getPlayList())
	    	{
	    		String uri = playListURI+playlist.getId();
	    		Variant var = Variant.builder()
	    					 .resolution(playlist.getResolution().getWidth(), playlist.getResolution().getHeight())
	    					 .bandwidth(playlist.getBandWidth())
	    					 .uri(uri)
	    					 .build();
	    					varList.add(var);	
	    	}
	    	
	    	*/
	    	
	    	return varList;
	    }
	    
	    
	  
	    
	    /**
	     * Get PlayList for Specific PlayListID
	     * @param mstplayList
	     * @param playlistId
	     * @return
	     * @throws InvalidMasterPlayListException 
	     */
	    public HlsPlayList getPlayListById(HlsMasterPlayList mplList, String plId) throws InvalidMasterPlayListException
	    {
	    	HlsPlayList playlist = null;
	    
	    	/*
	    	if(mplList.getPlayList().isEmpty())
	    	{
	    		throw new InvalidMasterPlayListException("PL can not be Empty for MPL ID:"+mplList.getId());
	    	}
	    	
	    	
	    	
	    	for (HlsPlayList plo : mplList.getPlayList())
	    	{
	    		if(plo.getId().equalsIgnoreCase(plId))
	    		{
	    			playlist = plo;
	    			break;
	    		}
	    	}
	    	
	    	*/
	    	return playlist;
	    }
	    
	    
	    public String prepareHlsPlayListXML(HlsPlayList playList ,List<HlsSegment> segList)
	    {
	    	String result = null;
	    	MediaPlaylist plList = null;
	    	MediaPlaylistParser parser = new MediaPlaylistParser();
	    	
	    	if(null != playList)
	    	{
	    		 List<MediaSegment> msegList  =  prepareMediaSegmentList(playList ,segList);
	    		 PlaylistType pltype =  generatePlayListType(playList.getType());
	    		 
	 	
	    		 plList = MediaPlaylist.builder()
	    				 .version(playList.getVersion())
	    				 .targetDuration(playList.getTargetDuration())
	    				 .playlistType(pltype)
	    				 .addAllMediaSegments(msegList)
	    				 .build();
	    		 
	    		
	    		 
	    			
	    	}
	    	result = parser.writePlaylistAsString(plList);
	    	result = result.concat("#EXT-X-ENDLIST");
	    	//System.out.println("Media PlayList XML:"+result);
	    	return result;
	    }
	    
	    
	    private List<MediaSegment> prepareMediaSegmentList(HlsPlayList playList ,List<HlsSegment> segList)
	    {
	    	List<MediaSegment> msegList = new ArrayList<MediaSegment>();
	    	
	    	String segmentURI = env.getSegmentUrl();
	    	for (HlsSegment segment : segList)
	    	{
	    		String uri = segmentURI+segment.getId();
	    		MediaSegment mseg = MediaSegment.builder()
	    							.duration(segment.getDuration())
	    							.uri(uri)
	    							.build();
	    		
	    		msegList.add(mseg);	
	    	}
	    	
	    	return msegList;
	    }
	    
	    private PlaylistType generatePlayListType(HlsPlayListType hlstype)
	    {
	    	PlaylistType type = null;
	    	
	    	if(hlstype.equals(HlsPlayListType.VOD))
	    	{
	    		type = PlaylistType.VOD;
	    		
	    	}
	    	else {
	    		type = PlaylistType.EVENT;
	    	}
	    	
	    	return type;
	    }
	    
	    public String getSequanceNo(SequenceType type) throws SequencerException
	    {
	    	String seqStr = null;
	    	Sequencer seq = Sequencer.getInstance(type.name());
	    	
	    	Long seqNo = seq.next();
	    	if (seqNo.equals(Long.valueOf(0)))
			{
	    		seqNo = seq.next();
			}
	    	
	    	int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH);
	    	seqStr = year+"-"+month+"-"+String.valueOf(seqNo);
	    	
	    	return seqStr;
	    }
	    
	    
	    
	    public Integer getIntegerSequanceNo(SequenceType type) throws SequencerException
	    {
	    	Sequencer seq = Sequencer.getInstance(type.name());
	    	
	    	Long seqNo = seq.next();
	    	if (seqNo.equals(new Long(0)))
			{
	    		seqNo = seq.next();
			}
	    	Integer sqNo = seqNo.intValue();
	    	
	    	return sqNo;
	    }
	    
	    public ImageInfo getImageInfo(MultipartFile file) throws ImageException
	    {
	    	ImageInfo imgInfo = null;
	    	
	    	String type = file.getContentType();
	    	try {
	    		if(file.isEmpty())
	    		{
	    			throw new ImageException("No file present");
	    		}
	    		if( null == type)
		    	{
		    		throw new ImageException("Invalid File Type");
		    	}
		    	
	    		 BufferedImage image=ImageIO.read (file.getInputStream());
	    		 if(image == null)
	    		 {
	    			 throw new ImageException("File:"+file.getName()+" , is not an Image");
	    		 }
	    		 imgInfo = new ImageInfo();
	    		 imgInfo.setWidth(image.getWidth());
	    		 imgInfo.setHeight(image.getHeight());
	    		 
	    		 String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());
	    		 String fileExtn = FilenameUtils.getExtension(file.getOriginalFilename());
	    		 imgInfo.setName(fileName);
	    		 imgInfo.setExtn(fileExtn);
	    		 imgInfo.setSize(file.getSize());
	    		 
	    	}
	    	catch(ImageException | IOException e)
	    	{
	    		throw new ImageException(e.getMessage(),e);
	    	}
	    	
	    	
	    	return imgInfo;
	    }
	    
	    
	    public ImageInfo getImageInfo(String name, InputStream stream)
	    {
	    	ImageInfo imgInfo = null;
	    	
	    	try {
	    		
	    		if( null == stream)
		    	{
		    		throw new ImageException("Invalid File Type");
		    	}
		    	
	    		 BufferedImage image=ImageIO.read(stream);
	    		 if(image == null)
	    		 {
	    			 throw new ImageException("File:"+name+" , is not an Image");
	    		 }
	    		 imgInfo = new ImageInfo();
	    		 imgInfo.setWidth(image.getWidth());
	    		 imgInfo.setHeight(image.getHeight());
	    		 
	    		 String fileName = FilenameUtils.getBaseName(name);
	    		 String fileExtn = FilenameUtils.getExtension(name);
	    		 imgInfo.setName(fileName);
	    		 imgInfo.setExtn(fileExtn);
	    		 
	    		 
	    	}
	    	catch(ImageException | IOException e)
	    	{
	    		//throw new ImageException(e.getMessage(),e);
	    	}
	    	
	    	
	    	return imgInfo;
	    }
	    
	    @Validated
	    public Person convertPersonToObject(String personTxt) throws PeopleException
	    {
	    	Person people = null;
	    	try 
	    	{
	    		ObjectMapper objectMapper = new ObjectMapper();
		    	people = objectMapper.readValue(personTxt, Person.class);
		    	
		    	if( null == people.getName())
		    	{
		    		throw new PeopleException("Person Name can not be null");
		    	}
		    	
		    	people.setId(getSequanceNo(SequenceType.PERSON));
		    	
			} catch (JsonProcessingException | SequencerException e) {
				throw new PeopleException("Unable to Convert Json to People Object Reason:"+e.getMessage());
			}
	    	return people;
	    }

	    
	    public PersonImport convertPersonImportToObject(String personTxt) throws ImporterException
	    {
	    	PersonImport person = null;
	    	try 
	    	{
	    		ObjectMapper objectMapper = new ObjectMapper();
		    	person = objectMapper.readValue(personTxt, PersonImport.class);
		    	
			} catch (JsonProcessingException  e) {
				throw new ImporterException("Unable to Convert Json to PersonImport Object Reason:"+e.getMessage());
			}
	    	return person;
	    }
	    
	    public CompanyImport convertCompanyImportToObject(String compTxt) throws ImporterException
	    {
	    	CompanyImport comp = null;
	    	try 
	    	{
	    		ObjectMapper objectMapper = new ObjectMapper();
		    	comp = objectMapper.readValue(compTxt, CompanyImport.class);
		    	
			} catch (JsonProcessingException  e) {
				throw new ImporterException("Unable to Convert Json to CompanyImport Object Reason:"+e.getMessage());
			}
	    	return comp;
	    }
	    
	    

	    public MovieImport convertMovieImportToObject(String movTxt) throws ImporterException
	    {
	    	MovieImport comp = null;
	    	try 
	    	{
	    		ObjectMapper objectMapper = new ObjectMapper();
		    	comp = objectMapper.readValue(movTxt, MovieImport.class);
		    	
			} catch (JsonProcessingException  e) {
				throw new ImporterException("Unable to Convert Json to MovieImport Object Reason:"+e.getMessage());
			}
	    	return comp;
	    }
	    
	    public MovieCollectionImport convertMovieCollectionImportToObject(String movTxt) throws ImporterException
	    {
	    	MovieCollectionImport comp = null;
	    	try 
	    	{
	    		ObjectMapper objectMapper = new ObjectMapper();
		    	comp = objectMapper.readValue(movTxt, MovieCollectionImport.class);
		    	
			} catch (JsonProcessingException  e) {
				throw new ImporterException("Unable to Convert Json to MovieCollectionImport Object Reason:"+e.getMessage());
			}
	    	return comp;
	    }
	    
	    public Movie convertTmdbMovie_to_Movie(TmdbMovie tmov, Date processDate) throws SequencerException
	    {
	    	Movie mov = new Movie();
	    	
	    	//mov.setId(getSequanceNo(SequenceType.MEDIA));
	    	mov.setImdbId(tmov.getId());
	    	mov.setTmdbId(tmov.getTmdbId());
	    	mov.setAdult(tmov.isAdult());
	    	mov.setName(tmov.getTitle());
	    	mov.setTitle(tmov.getTitle());
	    	if( null != tmov.getTagline())
	    	{
	    		mov.setTagLine(tmov.getTagline());
		    		
	    	}
	    	
	    	if( null != tmov.getHomepage())
	    	{
	    		mov.setHomePage(tmov.getHomepage());
	    	}
	    	
	    	if( null != tmov.getOriginal_title())
	    	{
	    		mov.setOriginalTitle(tmov.getOriginal_title());
	    	}
	    	
	    	if( null != tmov.getImdbId())
	    	{
	    		mov.setImdbId(tmov.getImdbId());
	    	}
	    	
	    	if ( null != tmov.getOrgMediaLanguage())
	    	{
	    		mov.setOriginalLanguage(tmov.getOrgMediaLanguage());
	    	}
	    	
	    	if (( null != tmov.getSpoken_languages()) && ( !tmov.getSpoken_languages().isEmpty()))
	    	{	
	    		mov.getSpokenLanguageList().addAll(tmov.getSpoken_languages());
	    	}
	    	
	    	if ( null != tmov.getBackdrop_pathId())
	    	{
	    		mov.setBackdropImageId(tmov.getBackdrop_pathId());
	    	}
	    	
	    	if ( null != tmov.getPoster_pathId())
	    	{
	    		mov.setPostarImageId(tmov.getPoster_pathId());
	    	}
	    	
	    	if ( null != tmov.getBelongs_to_collection())
	    	{
	    		mov.setBelongstocollection(tmov.getBelongs_to_collection());
	    		mov.getBelongstocollection().setTmdbId(mov.getBelongstocollection().getId());
	    		mov.getBelongstocollection().setId(null);
	    	}
	    	
	    	mov.setRuntime(tmov.getRuntime());
	    	mov.setRevenue(tmov.getRevenue());
	    	mov.setBudget(tmov.getBudget());
	    	mov.setOverview(tmov.getOverview());
	    	mov.setPopularity(tmov.getPopularity());
	    	
	    	if(null != tmov.getRelease_date())
	    	{
	    		mov.setRelease_date(tmov.getRelease_date());
	    	}
	    	
	    	if(null !=  tmov.getStatus())
	    	{
	    		mov.setStatus(convertString_to_Status(tmov.getStatus()));
	    	}
	    	
	    	mov.setVideoPresent(false);
	    	mov.setCreatedBy("SYSTEM");
	    	mov.setCreatedOn(processDate);
	    	
	    	return mov;
	    }
	    
	    
	    /**
	     * Convert TmdbMovie Collection to MovieCollection 
	     * along wiht Modify Movies belongs to Collection with ID of MovieCollection
	     * @param tmovcol
	     * @param colMovieList
	     * @param processDate
	     * @return
	     * @throws SequencerException
	     */
	    public MovieCollection convertTmdbMovieCollection_to_MovieCollection(TmdbCollection tmovcol, List<Movie> colMovieList, Date processDate) throws SequencerException
	    {
	    	MovieCollection mcol = null;
	    	if( null != tmovcol)
	    	{
	    		mcol = new MovieCollection();
	    	//	mcol.setId(getSequanceNo(SequenceType.COLLECTION));
	    		    		mcol.setId(tmovcol.getId());
	    		if ( null != tmovcol.getBackdrop_pathId())
	    		{
	    			mcol.setBackdropImageId(tmovcol.getBackdrop_pathId());
	    		}
	    		
	    		if ( null != tmovcol.getPoster_pathId())
	    		{
	    			mcol.setPostarImageId(tmovcol.getPoster_pathId());
	    			
	    		}
	    		
	    		mcol.setName(tmovcol.getName());
	    		
	    		if ( null != tmovcol.getOverview())
	    		{
	    			mcol.setOverview(tmovcol.getOverview());
	    		}
	    		
	    		mcol.setPopularity(tmovcol.getPopularity());
	    		
	    		mcol.setTmdbId(tmovcol.getTmdbId());
	    		
	    		mcol.setType(CollectionType.Movie);
	    		
	    		mcol.setCreatedBy("SYSTEM");
	    		mcol.setCreatedOn(processDate);
	    		
	    		// Populate Collection Details to Respective Movies
	    		
	    		addCollectionDetails_to_Movies(mcol, colMovieList);
	    		
	    		mcol.getMovieList().addAll(colMovieList);
	    	}
	    	
	    	
	    	return mcol;
	    	
	    }
	    
	    private void addCollectionDetails_to_Movies(MovieCollection mcol, List<Movie> colMovieList)
	    {
	    	if(!colMovieList.isEmpty())
	    	{
	    		colMovieList.stream().forEach(cm -> {
					try {
						
						
						BelongsToCollection bc = new BelongsToCollection();
						if( null != mcol.getBackdropImageId())
						{
							bc.setBackdropImageId(mcol.getBackdropImageId());
						}
						
						if ( null != mcol.getPostarImageId())
						{
							bc.setPostarImageId(mcol.getPostarImageId());
						}
						
						bc.setId(mcol.getId());
						bc.setTmdbId(mcol.getTmdbId());
							
						bc.setName(mcol.getName());
						cm.setBelongstocollection(bc);
						cm.setCollectionId(mcol.getId());
						
					} catch (Exception e) {
						// Swallowing Parallel Stream exception
						logger.error("Import Collection Error for :"+mcol.getTmdbId()+", ERROR:"+e.getMessage());
					}
				});	
	    	}
	    }
	   
	    
	    private StatusType convertString_to_Status(String sts)
	    {
	    	StatusType result = StatusType.UNKNOWN;
	    	
	    	
	    	for (StatusType fsts : StatusType.values())
	    	{
	    		if(fsts.getValue().toUpperCase().equalsIgnoreCase(sts.toUpperCase()))
	    		{
	    			result = fsts;
	    			break;
	    		}
	    	}
	    	
	    	return result;
	    	
	    	
	    }
	    
	    
	    public Person convertTmdbPerson_to_Person(TmdbPerson tper) throws SequencerException
	    {
	    	Person per = new Person();
	    	per.setId(getSequanceNo(SequenceType.PERSON));
	    	per.setBiography(tper.getBiography());
	    	per.setBirthdate(tper.getBirthday());
	    	per.setDeathdate(tper.getDeathday());
	    	
	    	
	    	per.setGender_type(getGenderFromInt(tper.getGender()));
	    	per.setHomepage(tper.getHomepage());
	    	per.setName(tper.getName());
	    	per.setOriginalname(null);
	    	per.setPlace_of_birth(tper.getPlace_of_birth());
	    	per.getAlso_known_as_list().addAll(tper.getAlso_known_as());
	    	if( null != tper.getKnown_for_department())
	    	{
	    		DepartmentType dep = getDepartmentType(tper.getKnown_for_department());
		    	per.getKnown_department_list().add(dep);
		    		
	    	}
	    	per.setImdb_id(tper.getImdb_id());
	    	per.setTmdbId(String.valueOf(tper.getId()));
	    	return per;
	    	
	    }

	    
	    @Async
		public Language convertLanguageImporttoLanguage(LanguageImport ilan) throws SequencerException
	    {
	    	Language lan = null;
	    	
	    	if( null != ilan)
	    	{
	    		lan = new Language();
	    		lan.setId(getSequanceNo(SequenceType.LANGUAGE));
	    		lan.setFamily(ilan.getFamily());
	    		lan.setIsoCode1(ilan.getIso639_1());
	    		lan.setIsoCode2(ilan.getIso639_2());
	    		lan.setName(ilan.getName());
	    		lan.setNativeName(ilan.getNativeName());
	    		lan.setWikiUrl(ilan.getWikiUrl());
	    	    
	    	}
	    	
	    	return lan;
	    }
	    
	    @Async
		public Country convertCountryImporttoCountry(CountryImport icon) throws SequencerException
	    {
	    	Country con = null;
	    	
	    	if( null != icon)
	    	{
	    		con = new Country();
	    		con.setId(getSequanceNo(SequenceType.COUNTRY));
	    		con.setIsoCode1(icon.getAlpha2());
	    		con.setIsoCode2(icon.getAlpha3());
	    		con.setName(icon.getName());
	    	}
	    	
	    	return con;
	    }
	    
	    @Async
		public Translation convertMovieTranslationImporttoTranslation(MovieTranslationImport mti) 
	    {
	    	Translation tran = null;
	    	
	    	try {
	    		if( null != mti)
		    	{
		    		tran = new Translation();
		    		tran.setTmdbId(mti.getId());
		    		tran.setType(EntityType.Movie);
		    		if((null != mti.getTranslationList()) && (!mti.getTranslationList().isEmpty()))
		    		{
		    			
		    			populateTranslationData(tran, mti.getTranslationList());
		    		}
		    			    		
		    	}	
	    	}
	    	catch (Exception e)
	    	{
	    		
	    	}
	    	
	    	
	    	
	    	return tran;
	    }
	    
	    
	    @Async
		public MovieCredit converttmdbMovieCredit_to_MovieCredit(TmdbMovieCredit tmov,Movie mov,  Map<String,Person> personMap, Date processDate) 
	    {
	    	MovieCredit mc = null;
	    	
	    	try {
	    		if( null != tmov)
		    	{
	    			mc = new MovieCredit();
	    			mc.setId(mov.getId());
	    			mc.setName(mov.getName());
	    			mc.setTmdbId(mov.getTmdbId());
	    			
	    			populateMovieCreditCastDetails(tmov, mc,personMap);
	    			populateMovieCreditCrewDetails(tmov, mc,personMap);
	    			
	    			mc.setCreatedBy("SYSTEM");
	    			mc.setCreatedOn(processDate);
	    			mc.setModifiedBy(null);
	    			mc.setModifiedOn(null);
		    	}	
	    	}
	    	catch (Exception e)
	    	{
	    		
	    	}
	    	
	    	
	    	
	    	return mc;
	    }

	    @Async 	
	    private void populateMovieCreditCastDetails(TmdbMovieCredit tmov, MovieCredit mc,Map<String,Person> personMap)
	    {
	    	if( !tmov.getCastList().isEmpty())
	    	{
	    		tmov.getCastList().stream().forEach(tc -> {
					try {
						
							Cast cast = convertTmdbCast_to_Cast(tc, mc, personMap);
							if ( null != cast)
							{
								mc.getCastList().add(cast);
							}	
						
						
						
						
					} catch (Exception e) {
						// Swallowing Parallel Stream exception
						logger.error("Populate Movie Cast Error for :"+tc.getId()+", ERROR:"+e.getMessage());
					}
				});	
	    	}
	    	
	    }
	    
	    private Cast convertTmdbCast_to_Cast(TmdbCast tmc, MovieCredit mc, Map<String,Person> personMap) throws SequencerException
	    {
	    	
	    	Cast cast = null;
	    	
	    	
	    	if( null != tmc)
	    	{
	    		String personID = "UNKNOWN";
		    	String peronImgId = null;
		    	if(personMap.containsKey(tmc.getId()))
		    	{
		    		Person per = personMap.get(tmc.getId());
		    		
		    		personID = per.getId();
		    		peronImgId = per.getImage_id();
		    	}
		    
	    		cast = new Cast();
	    		cast.setId(getSequanceNo(SequenceType.CAST_CREW));
	    		cast.setCharacter(tmc.getCharacter());
	    		cast.setCreditId(cast.getId());
	    		cast.setGenderType(getGenderFromInt(tmc.getGender()));
	    		cast.setKnownForDepartment(getDepartmentType(tmc.getKnown_for_department()));
	    		cast.setName(tmc.getName());
	    		cast.setOrder(tmc.getOrder());
	    		cast.setPersonId(personID);
	    		cast.setProfileImageId(peronImgId);
	    		cast.setTmdbcreditid(tmc.getCredit_id());
	    		cast.setTmdbId(String.valueOf(tmc.getId()));
	    		cast.setType(PeopleType.Cast);
	    		
	    	}
	    	
	    	return cast;
	    }
	    
	    
	    @Async 	
	    private void populateMovieCreditCrewDetails(TmdbMovieCredit tmov, MovieCredit mc,Map<String,Person> personMap)
	    {
	    	if( !tmov.getCrewList().isEmpty())
	    	{
	    		tmov.getCrewList().stream().forEach(tc -> {
					try {
						
					
							Crew crew = convertTmdbCrew_to_Crew(tc, mc, personMap);
							if ( null != crew)
							{
								mc.getCrewList().add(crew);
							}	
						
						
						
						
					} catch (Exception e) {
						// Swallowing Parallel Stream exception
						logger.error("Populate Movie Cast Error for :"+tc.getId()+", ERROR:"+e.getMessage());
					}
				});	
	    	}
	    	
	    }
	    
	    private Crew convertTmdbCrew_to_Crew(TmdbCrew tmc, MovieCredit mc, Map<String,Person> personMap) throws SequencerException
	    {
	    	
	    	Crew crew = null;
	    	
	    	if( null != tmc)
	    	{
	    		
	    		String personID = "UNKNOWN";
		    	String peronImgId = null;
		    	if(personMap.containsKey(tmc.getId()))
		    	{
		    		Person per = personMap.get(tmc.getId());
		    		
		    		personID = per.getId();
		    		peronImgId = per.getImage_id();
		    	}
		    	

	    		
	    		crew = new Crew();
	    		crew.setId(getSequanceNo(SequenceType.CAST_CREW));
	    		crew.setCharacter(tmc.getCharacter());
	    		crew.setCreditId(crew.getId());
	    		crew.setGenderType(getGenderFromInt(tmc.getGender()));
	    		crew.setKnownForDepartment(getDepartmentType(tmc.getKnown_for_department()));
	    		crew.setName(tmc.getName());
	    		crew.setOrder(tmc.getOrder());
	    		crew.setPersonId(personID);
	    		crew.setProfileImageId(peronImgId);
	    		crew.setTmdbcreditid(tmc.getCredit_id());
	    		crew.setTmdbId(String.valueOf(tmc.getId()));
	    		crew.setType(PeopleType.Crew);
	    		crew.setDepartment(getDepartmentType(tmc.getDepartment()));
	    		crew.setJob(tmc.getJob());
	    		
	    		
	    	}
	    	
	    	return crew;
	    }

	    @Deprecated
	    public void prpereYouTubeMedia(YouTubeMedia ym, Date modDate) throws SequencerException
	    {
	    	
	    	if( null != ym)
	    	{
	    		
	   	    ym.setId(getSequanceNo(SequenceType.YOUTUBE_MEDIA));
	   	    ym.setCreatedOn(modDate);
	   	  	
	    	}
	    	
	    
	    }
	    
	    public void prepareYouTubeMediaRequest(YouTubeImportRequest yimpReq, Date modDate) throws ImporterException
	    {
	    	try {
	    		if( null == yimpReq)
		    	{
		    		throw new ImporterException(" YouTube Import Request can not be null");
		    	}
		    	yimpReq.setId(getSequanceNo(SequenceType.IMPORT_REQUEST));
		    	yimpReq.setImportStatus(ImportStatusType.Open);
		    	yimpReq.setCreatedOn(modDate);
	    	}
	    	catch(Exception e)
	    	{
	    		throw new ImporterException(e.getMessage(), e);
	    	}
	    	
	    
	    }
	    

	    
	    /**
	     * Prepare TranslationData from TranslationImport and store into Translatoin Object
	     * @param tran
	     * @param translationList
	     */
	    @Async 	
	    private void populateTranslationData(Translation tran, List<TranslationImport> translationList)
	    {
	    	if( !translationList.isEmpty())
	    	{
	    		translationList.stream().forEach(ti -> {
					try {
						
						TranslationData td = convertTranslationImportToTranslationData(ti);
						if ( null != td)
						{
							tran.getDataList().add(td);
						}
						
					} catch (Exception e) {
						// Swallowing Parallel Stream exception
						logger.error("Populate Translation Error for :"+ti.getLanguageIsoCode1()+", ERROR:"+e.getMessage());
					}
				});	
	    	}
	    	
	    }
	    
	    
	    public List<PersonCredit> convertMovieCredit_to_PersonCredit(MovieCredit mc, Movie movie)
	    {
	    	List<PersonCredit> pcList = new ArrayList<PersonCredit>();
	    	
	    	if ( null != mc) 
	    	{
	    		
	    		if( !mc.getCastList().isEmpty())
		    	{
		    		mc.getCastList().stream().forEach(cast -> {
						try {
							
						
							PersonCredit pc = convertCast_to_PersonCredit(cast, mc, movie);
								if ( null != pc)
								{
								pcList.add(pc);
								}	
							
							
							
							
						} catch (Exception e) {
							// Swallowing Parallel Stream exception
							logger.error("Convert Cast to PersonCast Error for :"+cast.getId()+", ERROR:"+e.getMessage());
						}
					});	
		    	}
	    		
	    		if( !mc.getCrewList().isEmpty())
		    	{
		    		mc.getCrewList().stream().forEach(crew -> {
						try {
							
						
							PersonCredit pc = convertCrew_to_PersonCredit(crew, mc, movie);
								if ( null != pc)
								{
								pcList.add(pc);
								}	
							
							
							
							
						} catch (Exception e) {
							// Swallowing Parallel Stream exception
							logger.error("Convert Crew to PersonCast Error for :"+crew.getId()+", ERROR:"+e.getMessage());
						}
					});	
		    	}
	    		
	    	}
	    	
	    	return pcList;
	    }
	    
	    private PersonCredit convertCast_to_PersonCredit(Cast cast, MovieCredit mc, Movie movie)
	    { 
	    	PersonCredit pc = new PersonCredit();
	    	pc.setCreatedBy(mc.getCreatedBy());
	    	pc.setCreatedOn(mc.getCreatedOn());
	    	pc.setDepartment(cast.getKnownForDepartment());
	    	pc.setId(cast.getId());
	    	pc.setJob(null);
	    	pc.setMediaCreditId(mc.getId());
	    	pc.setMediaCharacter(cast.getCharacter());
	    	
	    	pc.setMediaType(CollectionType.Movie);
	    	pc.setModifiedBy(mc.getModifiedBy());
	    	pc.setModifiedOn(mc.getModifiedOn());
	    	pc.setPersonId(cast.getPersonId());
	    	pc.setPersonName(cast.getName());
	   
	    	pc.setMediaId(movie.getId());
	    	pc.setMediaName(movie.getName());
	    	pc.setType(cast.getType());

	    	if (null != movie.getTagLine())
	    	{
	    		pc.setMediaTagLine(movie.getTagLine());
	    	    
	    	}
	    	
	    	if ( null != movie.getTitle())
	    	{
	    		pc.setMediaTitle(movie.getTitle());
		    		
	    	}
	    	if(null != movie.getBackdropImageId())
	    	{
	    		pc.setMediaBackdropId(movie.getBackdropImageId());
	    	    
	    	}
	    	if ( null != movie.getPostarImageId())
	    	{
	    		pc.setMediaPostarId(movie.getPostarImageId());
	    	    
	    	}
	   
	    	if ( null != movie.getRelease_date())
	    	{
	    		pc.setRelease_date(movie.getRelease_date());
	    	    
	    	}
	    	
	    	return pc;

	    }
	    

	    public Media convertYouTubeMediaToMedia(YouTubeMedia ytm) throws MediaException
	    {
	    	Media md = null;
	    	
	    	try {
	    		md = new Media();
	    		if((null == ytm.getMediaId()) || (ytm.getMediaId().isEmpty()))
	    		{
	    			md.setId(getSequanceNo(SequenceType.MEDIA));
	    		    
	    		}
	    		else {
	    			md.setId(ytm.getMediaId());
	    		}
	    		md.setMediaType(ytm.getYtmInfo().getMediaType());
		    	md.setAdult(false);
		    	md.setActive(false);
		    	md.setCreatedBy(ytm.getCreatedBy());
		    	md.setCreatedOn(new Date());
		    	md.setModifiedBy(null);
		    	md.setModifiedOn(null);
		    	md.setName(ytm.getYtmInfo().getTitle());
		    	md.setOrder(ytm.getOrder());
		    	md.setOriginalLanguage(getMediaLanguagebyisoCode1(ytm.getIso1LanguageCode()));
		    	md.setOriginalTitle(ytm.getYtmInfo().getFullTitle());
		    	md.setOverview(ytm.getYtmInfo().getOverview());
		    	md.setRelease_date(ytm.getYtmInfo().getReleaseDate());
		    	md.setRuntime(ytm.getYtmInfo().getDuration());
		    	md.setStatus(StatusType.Released);
		    	md.setTagLine(null);
		    	md.setTitle(ytm.getYtmInfo().getTitle());
		    	
		    	// Add Media Images
		    	md.getImageList().addAll(ytm.getImageList());
		    	
		    	// Default YouTube Genre
		    	//Genre genre = getGenrebyName("Music");
		    	md.getGenerList().add(MediaGenreType.MUSIC);
		    	
		    	// Populate YouTube ExternalSystem Identify
		    	md.setSourceSystemType(ExternalSystemType.YOUTUBE);
		    	
		    	if( null != ytm.getYtmInfo().getYouTubeId())
		    	{
		    		md.getExternalSystemIdentifierMap().put("ID", ytm.getYtmInfo().getYouTubeId());
			    		
		    	}
		    	if(null != ytm.getYtmInfo().getDisplayId())
		    	{
		    		md.getExternalSystemIdentifierMap().put("DISPLAY_ID", ytm.getYtmInfo().getDisplayId());
			    		
		    	}
		    	if(null != ytm.getYtmInfo().getChannelId())
		    	{
		    		md.getExternalSystemIdentifierMap().put("CHANNEL_ID", ytm.getYtmInfo().getChannelId());
			    		
		    	}
		    	if(null != ytm.getYtmInfo().getChannelUrl())
		    	{
		    		md.getExternalSystemIdentifierMap().put("CHANNEL_URL", ytm.getYtmInfo().getChannelUrl());
			    		
		    	}
		    	
		    	if(null != ytm.getYtmInfo().getUploaderId())
		    	{
		    		md.getExternalSystemIdentifierMap().put("UPLOADER_ID", ytm.getYtmInfo().getUploaderId());
			    		
		    	}
		    	
		    	if(null != ytm.getYtmInfo().getWebPageUrl())
		    	{
		    		md.getExternalSystemIdentifierMap().put("WEBPAGE_URL", ytm.getYtmInfo().getWebPageUrl());
			    		
		    	}
		    	
		    	
		    	
	    	}
	    	catch(Exception e)
	    	{
	    		logger.error(e.getMessage(), e);
	    		throw new MediaException(e.getMessage(), e);
	    		
	    	}
	    	
	    	
	    	return md;
	    }
	    
	    
	    public Media convertImportMediaToMedia(ImportMediaRequest imr) throws MediaException
	    {
	    	Media md = null;
	    	
	    	
	    	try {
	    		/*
	    		md = new Media();
	    			md.setId(getSequanceNo(SequenceType.MEDIA));
	    		md.setMediaType(imr.getMediaType());
		    	md.setAdult(false);
		    	md.setCreatedBy(imr.getCreatedBy());
		    	md.setCreatedOn(imr.getCreatedOn());
		    	md.setModifiedBy(null);
		    	md.setModifiedOn(null);
		    	md.setName(imr.getName());
		    	md.setTitle(imr.getTitle());
		    	
		    	md.setOrder(1);
		    	md.setRuntime(ytm.getYtmInfo().getDuration());
		    	md.setStatus(StatusType.Released);
		    	md.setTagLine(null);
		    	
		    	// Add Media Images
		    	md.getImageList().addAll(ytm.getImageList());
		    	
		    	// Default YouTube Genre
		    	//Genre genre = getGenrebyName("Music");
		    	md.getGenerList().add(MediaGenreType.MUSIC);
		    	
		    	// Populate YouTube ExternalSystem Identify
		    	md.setSourceSystemType(ExternalSystemType.YOUTUBE);
		    	
		    	if( null != ytm.getYtmInfo().getYouTubeId())
		    	{
		    		md.getExternalSystemIdentifierMap().put("ID", ytm.getYtmInfo().getYouTubeId());
			    		
		    	}
		    	if(null != ytm.getYtmInfo().getDisplayId())
		    	{
		    		md.getExternalSystemIdentifierMap().put("DISPLAY_ID", ytm.getYtmInfo().getDisplayId());
			    		
		    	}
		    	if(null != ytm.getYtmInfo().getChannelId())
		    	{
		    		md.getExternalSystemIdentifierMap().put("CHANNEL_ID", ytm.getYtmInfo().getChannelId());
			    		
		    	}
		    	if(null != ytm.getYtmInfo().getChannelUrl())
		    	{
		    		md.getExternalSystemIdentifierMap().put("CHANNEL_URL", ytm.getYtmInfo().getChannelUrl());
			    		
		    	}
		    	
		    	if(null != ytm.getYtmInfo().getUploaderId())
		    	{
		    		md.getExternalSystemIdentifierMap().put("UPLOADER_ID", ytm.getYtmInfo().getUploaderId());
			    		
		    	}
		    	
		    	if(null != ytm.getYtmInfo().getWebPageUrl())
		    	{
		    		md.getExternalSystemIdentifierMap().put("WEBPAGE_URL", ytm.getYtmInfo().getWebPageUrl());
			    		
		    	}
		    	
		    	*/
		    	
	    	}
	    	catch(Exception e)
	    	{
	    		logger.error(e.getMessage(), e);
	    		throw new MediaException(e.getMessage(), e);
	    		
	    	}
	    	
	    	
	    	return md;
	    }
	    
	   
	    
	    private PersonCredit convertCrew_to_PersonCredit(Crew crew, MovieCredit mc, Movie movie)
	    { 
	    	PersonCredit pc = new PersonCredit();
	    	pc.setCreatedBy(mc.getCreatedBy());
	    	pc.setCreatedOn(mc.getCreatedOn());
	    	pc.setDepartment(crew.getDepartment());
	    	pc.setId(crew.getId());
	    	pc.setJob(crew.getJob());
	    	pc.setMediaCreditId(mc.getId());
	    	pc.setMediaType(CollectionType.Movie);
	    	pc.setModifiedBy(mc.getModifiedBy());
	    	pc.setModifiedOn(mc.getModifiedOn());
	    	pc.setPersonId(crew.getPersonId());
	    	pc.setPersonName(crew.getName());
	    	pc.setType(crew.getType());
	    	
	    	if (null != movie.getTagLine())
	    	{
	    		pc.setMediaTagLine(movie.getTagLine());
	    	    
	    	}
	    	
	    	if ( null != movie.getTitle())
	    	{
	    		pc.setMediaTitle(movie.getTitle());
		    		
	    	}
	    	if(null != movie.getBackdropImageId())
	    	{
	    		pc.setMediaBackdropId(movie.getBackdropImageId());
	    	    
	    	}
	    	if ( null != movie.getPostarImageId())
	    	{
	    		pc.setMediaPostarId(movie.getPostarImageId());
	    	    
	    	}
	   
	    	if ( null != movie.getRelease_date())
	    	{
	    		pc.setRelease_date(movie.getRelease_date());
	    	    
	    	}

	    	
	    	return pc;

	    }
	    
	    private TranslationData convertTranslationImportToTranslationData(TranslationImport ti) throws CountryException, ImporterException, LanguageException
	    {
	    	TranslationData td = null;
	    	if( null != ti)
	    	{
	    		td = new TranslationData();
	    		
	    		// Populate Country Details
	    		if( null != ti.getCountryIsoCode1())
	    		{
	    			Country country = getCountrybyisoCode1(ti.getCountryIsoCode1());
	    			if(null == country)
	    			{
	    				throw new ImporterException("Requested Country :"+ti.getCountryIsoCode1()+" Not available in System");
	    			}
	    			td.setCountryCd(country.getId());
	    			td.setCountryIsoCode1(country.getIsoCode1());
	    			td.setCountryIsoCode2(country.getIsoCode2());
	    			td.setCountryName(country.getName());
	    			
	    			
	    		}
	    		
	    		// Populate Language Details
	    		if( null != ti.getLanguageIsoCode1())
	    		{
	    			Language lang = getLanguagebyisoCode1(ti.getLanguageIsoCode1());
	    			if( null == lang)
	    			{
	    				throw new ImporterException("Requested Language :"+ti.getCountryIsoCode1()+" Not available in System");
	    			}
	    			
	    			td.setLanguageCd(lang.getId());
	    			td.setLanguageName(lang.getName());
	    			td.setLanguageNativeName(lang.getNativeName());
	    			
	    		}
	    		
	    		if( null != ti.getData())
	    		{
	    			if ( null != ti.getData().getHomePage())
	    			{
	    				td.setHomePage(ti.getData().getHomePage());
	    			}
	    			
	    			if ( null != ti.getData().getOverview())
	    			{
	    				td.setOverview(ti.getData().getOverview());
	    			}
	    			
	    			td.setRuntime(ti.getData().getRuntime());
	    			
	    			if ( null != ti.getData().getTagline())
	    			{
	    				td.setTagline(ti.getData().getTagline());
	    			}
	    			
	    			if ( null != ti.getData().getTitle())
	    			{
	    				td.setTitle(ti.getData().getTitle());
	    			}
	    			
	    		}
	    		
	    	}
	    	
	    	return td;
	    }
	    /**
	     * Convert int Gender Value to Enum
	     * @param value
	     * @return
	     */
	    private GenderType getGenderFromInt(int value)
	    {
	    	GenderType gen = null;
	    	
	    	if((value == 0) || (value > 3))
	    	{
	    		gen = GenderType.Unknown;
	    	}
	    	else if (value == 1)
	    	{
	    		gen = GenderType.Female; 
	    	}
	    	else if (value == 2)
	    	{
	    		gen = GenderType.Male;
	    	}
	    	else if (value == 3)
	    	{
	    		gen = GenderType.Other;
	    	}
	    	
	    	return gen;
	    	
	    }
	    
	    /**
	     * Convert String to Department Enum
	     * @param value
	     * @return
	     */
	    private DepartmentType getDepartmentType(String value)
	    {
	    	DepartmentType dep = null;
	    	
	    	switch(value.toUpperCase())
	    	{
	    	case  "COSTUME & MAKE-UP" :
	    	dep = DepartmentType.Costume_MakeUp;
	    	break;
	    	
	    	case  "VISUAL EFFECTS" :
	        dep = DepartmentType.Visual_Effects;
		    break;
		    
	    	case  "ACTING" :
		    dep = DepartmentType.Acting;
			break;
			
	    	case  "ACTORS" :
			dep = DepartmentType.Acting;
			break;
		    
	    	case  "CREW" :
			dep = DepartmentType.Crew;
			break;
			
	    	case  "EDITING":
			dep = DepartmentType.Editing;
			break;
		
	    	case  "DIRECTING":
			dep = DepartmentType.Directing;
			break;
			
	    	case  "PRODUCTION":
			dep = DepartmentType.Production;
			break;
			
	    	case  "CAMERA":
			dep = DepartmentType.Camera;
			break;
			
	    	case  "LIGHTING":
			dep = DepartmentType.Lighting;
			break;
		
	    	case  "ART":
			dep = DepartmentType.Art;
			break;
			
	    	case  "WRITING":
			dep = DepartmentType.Writing;
			break;
			
	    	case  "SOUND":
			dep = DepartmentType.Sound;
			break;
			
	    	default :
	    	dep = DepartmentType.Unknown;
	    	break;
	    	
	    	}
	    	
	    	
	    	return dep;
	    	
	    }
	    public  Image createImageObject(ImageInfo imginfo, EntityType entityType, ImageType type, String entityId, String imageId ) throws ImageException 
	    {
	    	// Image Binary Data will be populated in Service Layer for Optimized Memory Utilization
	    	Image img = null;
	    	try {
	    		img = new Image(entityType, type, entityId);
	    		if( null != imageId)
	    		{
	    			img.setId(imageId);
	    		}
	    		else {
	    			img.setId(getSequanceNo(SequenceType.IMAGE)+"."+imginfo.getExtn().toLowerCase());
	    		    		
	    		}
	    		img.setHeight(imginfo.getHeight());
		    	img.setWidth(imginfo.getWidth());
		    		
	    	}
	    	catch(SequencerException e)
	    	{
	    		throw new ImageException(e.getMessage(),e);
	    	}
	    	
	    	
	    	return img;
	    	
	    }
	    
	    public List<Genre> convertGenreImport_to_list(GenreImport gi) throws ImporterException
	    {
	    	List<Genre> genrelist = null;
	    	try {
	    		if((null == gi.getGenre_list()) || (gi.getGenre_list().isEmpty()))
		    	{
		    		throw new ImporterException("GenreImport list can not be null or empty");
		    	}
		    	genrelist = new ArrayList<Genre>();
		    	for (Genre gen : gi.getGenre_list())
		    	{
		    		gen.setTmdbid(gen.getId());
		    		gen.setId(getSequanceNo(SequenceType.GENRE));
		    		genrelist.add(gen);
		    	}
		    
	    	}
	    	catch(ImporterException |  SequencerException e)
	    	{
	    		throw new ImporterException(e.getMessage(), e);
	    	}
	    	
	    	return genrelist;
	    }
	    
	    
	    public boolean deleteDirectory(String dirPath)
	    {
	    	boolean result = false;
	    	
	    	try {
	    		File dir = new File(dirPath);
	 	    	FileUtils.forceDelete(dir);
				result = true;
			} catch (IOException e) {
				
			logger.error("Error deleting directory:"+dirPath.toString()+", Error:"+e.getMessage(), e);		
			}
	    	
	    	return result;
	    }
	    
	    
		public OsType getOperatingOs()
		{
			OsType osType = null;
			String osStr = System.getProperty("os.name").toLowerCase();
			
			if(osStr.indexOf("win") >= 0)
			{
				osType = OsType.Windows; 
			}
			else if (osStr.indexOf("nix") >= 0
	                || osStr.indexOf("nux") >= 0
	                || osStr.indexOf("aix") > 0)
			{
				osType = OsType.Unix; 
			}
			else if (osStr.indexOf("mac") >= 0)
			{
				osType = OsType.Mac;
			}
			else if (osStr.indexOf("sunos") >= 0)
			{
				osType = OsType.Solaris;
			}

			
			return osType;
			
		}
	    
	   
		public boolean isEmptyString(String string) {
		    return string == null || string.isEmpty();
		}
		
		public String encriptPassword(String plainTxt) throws UserException
		{
			String encriptedTxt = null;
			if( null == plainTxt)
			{
				throw new UserException("Empty Password not allowed");
				
			}
			encriptedTxt = 	passwordEncoder.encode(plainTxt);
			
			return encriptedTxt;
		}
		
		
		public FFmpegFormat getMediaMetaDataInfo(Path mediaPath) throws IOException
		{
			FFmpegFormat format = null;
			
			try {
				Path ffmpegPath = env.getFfmpegPath();
				String ffmpeg_dir_path = ffmpegPath.toString();
				
				FFprobe ffprobe = new FFprobe(ffmpeg_dir_path+File.separator+"ffprobe");
				
				FFmpegProbeResult probeResult = ffprobe.probe(mediaPath.toString());
				
				if ((null != probeResult) && ( null != probeResult.getFormat()))
				{
					format = probeResult.getFormat();	
				}
	
			}
			catch(Exception e )
			{
				logger.error("Error Getting media MetaData for :"+mediaPath.toString());
			}
						

		return format;
			
		}
		
		/**
		 * Generate Email Activation URL Link for Given Token 
		 * @param emailActivationToken
		 * @return
		 * @throws ValidationException
		 */
		public String getEmailConfirmationLink(String emailActivationToken, String uid) throws ValidationException
		{
			String result = null;
			
			try {
				if( null == emailActivationToken)
				{
					throw new ValidationException("Invalid Email Activation Token ");
				}
				
				
				String url = env.getEmailConfirmationUrl();
				StringBuilder sb = new StringBuilder();
				
				sb.append(url);
				sb.append("?");
				sb.append("code=");
				sb.append(emailActivationToken);
				sb.append("&");
				sb.append("uid=");
				sb.append(uid);
				
				result = sb.toString();
				
			}
			catch(Exception e)
			{
				logger.error(e.getMessage(), e);
				throw new ValidationException(e.getMessage(), e);
			}
			
			return result;
		}
		
		public String getEmailActivationEmailSubject()
		{
			return env.getActivationEmailSubject();
		}
		
		public String getActivationEmailFrom()
		{
			return env.getActivationEmail();
		}
		
		public String getAppLogoUrl()
		{
			return env.getAppLogoUrl();
		}
		
		public UUID getUUIDFromString(String value)
		{
			UUID result = null;
			try {
				result =  UUID.nameUUIDFromBytes(value.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
			
				result = UUID.randomUUID();
			}
			
			return result;
		}
		
		
		/**
		 * Get UUID by Appending current time so it will be change every time
		 * @param value
		 * @return
		 */
		public UUID getEncryptedUUIDFromString(String value)
		{
			UUID result = null;
			try {
				Date now = new Date();
				String encValue = value+"_"+now.getTime();
				result =  UUID.nameUUIDFromBytes(encValue.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
			
				result = UUID.randomUUID();
			}
			
			return result;
		}
		
		
		public void populateUserDetailsToUserVo(User user, UserVo userVo) throws ValidationException
		{
		
			try {
				if( null == user)
				{
					throw new ValidationException("User Object can not be null for conversion");
				}	
				userVo.setEmail(user.getEmail());
				userVo.setFirstName(user.getFirstName());
				userVo.setGender(GenderType.getType(user.getGender()));
				userVo.setId(user.getId());
				userVo.setLastName(user.getLastName());
				userVo.setUserName(user.getUserName());
				
			}
			catch(Exception e)
			{
				throw new ValidationException(e.getMessage(), e);
			}
			
			
		}
		
		
		public void parepareCreateChannelVo(CreateChannelVo requestVo, Date startT) throws ConversionException
		{
			// Step 1: Create Channel Entity
			prepareChannelEntity(requestVo, startT);
			
			// Step 2 : Create Default Admin Role Mapping for Channel
			prepareChannelUserMapping(requestVo);
			
		}
		
		private void prepareChannelEntity(CreateChannelVo requestVo, Date startT) throws ConversionException
		{
			Channel response= null;
			try {
				
				response = new Channel();
				String chnlID = getSequanceNo(SequenceType.Channel);
				response.setId(chnlID);
				response.setType(requestVo.getType());
				response.setVisibility(requestVo.getVisibility());
				response.setSubscriptionType(requestVo.getSubscriptionType());
				response.setName(requestVo.getName());	
				
				response.setDescription(requestVo.getDescription());
				
				if(requestVo.getVisibility().equals(ChannelVisibility.Private))
				{
					String encriptedChnlName = getEncryptedUUIDFromString(requestVo.getName()).toString();
					response.setEncriptedName(encriptedChnlName);
				}
				response.setActive(true);
				response.setCreatedBy(requestVo.getUserID());
				response.setCreatedOn(startT);
				response.setModifiedBy(requestVo.getUserID());
				response.setModifiedOn(startT);
				
				requestVo.setResponse(response);
				
			}
			catch(Exception e)
			{
				requestVo.setValid(false);
				throw new ConversionException(e.getMessage(), e);
			}
			
		}
		
		
		private void prepareChannelUserMapping(CreateChannelVo requestVo) throws ConversionException
		{
			try {
				
				Date endDate = getInfiniteDate();
				
				if(!requestVo.getChannelUsers().isEmpty())
				{
					for (ChannelParticipant cp : requestVo.getChannelUsers())
					{
						String cpID = getSequanceNo(SequenceType.ChannelParticipant);
						cp.setId(cpID);	
						cp.setChannelID(requestVo.getResponse().getId());
						cp.setChannelType(requestVo.getResponse().getType());
						
						if(requestVo.getType().equals(ChannelType.Messaging))
						{
							if(requestVo.getUserID().equals(cp.getUserID()))
							{
								cp.getUserRoles().add(UserRoleType.Admin);	
							}
							else {
								cp.getUserRoles().add(UserRoleType.Author);
							}	
						}
						else if ((requestVo.getType().equals(ChannelType.Blog))
								|| (requestVo.getType().equals(ChannelType.Audio))
								|| (requestVo.getType().equals(ChannelType.Video))
								)
						{
							if(requestVo.getUserID().equals(cp.getUserID()))
							{
								cp.getUserRoles().add(UserRoleType.Admin);	
							}
							else {
								cp.getUserRoles().add(UserRoleType.Viewer);
							}
						}
						cp.setValidFrom(requestVo.getResponse().getCreatedOn());
						cp.setValidTo(endDate);
						
						cp.setActive(true);
						cp.setCreatedBy(requestVo.getResponse().getCreatedBy());
						cp.setCreatedOn(requestVo.getResponse().getCreatedOn());
						cp.setModifiedBy(requestVo.getResponse().getModifiedBy());
						cp.setModifiedOn(requestVo.getResponse().getModifiedOn());
					
						
						
					}
				}
				
			}
			catch(Exception e)
			{
				requestVo.setValid(false);
				throw new ConversionException(e.getMessage(), e);
			}
			
		}
		
		public void prepreChannelParticipent()
		{
			
		}
	
		public Date getInfiniteDate()
		{
			Date result = null;
			try {
				String sDate1="31/12/9999";  
				result = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1); 
					
			}
			catch(Exception e)
			{
			// Swallow exception	
			}
			
			return result;
		}
		
		public ChannelParticipant getChnlParticipentRecord(String userID, List<ChannelParticipant> chnlPars)
		{
			ChannelParticipant result = null;
			
			if (( null != chnlPars) && (!chnlPars.isEmpty()))
			{
				 result = chnlPars.stream()
						  .filter(chp -> userID.equals(chp.getUserID()))
						  .findAny()
						  .orElse(null);
				
				
			}
			
			return result;
		}
		
		/**
		 * Prepare createMessage DB Entities
		 * @param req
		 * @param crDate
		 * @throws ConversionException
		 */
		
		public void prepareCreateMessage(MessageActionVo req, Date crDate) throws ConversionException
		{
			try {
				
				// Prepare Message Entity and populate to request
				prepareMessage(req, crDate);
				
				// Prepare ChannelMsgRelation
				prepareChnlMsgRelation(req, crDate);
				
				// Prepare MsgBoxRelation 
				//prepareMsgBoxRelation(req, crDate);
			}
			catch(Exception e)
			{
				req.setValid(false);
				throw new ConversionException(e.getMessage(), e);
			}
		}
		
		
		public void prepareMessage(MessageActionVo req, Date crDate) throws ConversionException
		{
			try {
				// Prepare Message
			
				Message msg = new Message();
				String msgID = getSequanceNo(SequenceType.Message);
				msg.setId(msgID);
				msg.setType(req.getType());
		
				if (null != req.getBody())
				{
					msg.setBody(req.getBody());
				}
				
				
				msg.setCreatedBy(req.getUserID());
				msg.setCreatedOn(crDate);
				msg.setModifiedBy(req.getUserID());
				msg.setModifiedOn(crDate);
				
				req.setMessage(msg);
				
					
			}
			catch(Exception e)
			{
				throw new ConversionException(e.getMessage(), e);
			}
			
		}
		
		

		public void prepareChnlMsgRelation(MessageActionVo req, Date crDate) throws ConversionException
		{
			try {
				
				ChannelMsgRelation chnlMsgRel = new ChannelMsgRelation();
				
				chnlMsgRel.setId(getSequanceNo(SequenceType.ChnlMsgRelation));
				chnlMsgRel.setChannelID(req.getChnlID());
				chnlMsgRel.setMsgId(req.getMessage().getId());
				chnlMsgRel.setSenderId(req.getUserID());
				chnlMsgRel.setCreatedBy(req.getUserID());
				chnlMsgRel.setCreatedOn(crDate);
				chnlMsgRel.setModifiedBy(req.getUserID());
				chnlMsgRel.setModifiedOn(crDate);
				
				ChnlMsgAttribute chAtr = new ChnlMsgAttribute();
				chAtr.setReadUserCount(1);
				
				chAtr.setRecipientCount(req.getChannelUsers().size());
				
				chnlMsgRel.setAttribute(chAtr);
				
				req.setChnlMsgRelation(chnlMsgRel);
					
			}
			catch(Exception e)
			{
				throw new ConversionException(e.getMessage(), e);
			}
			
		}

		/*
		@Deprecated
		public void prepareMsgBoxRelation(MessageActionVo req, Date crDate) throws ConversionException
		{
			try {
				
				if ((req.getActionType().equals(MsgActionType.Add))
					|| (req.getActionType().equals(MsgActionType.Reply))	
					)
				{
					for (ChannelParticipant cp : req.getChnlParticipants())
					{
						MsgBoxRelation mbr = new MsgBoxRelation();
						mbr.setId(getSequanceNo(SequenceType.MsgBoxRelation));
						mbr.setMsgID(req.getMessage().getId());
						
						if( null != req.getParentMsgID())
						{
							mbr.setParentMsgID(req.getParentMsgID());
						}
						
						if( null != req.getReplyattachmentID())
						{
							mbr.setParentAttacmentID(req.getReplyattachmentID());
						}
						
						mbr.setChnlID(req.getChnlID());
						
						if(cp.getUserID().equalsIgnoreCase(req.getUserID()))
						{
							mbr.setBoxType(MsgBoxType.Sent);
							String msgBoxID = getUserMsgBoxID(cp.getUserID(), MsgBoxType.Sent);
							mbr.setBoxID(msgBoxID);
							
							
							mbr.setVisible(true);
							mbr.setRead(true);
							mbr.setDelete(false);
							
							// Last Read Date for Respective Channel is Msg Sent Date
							cp.setLastReadDate(crDate);
							
						}
						else {
							
							mbr.setBoxType(MsgBoxType.Inbox);
							String msgBoxID = getUserMsgBoxID(cp.getUserID(), MsgBoxType.Inbox);
							mbr.setBoxID(msgBoxID);
							
							mbr.setVisible(true);
							mbr.setRead(false);
							mbr.setDelete(false);
						
							// In Memory Which needs to be update in DB as CurrentUnread+thisUnReadCount
							cp.setUnReadCount(1);
						}
						
						mbr.setUserID(cp.getUserID());
						mbr.setSenderID(req.getUserID());
						mbr.setMsgActionType(req.getActionType());
						mbr.setCreatedBy(req.getUserID());
						mbr.setCreatedOn(crDate);
						mbr.setModifiedBy(req.getUserID());
						mbr.setModifiedOn(crDate);
						
						req.getMsgBoxRelations().add(mbr);
						
					}
					
				}
				
					
			}
			catch(Exception e)
			{
				throw new ConversionException(e.getMessage(), e);
			}
			
		}
		
		
		*/

		
		private String getUserMsgBoxID(String userID, MsgBoxType boxType)
		{
			String result = null;
			
			if(boxType.equals(MsgBoxType.Inbox))
			{
				result = userID+"_"+"IN";
			}
			else if (boxType.equals(MsgBoxType.Sent))
			{
				result = userID+"_"+"SN";
			}
			
			return result;
		}
		
		public String convertStringListToString(List<String> reqList)
		{
			String result = null;
			if( !reqList.isEmpty())
			{
				result = StringUtils.join(reqList, ',');	
			}
			
			return result;
		}

		
		/* 
		public SignInResp convertAuthResponseToSignInResp(ResponseEntity<OAuth2AccessToken> authRespEntity) throws ConversionException
		{
			SignInResp resp = new SignInResp();
			
			try {
				
				if(!authRespEntity.getStatusCode().equals(HttpStatus.OK))
				{
					OAuth2AccessToken token = authRespEntity.getBody();
					resp.setAccessToken(token.getTokenValue());
					resp.setRefreshToken(token.getRefreshToken().getValue());
					resp.setExpiresIn(token.getExpiresIn());
					resp.setTokenType(token.getTokenType());
					
				}
				
			}catch(Exception e)
			{
				throw new ConversionException(e.getMessage(), e);
			}
			
			return resp;
		}
		*/
		
	
		public List<ChannelUser> prepareChannelUserFromParticipent(List<ChannelParticipant> chParticipants, List<User> users) throws ConversionException
		{
			List<ChannelUser> chUsers = new ArrayList<>();
			
			try {
				
				if(!chParticipants.isEmpty())
				{
					Map<String,User> userMap = users.stream()
							.collect(Collectors.toMap(User::getUid, user -> user));
					
						chParticipants.stream().forEach(cp-> {
							try {
							
								ChannelUser chu = new ChannelUser();
								chu.setChannelID(cp.getChannelID());
								chu.setChannelType(cp.getChannelType());
								chu.setUserID(cp.getUserID());
								
								if(userMap.containsKey(cp.getUserID()))
								{
									User cUser = userMap.get(cp.getUserID());
									chu.setFirstName(cUser.getFirstName());
									chu.setLastName(cUser.getLastName());
									if( null != cUser.getSrcface())
									{
										MediaImage img = new MediaImage(cUser.getSrcface(), EntityType.UserProfile, cUser.getSrcface());
										
										chu.setProfileImage(img);	
									}
									if(null != cUser.getGender())
									{
										chu.setGender(GenderType.getType(cUser.getGender()));	
									}
									else {
										chu.setGender(GenderType.Unknown);
									}
									
									
								}
								
								chu.setActive(cp.isActive());
								chu.setUserRoles(cp.getUserRoles());
								chu.setValidFrom(cp.getValidFrom());
								chu.setValidTo(cp.getValidTo());
								
								chUsers.add(chu);
								
								
							}
							catch(Exception e)
							{
								throw new RuntimeException(e.getMessage(), e);
							}
						});

				}
			}
			catch(Exception e)
			{
				throw new ConversionException(e.getMessage(), e);
			}
			
			return chUsers;
		}
		
		
		

		public Date removeDaysToDate(Date date,int days){
	        Date finalDate = null;

	        if((null != date) && (days > 0))
	        {
	            // Create Negative Value from +tv to create StartDate from CurrentDate as per Requester timezone
	            int negagiveNumberOfDays = (~(days - 1));

	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(date);
	                calendar.add(Calendar.DAY_OF_MONTH, negagiveNumberOfDays);
	                finalDate = calendar.getTime();

	        }
	        return finalDate;
	    }
		
		 public Date getOnlyDateFromDateTime(Date date)throws ParseException{
		        Date formattedDate = null;
		        if(date!=null) {
		            SimpleDateFormat dformat = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIME);
		            String dateInString = dformat.format(date);
		          formattedDate = dformat.parse(dateInString);
		         
		        }
		        return formattedDate;
		    }
		 
		  public String getDateWithoutTimeStringFromDate(Date date) {
		        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIME);
		        return (formatter.format(date));
		    }
		  
		  
		  public MessageActionVo stringToMsgActionObject(String msgActText) throws ConversionException
		    {
			  logger.info("Received Msg for Conversion:"+msgActText);
			  MessageActionVo result = null;
		    	try 
		    	{
		    		ObjectMapper objectMapper = new ObjectMapper();
		    		
			    	result = objectMapper.readValue(msgActText, MessageActionVo.class);
			    	
			    	if( null == result.getBody())
			    	{
			    		throw new ConversionException("MessageAction conversion failed ");
			    	}
			    	
			    
			    	
				} catch (JsonProcessingException  e) {
					logger.error(e.getMessage(), e);
					throw new ConversionException("Unable to Convert Json to Request object with Reason:"+e.getMessage());
				}
		    	catch(Exception e)
		    	{
		    		logger.error(e.getMessage(), e);
		    		throw new ConversionException("Unable to Convert Json to Request object with Reason:"+e.getMessage());
		    	}
		    	return result;
		    }
		  
		  
		  public String getFileNameExtension(String fileName) throws ValidationException
		  {
			  String extension = null;
			  try {
				  extension = FilenameUtils.getExtension(fileName);
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return extension;
		  }
		  
		  public String getFileNameOnly(String fileName) throws ValidationException
		  {
			  String name = null;
			  try {
				  name = FilenameUtils.getBaseName(fileName);
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return name;
		  }
		  
		  public MsgAttachmentType getAttachmentTypeByName(String extension) throws ValidationException
		  {
			  MsgAttachmentType type = null;
			  try {
				  	if((extension.equalsIgnoreCase("jpg"))
				  		||(extension.equalsIgnoreCase("jpeg"))	
				  		||(extension.equalsIgnoreCase("png"))
				    )
				  	{
				  		type = MsgAttachmentType.Image;
				  	}
				  	else if((extension.equalsIgnoreCase("mp4"))
					  	)
				  	{
				  		type = MsgAttachmentType.Video;
				  	}
				  	else if((extension.equalsIgnoreCase("hls"))
						  	)
					  	{
					  		type = MsgAttachmentType.HlsVideo;
					  	}
				  	else if((extension.equalsIgnoreCase("mp3"))
						  	)
					  	{
					  		type = MsgAttachmentType.Audio;
					  	}
				  	else if((extension.equalsIgnoreCase("doc"))
					  		||(extension.equalsIgnoreCase("docx"))	
					  		||(extension.equalsIgnoreCase("xlsx"))
					  		||(extension.equalsIgnoreCase("pdf"))
					    )
					  	{
					  		type = MsgAttachmentType.Document;
					  	}
				  	else {
				  		type = MsgAttachmentType.Invalid;
				  	}
				  	
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return type;
		  }

		  
		  public EventNotification prepareEventNotification(UserActionVo req) throws ValidationException
		  {
			  EventNotification event = new EventNotification();
			  
			  try {
				  if(req.getActionType().equals(UserActionType.Add_Friend_Request))
				  {
					  event.setId(getSequanceNo(SequenceType.EventNotification));
					  event.setSrcUserID(req.getSrcUserID());
					  event.setTrgUserID(req.getTrgUserID());
					  event.setType(EventType.Add_Friend);
					  event.setEntity(EventEntityType.User_Connection);
					  event.setEntityId(req.getUserConnectionId());
					  event.setSrcAction(UserActionType.Add_Friend_Request);
					  if( null != req.getActionMsg())
					  {
						  event.setSrcMsg(req.getActionMsg());
					  }
					  
					  event.setActionDate(req.getToday());
					  event.setValidFlag(true);
					  
					  
				  }
				  else if(req.getActionType().equals(UserActionType.Remove_Friend_Request))
				  {
					  event.setId(getSequanceNo(SequenceType.EventNotification));
					  event.setSrcUserID(req.getSrcUserID());
					  event.setTrgUserID(req.getTrgUserID());
					  event.setType(EventType.Remove_Friend);
					  event.setEntity(EventEntityType.User_Connection);
					  event.setEntityId(req.getUserConnectionId());
					  event.setSrcAction(UserActionType.Remove_Friend_Request);
					  if( null != req.getActionMsg())
					  {
						  event.setSrcMsg(req.getActionMsg());
					  }
					  
					  event.setActionDate(req.getToday());
					  event.setValidFlag(true);
					  
				  }
				  
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  
			  return event;
		  }
		  
		  
		  public List<UserConnection> prepareUserConnections(String userConnetionID, UserConnectionStatusType status, String srcUserID, String trgUserID, String modUserID, Date modDate) throws ValidationException
		  {
			  List<UserConnection> connections = new ArrayList<UserConnection>();
			  
			  try {
				  UserConnection src_trg = prepareUserConnection(userConnetionID, status, srcUserID, trgUserID, modUserID, modDate);
				  UserConnection trg_src = prepareUserConnection(userConnetionID, status,trgUserID, srcUserID, modUserID, modDate);
				  connections.add(src_trg);
				  connections.add(trg_src);
				  
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return connections;
			  
		  }
		  public UserConnection prepareUserConnection(String userConnetionID, UserConnectionStatusType status, String srcUserID, String trgUserID, String modUserID, Date modDate) throws ValidationException
		  {
			  UserConnection result = null;
			  
			  try {
				  
				  result = new UserConnection();
				  result.setId(getSequanceNo(SequenceType.UserConnection));
				  result.setConnectionId(userConnetionID);
				  result.setSrcUserId(srcUserID);
				  result.setTrgUserId(trgUserID);
				  result.setStatus(status);
				  result.setCreatedBy(modUserID);
				  result.setCreatedOn(modDate);
				  result.setModifiedBy(modUserID);
				  result.setModifiedOn(modDate);
				
				  
			  }
			  catch(Exception e)
			  {
				  throw new ValidationException(e.getMessage(), e);
			  }
			  
			  return result;
		  }
		  
		  
			public Channel prepareOneToOneMsgChannel(String modUserID, Date modDate) throws ConversionException
			{
				Channel result= new Channel();
				try {
					
					String chnlID = getSequanceNo(SequenceType.Channel);
					result.setId(chnlID);
					result.setType(ChannelType.Messaging);
					result.setSubType(ChannelSubType.OneToOne);
					result.setVisibility(VisibilityType.Private);
					
					result.setActive(true);
					result.setCreatedBy(modUserID);
					result.setCreatedOn(modDate);
					result.setModifiedBy(modUserID);
					result.setModifiedOn(modDate);
					
					
				}
				catch(Exception e)
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				
				return result;
			}
		
			
			public List<ChannelParticipant> prepareChannelParticipants(String chnlID, ChannelType chnlType, String srcUserID, String trgUserID, String modUserID, Date modDate) throws ConversionException
			{
				List<ChannelParticipant> cpList = new ArrayList<ChannelParticipant>();
				try {
					
					ChannelParticipant srcCp = prepareChannelParticipant(chnlID, chnlType, srcUserID, modUserID, modDate);
					ChannelParticipant trgCp = prepareChannelParticipant(chnlID, chnlType, trgUserID, modUserID, modDate);
					
					cpList.add(srcCp);
					cpList.add(trgCp);
					
				}
				catch(Exception e)
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				return cpList;
			}
			
			
			private ChannelParticipant prepareChannelParticipant(String chnlID, ChannelType chnlType, String userID, String modUserID, Date modDate) throws ConversionException
			{
				ChannelParticipant cp = new ChannelParticipant();
				
				try {
					
					Date endDate = getInfiniteDate();
					
					String cpID = getSequanceNo(SequenceType.ChannelParticipant);
					cp.setId(cpID);	
					cp.setChannelID(chnlID);
					cp.setChannelType(chnlType);
					cp.setUserID(userID);
					cp.setActive(true);
					
					if(userID.equals(modUserID))
					{
						cp.getUserRoles().add(UserRoleType.Admin);
					}
					else {
						cp.getUserRoles().add(UserRoleType.Author);
					}
					
					cp.setValidFrom(modDate);
					cp.setValidTo(endDate);
					cp.setCreatedBy(modUserID);
					cp.setCreatedOn(modDate);
					cp.setModifiedBy(modUserID);
					cp.setModifiedOn(modDate);
					
				}
				catch(Exception e)
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				return cp;
			}
			
			public SearchUserVo convertUserToSearchUser(User u) throws ConversionException
			{
				SearchUserVo result = null;
				try {
					
					if( null != u)
					{
						result = new SearchUserVo();
						result.setUserID(u.getUid());
						result.setFirstName(u.getFirstName());
						result.setLastName(u.getLastName());

						if( null != u.getSrcface())
						{
							MediaImage img = new MediaImage(u.getSrcface(), EntityType.UserProfile, u.getSrcface());
							
							result.setProfileImage(img);	
						}
						if(null != u.getGender())
						{
							result.setGender(GenderType.getType(u.getGender()));	
						}
						else {
							result.setGender(GenderType.Unknown);
						}

						if ( null != u.getProfileType())
						{
							result.setProfileType(u.getProfileType());
						}
						
						result.setLastLogin(u.getLastLogin());
						
					}
				}
				catch(Exception e )
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				return result;
				
			}
			
			
			public EventLog prepareEventLog()
			{
				EventLog result = null;
				
				
				
				return result;
			}
			
			
			
			public SearchVo convertUserToSearchResultUser(User u) throws ConversionException
			{
				SearchVo result = null;
				try {
					
					if( null != u)
					{
						result = new SearchVo();
						result.setEntityType(EventEntityType.User);
					
						SearchUserVo user = getSearchUserFromUser(u);
						result.setUser(user);
					
						/*
						
						result.setId(u.getUid());
						result.setFirstName(u.getFirstName());
						result.setLastName(u.getLastName());

						if( null != u.getSrcface())
						{
							MediaImage img = new MediaImage(u.getSrcface(), EntityType.UserProfile, u.getSrcface());
							
							result.setProfileImage(img);	
						}
						if(null != u.getGender())
						{
							result.setGender(GenderType.getType(u.getGender()));	
						}
						else {
							result.setGender(GenderType.Unknown);
						}

						if ( null != u.getProfileType())
						{
							result.setVisibility(u.getProfileType());
						}
						
						result.setLastLogin(u.getLastLogin());
						
						*/
						
					}
				}
				catch(Exception e )
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				return result;
				
			}
			
			public SearchUserVo getSearchUserFromUser(User u) throws ConversionException
			{
				SearchUserVo user = null;
				try {
					
					if( null != u)
					{
					
						user = new SearchUserVo();
						user.setUserID(u.getUid());
						user.setFirstName(u.getFirstName());
						user.setLastName(u.getLastName());
						if( null != u.getSrcface())
						{
							MediaImage img = new MediaImage(u.getSrcface(), EntityType.UserProfile, u.getSrcface());
							
							user.setProfileImage(img);	
						}
						
						if(null != u.getGender())
						{
							user.setGender(GenderType.getType(u.getGender()));	
						}
						else {
							user.setGender(GenderType.Unknown);
						}
						
						if ( null != u.getProfileType())
						{
							user.setProfileType(u.getProfileType());
						}
						
						user.setLastLogin(u.getLastLogin());
												
					}
				}
				catch(Exception e )
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				return user;
			}
			
			
			public SearchVo convertMsgChannelToSearchResult(Channel ch) throws ConversionException
			{
				SearchVo result = null;
				try {
					
					if( null != ch)
					{
						result = new SearchVo();
						result.setEntityType(EventEntityType.Msg_Chnl);
						
						SearchChannelVo chnl = getSearchChnlFromChnl(ch);
						result.setChnl(chnl);
						
						/*
						
						if(( null != ch.getImageList() && (!ch.getImageList().isEmpty())))
						{
							MediaImage img = new MediaImage(ch.getImageList().get(0).getId(), EntityType.Channel, ch.getImageList().get(0).getId());
							img.setImageType(ImageType.ChannelLogo);
							
							chnl.setLogo(img);	
						}
						
						
						
						result.setVisibility(ch.getVisibility());
						
						result.setId(ch.getId());
						result.setChnlName(ch.getName());
						result.setChnlDescription(ch.getDescription());
						result.setChnlType(ch.getType());
						result.setChnlSubType(ch.getSubType());
						result.setChnlSubscriptionType(ch.getSubscriptionType());

						if(( null != ch.getImageList() && (!ch.getImageList().isEmpty())))
						{
							MediaImage img = new MediaImage(ch.getImageList().get(0).getId(), EntityType.Channel, ch.getImageList().get(0).getId());
							img.setImageType(ImageType.ChannelLogo);
							
							result.setChnlLogo(img);	
						}
						
						*/
						
					}
				}
				catch(Exception e )
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				return result;
				
			}
			
			
			public SearchChannelVo getSearchChnlFromChnl(Channel ch) throws ConversionException
			{
				SearchChannelVo chnl = null;
				try {
					
					if( null != ch)
					{
					
						chnl = new SearchChannelVo();
						chnl.setId(ch.getId());
						chnl.setVisibility(ch.getVisibility());
						chnl.setName(ch.getName());
						chnl.setDescription(ch.getDescription());
						chnl.setType(ch.getType());
						chnl.setSubType(ch.getSubType());
						chnl.setSubscriptionType(ch.getSubscriptionType());
						
												
					}
				}
				catch(Exception e )
				{
					throw new ConversionException(e.getMessage(), e);
				}
				
				return chnl;
			}
			
			
			public boolean validateEmailAddress(String email)
			{
				boolean result = false ;
					
				try {
					String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(email);
					result =  matcher.matches();
				}
				catch(Exception e)
				{
					
				}
				
				return result;
			}
			
			
			public void extractEntityIdsFromUserNotificaiton(UserNotificationResp resp, List<EventNotification> searchResult) throws UserServiceException
			{
				try {
			
					Set<String> userSet = new HashSet<>();
					Set<String> chnlSet = new HashSet<>();
					
					
					searchResult.stream().forEach(log -> {
						
				if ((log.getType().equals(EventType.Add_Friend))
				|| (log.getType().equals(EventType.Remove_Friend))		
					)
				{
				
					userSet.add(log.getSrcUserID());
					userSet.add(log.getTrgUserID());
					
					
				}
				else if ((log.getType().equals(EventType.Chnl_Add_User))
						||(log.getType().equals(EventType.Chnl_Remove_User))
						)
				{
					userSet.add(log.getSrcUserID());
					chnlSet.add(log.getEntityId());
				}
						
					});
					
					resp.getSearchUserIds().addAll(userSet);
					resp.getSearchMsgChnlIds().addAll(chnlSet);
					
				}
				catch(Exception e)
				{
					throw new UserServiceException(e.getMessage(), e);
				}
			}
			
			
			
	}
