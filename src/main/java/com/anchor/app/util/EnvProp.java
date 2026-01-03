package com.anchor.app.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.anchor.app.exceptions.AppConfigurationException;

import jakarta.annotation.PostConstruct;

@Component
public class EnvProp {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private Environment env;

	private boolean authEnabled;
	
    public static final String VIDEO = "/video";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int BYTE_RANGE = 1024;
    public static final int DEFAULT_BUFFER_SIZE  = 20480; // ..bytes = 20KB.
    public static final int BUFFER_LENGTH = 1024 * 16;
    public static final long EXPIRE_TIME = 1000 * 60 * 60 * 24;
    public static final Pattern RANGE_PATTERN = Pattern.compile("bytes=(?<start>\\d*)-(?<end>\\d*)");
    public static final String EXPIRE_HEADER = "Expires";
    public static final String CACHE_CONTROL_HEADER = "Cache-Control";
    public static final String CACHE_CONTROL_VALUE = "max-age=31536000";
    	
	
	private String playlistUrl;
	private String segmentUrl;

	//MongoDB Config
	private String mongodb;
	private String mongourl;
	
	
	//Fetch Image Config
	private String ImageUrl;
	
	// Batch Import Config
	private int person_import_size;

	// MovieDb Config
	private String movieDB_URL;
	private String movieDB_Image_URL;
	private String movieDB_apiKey;

	// YouTube Config
	private int youTube_download_size;
	private int youTube_convert_size;
	private int youTube_retryCount;
	private Path youtube_StaggingLocation;

	
	// Media Import Confirm
	private Path importMedia_StaggingLocation;
	
	// HLS Converion Config
	private Path ffmpegPath;
	private String hlsFolderName;
	private String hlsMasterPlayListName;
	
	// MinoConfig
	private String mediaBucketName;
	private String imgBucketName;
	private String msgBucketName;
	private String userProfileBucketName;
	private String minioAccessKey;
	private String minioSecretKey;
	private String minioUrl;
	private String minioRegion;
	
	
	// Messaging Query Config
	
	private String activationEmailQueueName;
	private String eventQueueName;
	private String appEventQueueName;
	private String eventLogQueueName;
	// Front End App Config
	
	private String emailConfirmationUrl;
	private String appLogoUrl;
	private String activationEmailSubject;
	private String activationEmail;
	
	
	// Auth Default Config
	private String defaultAuthClientId;
	private String defaultAuthClientPass;

	// Nats / SocketIO Server Config
	private String natsServerUrl;
	private String socketServer;
	private int socketPort;
	
	private String fireBaseServiceFilePath;
	
	@PostConstruct
	public void init() throws IOException
	{
		try {

			populateMongoDbConfig();

			// Populate Minio Config
			populateMinioConfig();
		

			/* 
			populateAuthConfig();
			
			this.playlistUrl = env.getProperty("media.plUrl");
			this.segmentUrl =  env.getProperty("media.segmentUrl");
			this.mongodb = env.getProperty("mongodb.database");
			this.mongourl = env.getProperty("mongodb.url"); 
			this.ImageUrl =  env.getProperty("media.ImageURL");
			populatePersonImportBatchSize();
			
			// MovieDB Config Initilization
			this.movieDB_URL = "https://api.themoviedb.org/3/";
			this.movieDB_Image_URL ="https://image.tmdb.org/t/p/original";
			//this.movieDB_apiKey = "e169b86bd6c6a69ffadb436e42d94a70";
			this.movieDB_apiKey = env.getProperty("tmdbImport.apikey");
		
			// YouTube Config
			populateYouTubeConfig();

			// Populate ImportMediaConfig
			populateImportMediaConfig();
			
			
			//HLS Config
			populateHlsConfig();
			
			
			// Populate Messaging Queue Config
			populateMsgQueueConfig();	
			
			
			// Populate FroentEnd App Configuration
			populateFrontEndConfig();
			
			// Populate Default Auth Config
			populateDefaultAuthConfig();
			
			// Populate Nats Server Config
			populateNatsConfig();
			
			populateFireBaseConfig();
			*/
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}

	}
	
