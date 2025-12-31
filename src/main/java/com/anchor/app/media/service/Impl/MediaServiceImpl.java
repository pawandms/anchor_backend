package com.anchor.app.media.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Term;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.enums.MediaGenreType;
import com.anchor.app.enums.MediaOrderType;
import com.anchor.app.enums.MediaType;
import com.anchor.app.exception.ImageServiceException;
import com.anchor.app.exception.MediaServiceException;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.media.model.MediaViewStat;
import com.anchor.app.media.model.MetaData;
import com.anchor.app.media.repository.MediaRepository;
import com.anchor.app.media.repository.MediaViewStatRepository;
import com.anchor.app.media.service.MediaService;
import com.anchor.app.minio.MinioService;
import com.anchor.app.model.Image;
import com.anchor.app.model.vo.ImageInfo;
import com.anchor.app.util.HelperBean;

@Service
public class MediaServiceImpl implements MediaService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MediaRepository mediaRep;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MinioService minioService;

	@Autowired
	private HelperBean helper;

	@Autowired
	private MediaViewStatRepository mediaviewRep;
	
	
	@Override
	public Media saveMedia(Media media) throws MediaServiceException {
		
		if( null == media)
		{
			throw new MediaServiceException("Empty Media can not be persisted");
		}
		media = mediaRep.save(media);
		
		return media;
	}

	@Override
	public Page<Media> getPage(MediaType mediaType, MediaGenreType genreType, MediaOrderType mediaOrder,
			Pageable pageable) {

		Page<Media> result = null;
		
		try {
		
			
			Criteria typeCrt = null;
			Criteria genreCrt = null;
			List<Criteria> andCrtList = new  ArrayList<>();
			
			if( null != mediaType)
			{
				typeCrt = Criteria.where("mediaType").is(mediaType);
				
				
			}
			 if (!genreType.equals(MediaGenreType.NONE))
			{
					genreCrt = Criteria.where("generList").is(genreType);
					
				
			}
		
			Query query = new Query();
			
			if(( null != typeCrt) && ( null != genreCrt))
			{
				query.addCriteria(new Criteria().andOperator(typeCrt, genreCrt));
			}
			else if(null != typeCrt)
			{
				query.addCriteria(typeCrt);
				
			}
		
		query.with(pageable);
	
		if( null != mediaOrder)
		{
			if(mediaOrder.equals(MediaOrderType.LATEST))
			{
				query.with(Sort.by(Sort.Direction.ASC, "createdOn"));
			}
			else if(mediaOrder.equals(MediaOrderType.POPULAR))
			{
				query.with(Sort.by(Sort.Direction.ASC, "popularity"));
			}
		}
		
		query.fields().exclude("sourceSystemType");
		query.fields().exclude("externalSystemIdentifierMap");
			
		
		
		List<Media> mediaList = mongoTemplate.find(query, Media.class, "media");
		
		result  = PageableExecutionUtils.getPage(
				mediaList,
		        pageable,
		        () -> mongoTemplate.count(query, Media.class));
		
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		
		return result;
	}

	@Override
	public Slice<Media> getSlice(MediaType mediaType, MediaGenreType genreType, MediaOrderType mediaOrder,
			Pageable pageable) {
		
		Slice<Media> mediaSlice = mediaRep.findByMediaTypeAndGenerList(mediaType, genreType, pageable);
		return mediaSlice;
	
	}

	@Override
	public ResponseEntity<?> getMediaStream(String mediaID, String range) throws MediaServiceException {

		ResponseEntity<?> result = null;
		try {
			
			// Get Media Object
			Media media = getMedia(mediaID);
			if( null == media)
			{
			throw new MediaServiceException("Invalid Media ID"+mediaID);	
			}
			
			if( !media.isVideoPresent())
			{
				throw new MediaServiceException("Video Not Present for Given MediaID"+mediaID);
			}
			
			String buckentName = media.getBucketName();
			String contentID = media.getContentID();
			long mediaSize = media.getSizeInBytes();
			
			result = minioService.getMedia(contentID, buckentName, mediaSize, range);
			
			
		}
		catch(Exception e)
		{
			throw new MediaServiceException(e.getMessage(),e);
		}
		
		return result;
		
	}

	@Override
	public Media getMedia(String mediaID) throws MediaServiceException {

		Media media = null;
		try {
		
			if( null == mediaID)
			{
				throw new MediaServiceException("Invalid Media ID");
			}
			Optional<Media> mediaOptional=  mediaRep.findById(mediaID);
			if(mediaOptional.isPresent())
			{
				media = mediaOptional.get();
				if( null == media.getMetaData())
				{
					MetaData metaData = new MetaData();
					metaData.setMediaId(media.getId());
					media.setMetaData(metaData);
					
				}

			}
		
			
			
				}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}
		
		
		return media;
	}

	@Override
	public List<Media> getMediaAggregationByCreatedUser() throws MediaServiceException {

		List<Media> mediaList = null;
		try {
			
		     MatchOperation match = new MatchOperation(Criteria.where("active").is(true));
	            GroupOperation group = Aggregation.group("mediaType", "createdBy");

	          AggregationOptions option = AggregationOptions.builder().allowDiskUse(true).cursorBatchSize(100).build();

	            Aggregation aggregate = Aggregation.newAggregation(match,group)
	           //         .withOptions(option)
	            ;
	            /*AggregationResults<UserReading> orderAggregate = mongoTemplate.aggregate(aggregate,
	                    "userReading", UserReading.class);
	*/
	            AggregationResults<Media> orderAggregate = mongoTemplate.aggregate(aggregate,
	                    "media", Media.class);


	           logger.info("Result:"+orderAggregate.getMappedResults().size());
	           mediaList = orderAggregate.getMappedResults();

		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}
		
		return mediaList;
	}

	@Override
	public void saveMediaMetaData(MetaData metaData) throws MediaServiceException {
		
		try {
			
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(metaData.getMediaId()));
			
			Update update = new Update().set("metaData", metaData);
			
			mongoOperations.upsert(query, update, Media.class);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}
		
	}

	@Override
	public Page<Media> getMediaBySearchString(String searchString, Pageable pageable)throws MediaServiceException
	{
		Page<Media> result = null;
		try {
			
			//String[] searchArray = searchString.split("[^a-zA-Z]+");
			
			//List<String> searchList =  splitSearchText(searchString);
			
		//	String[] searchArray = searchList.stream().toArray(String[]::new);
			
			//TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(searchArray);
			Term term = new Term(searchString);
			TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(term);
			
			
			/* Comment on 06-12-21 as its not giving accurate result*/
			//result = mediaRep.findAllBy(criteria, pageable);

		
			Criteria activeCrt = Criteria.where("active").is(true);
			
			
			Query query = TextQuery.queryText(criteria)
						
						//.addCriteria(activeCrt)
					  .sortByScore()
					  .with(pageable);

			query.addCriteria(activeCrt);
			
			query.fields().exclude("sourceSystemType");
			query.fields().exclude("externalSystemIdentifierMap");
			query.fields().exclude("originalTitle");
			query.fields().exclude("tagLine");
			query.fields().exclude("homePage");
			query.fields().exclude("overview");
			query.fields().exclude("release_date");
			query.fields().exclude("revenue");
			query.fields().exclude("budget");
			query.fields().exclude("createdBy");
			query.fields().exclude("createdOn");
			query.fields().exclude("modifiedBy");
			query.fields().exclude("modifiedOn");
			
			
			List<Media> mediaList = mongoOperations.find(query, Media.class);
		
					
		
		result  = 	PageableExecutionUtils.getPage(
					mediaList,
					pageable,
					() -> mongoTemplate.count(query, Media.class));
			
		
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}
		
		
		return result;
	
	}

	private List<String> splitSearchText(String str){
		
		return Stream.of(str.split("[^a-zA-Z]+"))
	      .map (elem -> new String("/"+elem+"/"))
	      .collect(Collectors.toList());
	}

	@Override
	public Media updateMedia(Media media, String modifiedUserName) throws MediaServiceException {
		try {
			
			if(null == media.getId())
			{
				throw new MediaServiceException("Invalid Media ID");
			}
			Date modT = new Date();
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(media.getId()));
			
			Update update = new Update().set("title", media.getTitle())
					.set("active", media.isActive())
					.set("overview", media.getOverview())
					.set("metaData", media.getMetaData())
					.set("modifiedBy", modifiedUserName)
					.set("modifiedOn", modT);
			
			
			mongoOperations.updateFirst(query, update, Media.class);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}
		return media;

	}

	private void markMediaInactive(String mediaID, String modifiedBy) throws MediaServiceException
	{
	try {
			
			if(null == mediaID)
			{
				throw new MediaServiceException("Invalid Media ID");
			}
			Date modT = new Date();
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(mediaID));
			
			Update update = new Update()
					.set("active", false)
					.set("modifiedBy", modifiedBy)
					.set("modifiedOn", modT);
			
			
			mongoOperations.updateFirst(query, update, Media.class);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}

	}
	
	@Override
	public MediaImage addMediaImage(EntityType entityType, ImageType imageType, String entityId, MultipartFile image)
			throws MediaServiceException {
		
		MediaImage result = null;
		Image img = null;
		
		try {
			// getFileInfo
			if(entityType.equals(EntityType.Media))
			{
				ImageInfo imgInfo = helper.getImageInfo(image);
				
				// Get Respective Media's Existing Image Details
				Media media = getMedia(entityId);
				
				if( null == media)
				{
					throw new ImageServiceException("Media not available for entity ID:"+entityId);
				}
				
				if(!media.getImageList().isEmpty())
				{
					// Verify its updating Existing Media or Insert New One
					result = media.getImageList().stream()
											.filter(mi -> imageType.equals(mi.getImageType()))
											.findAny()
											.orElse(null);
				
					if(( null != result))
					{
						img = helper.createImageObject(imgInfo, entityType, imageType, entityId, result.getId());
				
						minioService.addImage(img, image.getInputStream());
						
					}
										
					
				}
				
				if ( null == result)
				{ 	
					img = helper.createImageObject(imgInfo, entityType, imageType, entityId, null);
					result = new MediaImage(img.getId(), img.getEntityType(), img.getId());
					
					// Save Image to DB
					
					minioService.addImage(img, image.getInputStream());
					
					// Update Media Object with Updated Media
					result.setHeight(img.getHeight());
					result.setWidth(img.getWidth());
					result.setImageType(img.getImageType());
					media.getImageList().add(result);
					
					saveMedia(media);
			
					
				}
				
				
				
			}
			else {
				throw new ImageServiceException("Invalid Implementation for entity type:"+entityType.name());
			}
					
		}
		catch(Exception e)
		{
			throw new MediaServiceException(e.getMessage(), e);
		}
		
		return result;


	}

	@Override
	public int updateMediaContentID() throws MediaServiceException {

		int result = 0;
		try {
			List<Media> mediaList = mediaRep.getMediaWhereContentIDIsNull();
			String bucketName = "media";
			if( !mediaList.isEmpty())
			{
				result = mediaList.size();
				for (Media media : mediaList)
				{
					media.setContentID(media.getId());
					media.setBucketName(bucketName);
				}
				
				mediaRep.saveAll(mediaList);
			}
			
			
		}
		catch(Exception e)
		{
			throw new MediaServiceException(e.getMessage(), e);
		}
		
		return result;
	}

	@Override
	public void updateMediaViewCount(String appUserID,String mediaID) throws MediaServiceException {
		
try {
			Date startT = new Date();
			
			if(null == mediaID)
			{
				throw new MediaServiceException("Invalid Media ID");
			}
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(mediaID));
			
			Update update = new Update().inc("view_count", 1);
			mongoOperations.updateFirst(query, update, Media.class);
			
			String mediaViewStatID = appUserID+"_"+mediaID;
	
			
			mediaviewRep.findById(mediaViewStatID);
			
			Optional<MediaViewStat> optmvStat = mediaviewRep.findById(mediaViewStatID);
			
			if(optmvStat.isPresent())
			{
				MediaViewStat mvstat = optmvStat.get();
				mvstat.setModifiedOn(startT);	
			}
			else {
				
				MediaViewStat mvstat = new MediaViewStat();
				mvstat.setId(mediaViewStatID);
				mvstat.setAppUserID(appUserID);
				mvstat.setMediaID(mediaID);
				mvstat.setCreatedBy("SYSTEM");
				mvstat.setCreatedOn(startT);
				mvstat.setModifiedBy("SYSTEM");
				mvstat.setModifiedOn(startT);
				
				mediaviewRep.save(mvstat);
				
			}
			
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}
		
		
	}

	@Override
	public void deleteMedia(String mediaID) throws MediaServiceException {
		
		try  {
			if( null == mediaID)
			{
				throw new MediaServiceException("MediaID can not be null for Delete Operation");
			}
			
			// Step 1 : Fetch media Details
			Media delMedia = getMedia(mediaID);
			
			if(null == delMedia)
			{
				throw new MediaServiceException("Invalid MediaID for Delete Operation");
			}
			
			// Mark Media as In-Active to User can not see it
			markMediaInactive(mediaID, "SYSTEM");
			
			// Step 2 : Delete Media Content
			if(( null != delMedia.getContentID()) && ( null != delMedia.getBucketName()))
			{
				minioService.deleteMediaContent(delMedia.getContentID(), delMedia.getBucketName());
			}
			
			// Step 2.1 Delete Media Image If Any
			if(!delMedia.getImageList().isEmpty())
			{
				minioService.deleteImage(delMedia.getImageList());
			}
		
			// Step 3 : Delete Media Object from DB
			mediaRep.deleteById(delMedia.getId());
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}
		
	}


	
}
