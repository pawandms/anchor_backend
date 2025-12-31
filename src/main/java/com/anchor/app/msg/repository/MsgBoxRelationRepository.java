package com.anchor.app.msg.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.enums.MediaGenreType;
import com.anchor.app.enums.MediaType;
import com.anchor.app.media.model.Media;
import com.anchor.app.msg.model.MsgBoxRelation;
import com.anchor.app.msg.vo.MsgAggregateVo;

@Repository
public interface MsgBoxRelationRepository extends MongoRepository<MsgBoxRelation, String> {
	
	@Query("{userID: ?0, chnlID: ?1, msgID: ?2 }")  
	public List<MsgBoxRelation> getMsgBoxRelationByUserIDAndChnlIDAndMsgID(String userID, String chnlID, String msgID);

	@Aggregation(pipeline = {
			"{$match: { chnlID : ?0 , userID : ?1 }}",
			"{$lookup: { from: 'Message', localField: 'msgID', foreignField: '_id', as: 'msgList' }}",
			"{$lookup: { from: 'Message', localField: 'parentMsgID', foreignField: '_id', as: 'parentMsgList' }}"
	})
	public List<MsgAggregateVo> getMsgBoxRelationByUserIDAndChnlID (String chnlID, String userID, Pageable pageable);

	
	@Query(value="{userID: ?0, chnlID: ?1 }")
	public Page<MsgBoxRelation> getMsgBoxRelationForUser (String userID,String chnlID, Pageable pageable);


}