	public EnvProp() {
		super();
		logger.info("Initilizing Envprop....");
	}

	private void populateMongoDbConfig() throws AppConfigurationException
	{
		try{
			String mongodbName =  env.getProperty("spring.mongodb.database");
			if( null == mongodbName)
			{
			throw new AppConfigurationException("Unable to Fetch MongoDb DB Name");
			}
			this.mongodb = mongodbName;

			String mongoUrl  = env.getProperty("spring.mongodb.uri");  
			if( null == mongoUrl)
			{
			throw new AppConfigurationException("Unable to Fetch MongoDb DB Url");
			}
			this.mongourl = mongoUrl;
		
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new AppConfigurationException(e.getMessage(), e);
			
		}

	}

	private void populateAuthConfig()
	{
		boolean authEnabled = Boolean.getBoolean(env.getProperty("app.enable-auth", "true"));
		this.authEnabled = authEnabled;
		
		
	}
	
	private void populateMinioConfig()
	{
		
		String mediaBucketName = env.getProperty("minio.bucket.media");
		
		if( null == mediaBucketName)
		{
			throw new RuntimeException("Unable to Fetch Minio Configuration");
		}
		this.mediaBucketName = mediaBucketName;
	
		
		String imgBucketName = env.getProperty("minio.bucket.image");
		this.imgBucketName = imgBucketName;
		
		String msgBucketName = env.getProperty("minio.bucket.msg");
		this.msgBucketName = msgBucketName;
		
		String userProfileBucketName = env.getProperty("minio.bucket.userProfile");
		this.userProfileBucketName = userProfileBucketName;
		
		String minioAccessKey = env.getProperty("minio.accessKey");
		this.minioAccessKey =minioAccessKey;
		
		String minioSecretKey = env.getProperty("minio.secretKey");
		this.minioSecretKey =minioSecretKey;
		

		String minioUrl = env.getProperty("minio.url");
		this.minioUrl =minioUrl;
		
		String minioRegion = env.getProperty("minio.region");
		
		if( null != minioRegion)
		{
			this.minioRegion = minioRegion;
		}
		
	}
	
	private void populatePersonImportBatchSize() 
	{
		String prImport = env.getProperty("tmdbImport.personimportsize", "100");
		this.person_import_size = Integer.parseInt(prImport);
	}

	private void populateYouTubeConfig() throws IOException
	{
		String yt_downlaod = env.getProperty("youtubeImport.downloadsize", "5");
		this.youTube_download_size = Integer.parseInt(yt_downlaod);
		
		String yt_convert = env.getProperty("youtubeImport.convertsize", "5");
		this.youTube_convert_size = Integer.parseInt(yt_convert);
		
		String yt_RetryCount = env.getProperty("youtubeImport.retrycount", "5");
		
		this.youTube_retryCount = Integer.parseInt(yt_RetryCount);
		
		String youtube_stagging = env.getProperty("youtubeImport.stagingPath");
		
		if(null == youtube_stagging)
		{
			throw new RuntimeException("Unable to fetch youtube import staging path configuration");
		}
		Path youTubeStaggingPath = Paths.get(youtube_stagging);
		
		 if(!Files.exists(youTubeStaggingPath, LinkOption.NOFOLLOW_LINKS))
			{
			 Files.createDirectories(youTubeStaggingPath);
					
			}
		
		 this.youtube_StaggingLocation = youTubeStaggingPath;
		
	}
	
	
	private void populateImportMediaConfig() throws IOException
	{
		
		String importMedia_stagging = env.getProperty("mediaImport.stagingPath");
		
		Path importMediaStaggingPath = Paths.get(importMedia_stagging);
		
		 if(!Files.exists(importMediaStaggingPath, LinkOption.NOFOLLOW_LINKS))
			{
			 Files.createDirectories(importMediaStaggingPath);
					
			}
		
		 this.importMedia_StaggingLocation = importMediaStaggingPath;
		
	}
	
