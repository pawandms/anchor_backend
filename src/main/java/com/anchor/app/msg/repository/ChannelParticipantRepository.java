package com.anchor.app.msg.repository;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.model.ChannelParticipant;

@Repository
public interface ChannelParticipantRepository extends MongoRepository<ChannelParticipant, String> {

	public List<ChannelParticipant> findByChannelID(String chnlID);
	
	@Query("{userID : ?0, active: ?1,  validFrom : {$lte : ?2}, validTo : {$gte : ?2}}")  
	public List<ChannelParticipant> findByUserIDActiveAndValidToday(String userID, boolean activeFlag,   Date todaysDate);
	
	@Query("{userID : ?0, active: ?1, channelType: ?3, validFrom : {$lte : ?2}, validTo : {$gte : ?2}}")  
	public List<ChannelParticipant> findByUserIDActiveAndValidToday(String userID, boolean activeFlag,   Date todaysDate, ChannelType channelType);

	@Query("{userID : ?0}")
	public List<ChannelParticipant> findByUserID(String userID);
	
	@Query("{userID : ?0, channelType: ?1}")
	public List<ChannelParticipant> findByUserIDAndChannelType(String userID, ChannelType channelType);

	@Query("{channelID : ?0, 'userID' : {'$in' : ?1 }, validFrom : {$lte : ?2}, validTo : {$gte : ?2} }")  
	public List<ChannelParticipant> findByUserIDsAndChannelID(String channelID, Iterable<String> userID, Date todaysDate);

	@Query("{channelID : ?0, 'userID' : ?1 , validFrom : {$lte : ?2}, validTo : {$gte : ?2} }")  
	public List<ChannelParticipant> findByUserIDAndChannelID(String channelID, String userID, Date todaysDate);
	
	@Query( value="{channelID : ?0, active: ?1, validFrom : {$lte : ?2}, validTo : {$gte : ?2}}")  
	public List<ChannelParticipant> getByChannelIDAndValidToday(String channelID, boolean activeFlag,   Date todaysDate);

	@Query("{channelID : ?0, userID: ?1}")
	public List<ChannelParticipant> findByChannelIDAndUserID(String chnlID, String userID);
	
	@Query("{channelID : {'$in' : ?0 }, active: ?1 }")
	public List<ChannelParticipant> findByChannelIds (Iterable<String> chnlIds, boolean activeFlag);

}
