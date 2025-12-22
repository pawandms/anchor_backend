package com.anchor.app.oauth.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Term;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.anchor.app.exceptions.MediaServiceException;
import com.anchor.app.exceptions.UserDaoException;
import com.anchor.app.exceptions.UserServiceException;
import com.anchor.app.oauth.enums.UserStatusType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.anchor.app.oauth.exceptions.ConversionException;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.repository.UserRepository;
import com.anchor.app.util.HelperBean;
import com.anchor.app.vo.SearchUserVo;


@Component
public class UserDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRep;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private HelperBean helper;
	
    @CachePut(value="UserCache")
	public void saveUser(User user) throws UserDaoException
	{
		try {
			
			userRep.save(user);
		}
		catch(Exception e)
		{
			throw new UserDaoException(e.getMessage(), e);
		}
		
	}
	
	@Cacheable(value="UserCache")
	public List<User> getUserByUserNameOrEmail(String userName, String email) throws UserDaoException
	{
		List<User> userList = null;
		try {
			
			userList = userRep.findByUserNameOrEmail(userName, email);
		}
		catch(Exception e)
		{
			throw new UserDaoException(e.getMessage(), e);
		}
		
		return userList;
	}

	@Cacheable(value="UserCache", key="#userID")
	public User getUserByUserID(Long userID) throws UserDaoException
	{
		User result = null;
		try {
			
			List<User> userList = userRep.findByUid(userID);
			if(!userList.isEmpty())
			{
				result = userList.get(0);
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserDaoException(e.getMessage(), e);
		}
		
		return result;
	}

	
	@Cacheable(value="UserCache")
	public List<User> getUserForUserIds(Collection userIds) throws UserDaoException {
		List<User> users = null;
		try {
			
			users = userRep.findByUidIn(userIds);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserDaoException(e.getMessage(), e);
		}
		
		return users;
	}

	
	public boolean isUserNamePresent(String userName) throws UserDaoException {
		
		boolean result = true;
		
		try {
			if(null != userName)
			{
				List<User> userList = userRep.findByUserName(userName);
				if(userList.isEmpty())
				{
					result = false;
				}
			}	
		}
		catch(Exception e)
		{
			throw new UserDaoException(e.getMessage(), e);
		}
		
		
		return result;
	}

	
	public boolean isEmailPresent(String email) throws UserDaoException {
		boolean result = true;
		
		try {
			if(null != email)
			{
				List<User> userList = userRep.findByEmail(email);
				if(userList.isEmpty())
				{
					result = false;
				}
			}
			
		}
		catch(Exception e)
		{
			throw new UserDaoException(e.getMessage(), e);
		}
			
		return result;
	}

	public void activateUserId(Long uid, String userName, Date modT) throws UserDaoException
	{
		try {
			
			if( (null == uid) || (null == userName))
			{
				throw new UserServiceException("Invalid uid or userName");
			}
			
			Query query = new Query();
			
			Criteria idCrt = Criteria.where("uid").is(uid);
			Criteria userNameCrt = Criteria.where("userName").is(userName);
		
			query.addCriteria(new Criteria().andOperator(idCrt, userNameCrt));
			
			Update update = new Update().set("status", UserStatusType.Normal.getValue())
					.set("modifiedBy", userName)
					.set("modifiedDate", modT);
			
			
			mongoOperations.updateFirst(query, update, User.class);
		}
		catch(Exception e)
		{
			throw new UserDaoException(e.getMessage(), e);
		}
		
	}
	
	@CacheEvict(value="UserCache", key="#uid")
	public void updateUserProfileContentId(Long uid, String modUserID, String contentID, Date modT) throws UserDaoException
	{
		try {
			
			if( (null == uid) || (null == modUserID))
			{
				throw new UserServiceException("Invalid uid or userName");
			}
			
			Query query = new Query();
			
			Criteria idCrt = Criteria.where("uid").is(uid);
				query.addCriteria(idCrt);
			
			Update update = new Update().set("srcface", contentID)
					.set("modifiedBy", modUserID)
					.set("modifiedDate", modT);
			
			
			mongoOperations.updateFirst(query, update, User.class);
		}
		catch(Exception e)
		{
			throw new UserDaoException(e.getMessage(), e);
		}
		
	}
	
	
	@CacheEvict(value="UserCache", key="#uid")
	public void updateUserProfileType(String modUserID, Long uid, VisibilityType profileType, Date modT) throws UserDaoException
	{
		
		try {
			
			if( (null == uid) || (null == modUserID))
			{
				throw new UserServiceException("Invalid uid or userName");
			}
			
			Query query = new Query();
			
			Criteria idCrt = Criteria.where("uid").is(uid);
				query.addCriteria(idCrt);
			
			Update update = new Update().set("profileType", profileType)
					.set("modifiedBy", modUserID)
					.set("modifiedDate", modT);
			mongoOperations.updateFirst(query, update, User.class);
		}
		catch(Exception e)
		{
			throw new UserDaoException(e.getMessage(), e);
		}
		
	}

	
	@CacheEvict(value="UserCache", key="#uid")
	public void updateUserLoginStatus(Long uid, Date modT) throws UserDaoException
	{
		
		try {
			
			Query query = new Query();
			
			Criteria idCrt = Criteria.where("uid").is(uid);
				query.addCriteria(idCrt);
			
			Update update = new Update().set("lastLogin", modT);
			mongoOperations.updateFirst(query, update, User.class);
		}
		catch(Exception e)
		{
			throw new UserDaoException(e.getMessage(), e);
		}
		
	}
	
	public Page<SearchUserVo> getUserBySearchString(String searchKey, Pageable pageable)throws MediaServiceException
	{
		Page<SearchUserVo> result = null;
		List<SearchUserVo> searchUsers = new ArrayList<>();
		try {
			
			//String[] searchArray = searchKey.split(" ");
			
			Term term = new Term(searchKey);
			TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(term).caseSensitive(false);
			
			Query query = TextQuery.queryText(criteria)
					  .sortByScore()
					  .with(pageable);
			
			List<User> userList = mongoOperations.find(query, User.class);
				
			if(!userList.isEmpty())
			{
				userList.stream().forEach(user-> {
					
					 
					try {
						SearchUserVo su = helper.convertUserToSearchUser(user);
						searchUsers.add(su);
					} catch (ConversionException e) {
					
						throw new RuntimeException(e.getMessage(), e);
					}
					
				});
			}
					
		
		result  = 	PageableExecutionUtils.getPage(
					searchUsers,
					pageable,
					() -> mongoOperations.count(query, User.class));
			
		
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new MediaServiceException(e.getMessage(), e);
		}
		
		
		return result;
	
	}

}
