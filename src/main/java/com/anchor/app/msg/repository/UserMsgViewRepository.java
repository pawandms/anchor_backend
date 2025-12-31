package com.anchor.app.msg.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.UserMsgView;

@Repository
public interface UserMsgViewRepository extends MongoRepository<UserMsgView, String> {


	@Query(value="{userID: ?0, chnlID: ?1 }")
	public Page<UserMsgView> getUserMsgForChannel (String userID,String chnlID, Pageable pageable);

}
