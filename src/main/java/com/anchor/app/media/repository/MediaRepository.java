package com.anchor.app.media.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.enums.MediaGenreType;
import com.anchor.app.enums.MediaType;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaMetaData;

@Repository
public interface MediaRepository extends MongoRepository<Media, String> {

	
	@Query(value="{ $and: [ { active : true, mediaType: { $eq: ?0 } }, { generList: { $eq: ?1 } } ] }", fields="{ 'sourceSystemType' : 0, 'externalSystemIdentifierMap' : 0,'originalTitle' : 0, 'tagLine' : 0,  'homePage' : 0, 'overview' : 0, 'release_date' : 0, 'revenue' : 0, 'budget' : 0, 'createdBy' : 0, 'createdOn' : 0, 'modifiedBy': 0, 'modifiedOn' : 0 }")
	public Page<Media> findByMediaTypeAndGenerList (MediaType mediaType, MediaGenreType genreType, Pageable pageable);

	@Query(value="{ active : true,  mediaType: { $eq: ?0 }}", fields="{ 'sourceSystemType' : 0, 'externalSystemIdentifierMap' : 0,'originalTitle' : 0, 'tagLine' : 0,  'homePage' : 0, 'overview' : 0, 'release_date' : 0, 'revenue' : 0, 'budget' : 0, 'createdBy' : 0, 'createdOn' : 0, 'modifiedBy': 0, 'modifiedOn' : 0 }")
	public Page<Media> findByMediaType(MediaType mediaType, Pageable pageable);

	
	@Query(value = "{ '_id' : {'$in' : ?0 } }")
	public Page<Media> findAllByIdsIn(Iterable<String> ids, Pageable pageable);

	@Query(value="{ createdBy: { $eq: ?0 }}", fields="{ 'sourceSystemType' : 0, 'externalSystemIdentifierMap' : 0,'originalTitle' : 0, 'tagLine' : 0,  'homePage' : 0, 'overview' : 0, 'release_date' : 0, 'revenue' : 0, 'budget' : 0, 'createdBy' : 0, 'createdOn' : 0, 'modifiedBy': 0, 'modifiedOn' : 0 }")
	public Page<Media> findByCreatedBy(String userName, Pageable pageable);

	@Query(value="{ 'metaData.metaDataList' : { $in: [?0]}}", fields="{ 'sourceSystemType' : 0, 'externalSystemIdentifierMap' : 0,'originalTitle' : 0, 'tagLine' : 0,  'homePage' : 0, 'overview' : 0, 'release_date' : 0, 'revenue' : 0, 'budget' : 0, 'createdBy' : 0, 'createdOn' : 0, 'modifiedBy': 0, 'modifiedOn' : 0 }")
	public Page<Media> findByMetaDataText(Iterable<String> searchString, Pageable pageable);

	
	// Paginate over a full-text search result
	@Query(value="{ active : true }",fields="{ 'sourceSystemType' : 0, 'externalSystemIdentifierMap' : 0,'originalTitle' : 0, 'tagLine' : 0,  'homePage' : 0, 'overview' : 0, 'release_date' : 0, 'revenue' : 0, 'budget' : 0, 'createdBy' : 0, 'createdOn' : 0, 'modifiedBy': 0, 'modifiedOn' : 0 }")
	Page<Media> findAllBy(TextCriteria criteria, Pageable pageable);

		
	@Query(value="{ contentID : {$exists : false }, isVideoPresent : true }", fields="{ 'sourceSystemType' : 0, 'externalSystemIdentifierMap' : 0,'originalTitle' : 0, 'tagLine' : 0,  'homePage' : 0, 'overview' : 0, 'release_date' : 0, 'revenue' : 0, 'budget' : 0, 'createdBy' : 0, 'createdOn' : 0, 'modifiedBy': 0, 'modifiedOn' : 0 }")
	List<Media> getMediaWhereContentIDIsNull();

	@Query(fields="{ 'sourceSystemType' : 0, 'externalSystemIdentifierMap' : 0,'originalTitle' : 0, 'tagLine' : 0,  'homePage' : 0, 'overview' : 0, 'release_date' : 0, 'revenue' : 0, 'budget' : 0, 'createdBy' : 0, 'createdOn' : 0, 'modifiedBy': 0, 'modifiedOn' : 0 }")
	public Page<Media> findAllBy(org.springframework.data.mongodb.core.query.Query query, Pageable pageable);
	
}