	private void populateHlsConfig() throws IOException
	{
		
		hlsFolderName = env.getProperty("hlsConversion.hlsfolderName", "HLS");
		hlsMasterPlayListName = env.getProperty("hlsConversion.hlsMasterPlayListName", "master.m3u8");
		
		String ffmpegPathStr = env.getProperty("hlsConversion.ffmpegPath");
		
		Path ffmpegPath = Paths.get(ffmpegPathStr);
		
		 if(!Files.exists(ffmpegPath, LinkOption.NOFOLLOW_LINKS))
			{
				 throw new IOException("Unable FFMPEG Executable Path");
					
			}
		
		 this.ffmpegPath = ffmpegPath;
		
	}
	
	private void populateMsgQueueConfig()
	{
		String activationEmailQueueName = env.getProperty("msgqueue.activationemail.quenname");
		
		if( null == activationEmailQueueName)
		{
			throw new RuntimeException("Unable to Fetch Messaging Queue Configuration");
		}
		
		this.activationEmailQueueName = activationEmailQueueName;
		
		String eventQueueName = env.getProperty("msgqueue.eventquename");
		if( null == eventQueueName)
		{
			throw new RuntimeException("Unable to Fetch Messaging Queue Configuration");
		}
		
		this.eventQueueName = eventQueueName;
		
		String appEventQueueName = env.getProperty("msgqueue.appeventqueuename");
		if( null == appEventQueueName)
		{
			throw new RuntimeException("Unable to Fetch App EventMessaging Queue Configuration");
		}
		
		this.appEventQueueName = appEventQueueName;
		
		String eventLogQueueName = env.getProperty("msgqueue.event_log_queue");
		if( null == eventLogQueueName)
		{
			throw new RuntimeException("Unable to Fetch EventLog Queue Configuration");
		}
		
		this.eventLogQueueName = eventLogQueueName;
		
	}
	
	
	private void populateFrontEndConfig()
	{
		String emailConfirmationUrl = env.getProperty("frontend-app.url.emailConfirmation");
		
		if( null == emailConfirmationUrl)
		{
			throw new RuntimeException("Unable to Fetch FrontEnd App Configuration");
		}
		
		this.emailConfirmationUrl = emailConfirmationUrl;
		
		String appLogoUrl = env.getProperty("frontend-app.url.applogo");
		
		if( null == appLogoUrl)
		{
			throw new RuntimeException("Unable to Fetch FrontEnd App Configuration");
		}
		
		this.appLogoUrl = appLogoUrl;
		
		String activationEmailSubject = env.getProperty("frontend-app.activation.subject");
		
		if( null == activationEmailSubject)
		{
			throw new RuntimeException("Unable to Fetch FrontEnd App Configuration");
		}
		
		this.activationEmailSubject = activationEmailSubject;
		
		String activationEmail = env.getProperty("frontend-app.activation.email");
		
		if( null == activationEmail)
		{
			throw new RuntimeException("Unable to Fetch FrontEnd App Configuration");
		}
		
		this.activationEmail = activationEmail;
	}
	
	
	private void populateDefaultAuthConfig()
	{
		String defaultAuthClientId = env.getProperty("auth.default.client.id");
		
		if( null == defaultAuthClientId)
		{
			throw new RuntimeException("Unable to Fetch Default AuthConfiguration");
		}
		
		this.defaultAuthClientId = defaultAuthClientId;
		
		String defaultAuthClientPass = env.getProperty("auth.default.client.password");
		
		if( null == defaultAuthClientPass)
		{
			throw new RuntimeException("Unable to Fetch Default AuthConfiguration");
		}
		
		this.defaultAuthClientPass = defaultAuthClientPass;
		
	}

