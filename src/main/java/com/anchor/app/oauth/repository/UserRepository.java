package com.anchor.app.oauth.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.oauth.model.User;


@Repository
public interface UserRepository extends MongoRepository<User, Long> {
	
	 public List<User> findByUserNameOrEmail(String userName, String email);

	 List<User> findByUserName(String userName);
	 
	 List<User> findByUid(Long uid);
	 
	// @Query(value = "{ 'email' : ?0 }")
	 public List<User> findByEmail(String email);

	 /** 
	  * Get All User for matching userIDs
	  * @param uids
	  * @return
	  */
	 List<User> findByUidIn(Collection uids);
	
}
