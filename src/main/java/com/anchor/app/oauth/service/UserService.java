package com.anchor.app.oauth.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.exception.UserServiceException;
import com.anchor.app.msg.enums.VisibilityType;
import com.anchor.app.msg.vo.SearchUserVo;
import com.anchor.app.oauth.model.OauthClientDetails;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.model.UserAuth;
import com.anchor.app.oauth.viewmodel.ClientDetailsVo;
import com.anchor.app.oauth.vo.UserVo;

public interface UserService {
	
	/**
	 * Create User into System
	 * @param request
	 * @return
	 * @throws UserServiceException
	 */
	public UserVo createUser(UserVo request) throws UserServiceException;
	
	/**
	 * Activate User post Email Verification by Token and Uid
	 * @param token
	 * @param uid
	 * @return
	 * @throws UserServiceException
	 */
	public UserVo activateUser(String token, String uid)throws UserServiceException;
	
	public boolean isEmailPresent(String email) throws UserServiceException;
	
	public OauthClientDetails saveClientDetails(ClientDetailsVo request) throws UserServiceException;

	public User getUserDetails(String userName)throws UserServiceException;
	
	public UserAuth getUserAuthDetails(String userName) throws UserServiceException;

	public List<User> getUserForUserIds(Collection userIds)throws UserServiceException;
	
	public User getUserForUserID(String userID) throws UserServiceException;
	
	/**
	 * Add/Update Profile Image of the Respective User
	 * @param userID
	 * @param profileImage
	 * @throws UserServiceException
	 */
	public void addUpdateUserProfileImage(String reqUserID, String userID, MultipartFile profileImage)throws UserServiceException;
	
	/**
	 * Update User Profile Type
	 * @param reqUserID
	 * @param userID
	 * @param profileType
	 * @param modDate
	 * @return
	 * @throws UserServiceException
	 */
	public void updateUserProfileType(String reqUserID, String userID, VisibilityType profileType, Date modDate )throws UserServiceException;
	
	
	public void updateUserLoginStatus(String userID)throws UserServiceException;

	public Page<SearchUserVo> getUserBySearchString(String searchKey, Pageable pageable)throws UserServiceException;

}