	private void populateNatsConfig()
	{
		String natsServerUrl = env.getProperty("nats.server.url");
		
		if( null == natsServerUrl)
		{
			throw new RuntimeException("Unable to Fetch Nats Server config");
		}
		
		this.natsServerUrl = natsServerUrl;
		
		String socketServer = env.getProperty("socketio.server.host");
		if( null == socketServer)
		{
			throw new RuntimeException("Unable to Fetch SocketIo Server config");
		}
		
		this.socketServer = socketServer;
		
		String socketPortTxt = env.getProperty("socketio.server.port");
		
		if( null == socketPortTxt)
		{
			throw new RuntimeException("Unable to Fetch SocketIo Server config");
		}
		
		this.socketPort = Integer.parseInt(socketPortTxt);
		
		
	}
	
	
	private void populateFireBaseConfig()
	{
		String fireBaseServiceFilePath = env.getProperty("firebase.service.file");
		
		if( null == fireBaseServiceFilePath)
		{
			throw new RuntimeException("Unable to Fetch FireBase config");
		}
		
		this.fireBaseServiceFilePath = fireBaseServiceFilePath;
		
	}

	

	public String getPlaylistUrl() {
		return playlistUrl;
	}



	public String getSegmentUrl() {
		return segmentUrl;
	}



	public String getMongodb() {
		return mongodb;
	}



	public String getMongourl() {
		return mongourl;
	}



	public String getImageUrl() {
		return ImageUrl;
	}

	public int getPerson_import_size() {
		return person_import_size;
	}



	public String getMovieDB_URL() {
		return movieDB_URL;
	}

	public String getMovieDB_Image_URL() {
		return movieDB_Image_URL;
	}

	public String getMovieDB_apiKey() {
		return movieDB_apiKey;
	}

	public int getYouTube_download_size() {
		return youTube_download_size;
	}

	public int getYouTube_convert_size() {
		return youTube_convert_size;
	}

	public Path getYoutube_StaggingLocation() {
		return youtube_StaggingLocation;
	}

	public int getYouTube_retryCount() {
		return youTube_retryCount;
	}

	public Path getFfmpegPath() {
		return ffmpegPath;
	}

	public String getHlsFolderName() {
		return hlsFolderName;
	}

	public String getHlsMasterPlayListName() {
		return hlsMasterPlayListName;
	}

	

	public String getMediaBucketName() {
		return mediaBucketName;
	}

	public String getImgBucketName() {
		return imgBucketName;
	}
	
	public String getMsgBucketName() {
		return msgBucketName;
	}
	
	public String getUserProfileBucketName() {
		return userProfileBucketName;
	}

	public String getMinioAccessKey() {
		return minioAccessKey;
	}

	public String getMinioSecretKey() {
		return minioSecretKey;
	}

	public String getMinioUrl() {
		return minioUrl;
	}
	
	
	public String getMinioRegion() {
		return minioRegion;
	}

	public Path getImportMedia_StaggingLocation() {
		return importMedia_StaggingLocation;
	}

	public String getActivationEmailQueueName() {
		return activationEmailQueueName;
	}
	
	public String getEventQueueName() {
		return eventQueueName;
	}
	
	public String getAppEventQueueName() {
		return appEventQueueName;
	}

	public String getEmailConfirmationUrl() {
		return emailConfirmationUrl;
	}

	public String getAppLogoUrl() {
		return appLogoUrl;
	}

	public String getActivationEmailSubject() {
		return activationEmailSubject;
	}

	public String getActivationEmail() {
		return activationEmail;
	}

	
	public String getDefaultAuthClientId() {
		return defaultAuthClientId;
	}

	public String getDefaultAuthClientPass() {
		return defaultAuthClientPass;
	}

	public boolean isAuthEnabled() {
		return authEnabled;
	}

	public String getNatsServerUrl() {
		return natsServerUrl;
	}

	public String getSocketServer() {
		return socketServer;
	}

	public int getSocketPort() {
		return socketPort;
	}

	public String getFireBaseServiceFilePath() {
		return fireBaseServiceFilePath;
	}

	public String getEventLogQueueName() {
		return eventLogQueueName;
	}


	
	
	
}
