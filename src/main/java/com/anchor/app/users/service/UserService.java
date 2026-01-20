package com.anchor.app.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import com.anchor.app.dto.ErrorMsg;
import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.exceptions.UserServiceException;
import com.anchor.app.exceptions.ValidationException;
import com.anchor.app.media.dto.StreamMediaInfo;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.service.MediaService;
import com.anchor.app.oauth.dto.AuthReq;
import com.anchor.app.oauth.enums.GenderType;
import com.anchor.app.oauth.enums.PermissionType;
import com.anchor.app.oauth.enums.UserIdentifyType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.anchor.app.oauth.exceptions.AuthServiceException;
import com.anchor.app.msg.service.ChannelService;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.model.UserAuth;
import com.anchor.app.oauth.model.UserVerifyToken;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.users.dto.SignUpRequest;
import com.anchor.app.users.dto.UserInfoUpdateRequest;
import com.anchor.app.users.dto.UserProfile;
import com.anchor.app.users.enums.UserRoleType;
import com.anchor.app.users.repository.UserAuthRepository;
import com.anchor.app.users.repository.UserRepository;
import com.anchor.app.users.repository.UserVerifyTokenRepository;
import com.anchor.app.util.HelperBean;
import com.anchor.app.util.enums.SequenceType;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthRepository userAuthRepository;
    
    @Autowired
    private UserVerifyTokenRepository userVerifyTokenRepository;
    
    @Autowired
    private ChannelService channelService;
    
    @Autowired
    private HelperBean helperBean;

    @Autowired
	private AuthService authService;
    
    @Autowired
    private MediaService mediaService;
    
    /**
     * Validate structural errors from BindingResult (annotation-based validation)
     * 
     * @param request SignUpRequest object
     * @param bindingResult Spring validation result
     */
    public void validateStructuralErrors(SignUpRequest request, BindingResult bindingResult) {
        try {

            if (bindingResult.hasErrors()) {
                request.setValid(false);
                bindingResult.getFieldErrors().forEach(error -> 
                    request.getErrors().add(new ErrorMsg(
                        error.getCode(),
                        error.getField(),
                        error.getDefaultMessage()
                    ))
                );
            }

            // Set default values if not provided
            if (request.getRole() == null) {
                request.setRole(UserRoleType.GENERAL_USER);
            }
            if (request.getProfileType() == null) {
                request.setProfileType(VisibilityType.Public);
            }

            if( null == request.getGender())
            {
                request.setGender(GenderType.Unknown);
            }    
       
        } catch (Exception e) {
            logger.error("Error during structural validation: {}", e.getMessage(), e);
            request.setValid(false);
            request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), "general", "Error during validation: " + e.getMessage()));
        }
    }
    
    /**
     * Validate business rules (database-related checks)
     * 
     * @param request SignUpRequest object
     */
    private void validateBusinessRules(SignUpRequest request) {
        try {
            // Validate email doesn't exist
            if (userRepository.existsByEmail(request.getEmail())) {
                logger.warn("Registration failed - email already exists: {}", request.getEmail());
                request.setValid(false);
                request.getErrors().add(new ErrorMsg(ValidationErrorType.Email_Already_Present.name(), "email", request.getEmail()));
            }
            
            // Validate username if provided
            if (request.getUserName() != null && !request.getUserName().isEmpty()) {
                if (userRepository.existsByUserName(request.getUserName())) {
                    logger.warn("Registration failed - username already exists: {}", request.getUserName());
                    request.setValid(false);
                    request.getErrors().add(new ErrorMsg(ValidationErrorType.UserName_Already_Taken.name(), "userName", request.getUserName()));
                }
            }
            
            // Validate mobile if provided
            if (request.getMobile() != null && !request.getMobile().isEmpty()) {
                if (userRepository.existsByMobile(request.getMobile())) {
                    logger.warn("Registration failed - mobile already exists: {}", request.getMobile());
                    request.setValid(false);
                    request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Mobile.name(), "mobile", request.getMobile()));
                }
            }
        } catch (Exception e) {
            logger.error("Error during business validation: {}", e.getMessage(), e);
            request.setValid(false);
            request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Request.name(), "general", "Database error during validation: " + e.getMessage()));
        }
    }
    
    
    /**
     * Register a new user
     * 
     * @param request SignUpRequest containing user details - response fields will be populated
     * @param bindingResult Spring validation result for structural validation
     */
    public void signUpUser(SignUpRequest request, BindingResult bindingResult) throws UserServiceException {
        
        logger.info("Starting user registration for email: {}", request.getEmail());
        
        try {
            
            request.setValid(true);
        // Validate structural errors (annotation-based validation)
        validateStructuralErrors(request, bindingResult);
        
        // If structural validation failed, throw exception
        if (!request.isValid()) {
            throw new ValidationException("Structural validation failed");
        }
        
        // Validate business rules (database-related checks)
        validateBusinessRules(request);
        
        // If business validation failed, throw exception
        if (!request.isValid()) {
            throw new ValidationException("Business validation failed");
        }

            persistUser(request);
            
            logger.info("User registration completed successfully for: {}", request.getEmail());
                
        }
        catch(ValidationException e)
        {
            throw new UserServiceException("Failed to register user: " + e.getMessage());
        }
         catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage(), e);
            request.setValid(false);
            request.setErrorMessage("Failed to register user: " + e.getMessage());
            throw new UserServiceException("Failed to register user: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void persistUser(SignUpRequest request) throws UserServiceException
    {
        try{
            Date now = new Date();
            String userId = helperBean.getSequanceNo(SequenceType.User);
            String userName = request.getUserName() != null && !request.getUserName().isEmpty() 
                ? request.getUserName() 
                : request.getEmail();
            
            // Create User record
            User user = User.builder()
                .id(userId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(userName)
                .email(request.getEmail())
                .mobile(request.getMobile())
                .gender(request.getGender())
                .dob(request.getDob())
                .admin(false)
                .status(com.anchor.app.oauth.enums.UserStatusType.Normal.getValue()) // Normal user
                .profileType(request.getProfileType())
                .isTwoStepVerificationEnabled(false)
                .notificationSettings(helperBean.getDefaultUserNotification())
                .privacySettings(helperBean.getDefaultUserPrivecy())
                .crUser(userId)
                .crDate(now)
                .modUser(userId)
                .modDate(now)
                .build();
            
            userRepository.save(user);
            logger.info("User record created with ID: {}", userId);
            
            // Create UserAuth record
            String hashedPassword = helperBean.encriptPassword(request.getPassword());
            
            UserAuth userAuth = UserAuth.builder()
            .id(user.getId())
            .identityType(UserIdentifyType.EMAIL.getValue()) // Email
            .identifier(request.getEmail())
            .certificate(hashedPassword)
            .enabled(true)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .roles(java.util.Collections.singletonList(request.getRole().name()))
            .crUser(userId)
            .crDate(now)
            .modUser(userId)
            .modDate(now)
            .build();

            userAuthRepository.save(userAuth);
            logger.info("UserAuth record created for user: {}", userId);
            
            // Create UserVerifyToken for email verification
            String id = helperBean.getSequanceNo(SequenceType.UserVerifyToken);
            String token = helperBean.getUUIDFromString(request.getEmail()).toString();
            UserVerifyToken verifyToken = UserVerifyToken.builder()
                .id(id)
                .userName(userName)
                .verifyToken(token)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailAddress(request.getEmail())
                .valid(true)
                .crUser(userId)
                .crDate(now)
                .modUser(userId)
                .modDate(now)
                .build();
            
            userVerifyTokenRepository.save(verifyToken);
            logger.info("Verification token created for user: {}", userId);
            
            // Create Self Channel for user using ChannelService
            channelService.createSelfChannel(userId, userName);
            
            // Populate response fields in request object
            request.setUserId(userId);
            request.setVerificationToken(token);
           
            
        }
        catch(Exception e)
        {
            throw new UserServiceException(e.getMessage(),e );
        }
    }
    
    /**
     * Check if email exists in the system
     * 
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    

    
	/**
     * Get user profile by user ID with authorization and validation
     * 
     * @param request UserProfile request object containing userId and other validation data
     * @return UserProfile or null if not found
     * @throws UserServiceException if error occurs
     */
    public void getUserProfileById(UserProfile request) throws UserServiceException {
        try {
            request.setValid(true);
            
            // Validate user ID
            if (request.getId() == null || request.getId().isEmpty()) {
                request.setValid(false);
                request.getErrors().add(new ErrorMsg(
                    ValidationErrorType.Invalid_Request.name(),
                    "userId",
                    "User ID cannot be null or empty"
                ));
                return;
            }
            
            // Check permissions - reqUserID will be populated from authenticated user in AuthService
            AuthReq authReq = new AuthReq(null, request.getId(), PermissionType.UsrView);
            boolean hasPermission = authService.hasPersmission(authReq);
            
            if(!hasPermission)
		    {
			 throw new AuthServiceException("Invalid Permission");
		    }    
            
            // Fetch user profile
            Optional<User> userOpt = userRepository.findById(request.getId());
            
            if (userOpt.isPresent()) {
                logger.info("User profile found for userId: {}", request.getId());
                User user = userOpt.get();
                
                // Populate request object with user data using helper method
                helperBean.populateUserProfile(request, user);
                
            } else {
                logger.warn("User profile not found for userId: {}", request.getId());
                request.setValid(false);
                request.getErrors().add(new ErrorMsg(
                    ValidationErrorType.Invalid_UserId.name(),
                    "userId",
                    "User not found with ID: " + request.getId()
                ));
            }
            
        } catch (Exception e) {
            logger.error("Error fetching user profile for userId: {}", request.getId(), e);
            throw new UserServiceException("Failed to fetch user profile: " + e.getMessage(), e);
        }
    }
    
	public User getUserDetails(String userName) throws UserServiceException {
			
		User user = null;
		try {
			UserAuth userAuth = getUserAuthDetails(userName);
			
			if(null != userAuth)
			{
				String userID = userAuth.getId();
				Optional<User> userOpt = userRepository.findById(userID);
				
                if(userOpt.isPresent())
                {
                    user = userOpt.get();
                }    
				
			}
		
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return user;
	}

    
	public UserAuth getUserAuthDetails(String userName) throws UserServiceException {
		
		UserAuth uauth = null;
		
		try {
			
			if( null != userName)
			{
				uauth = userAuthRepository.findByUserName(userName);
				
			}
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return uauth;
	}

    
    public void addUpdateUserProfileImage(StreamMediaInfo req) throws UserServiceException
    {
        String mediaId = null;
        try{
            req.setValid(true);
            
           // Perform Authorization - reqUserID will be populated from authenticated user in AuthService
			AuthReq authReq = new AuthReq(null, req.getUserID(), PermissionType.UsrEdit);

            boolean hasPermission = authService.hasPersmission(authReq);
		
		 if(!hasPermission)
		 {
			 throw new AuthServiceException("Invalid Permission");
		 }
         
         // Get existing media ID from user profile
         Optional<User> userOpt = userRepository.findById(req.getUserID());
         if (userOpt.isPresent()) {
             mediaId = userOpt.get().getProfileImageMediaId();
         }
         
         Media media = mediaService.saveUserProfileImage(req.getUserID(), mediaId, req.getInputFile());

         // Update User Object for ProfileImageID
         updateProfileImageMediaId(req.getUserID(), media.getId());
         req.setMediaId(media.getId());
         req.setMediaType(media.getType());
         
			
        }
        catch(Exception e)
        {
            throw new UserServiceException(e.getMessage(), e);
        }


    }

    /**
     * Update profileImageMediaId for a user
     * Uses repository method for direct field update
     * 
     * @param userId User ID
     * @param mediaId Media ID to set as profile image
     * @throws UserServiceException if update fails
     */
    private  void updateProfileImageMediaId(String userId, String mediaId) throws UserServiceException {
        try {
            // Check if user exists
            if (!userRepository.existsById(userId)) {
                throw new UserServiceException("User not found with ID: " + userId);
            }
            
            // Update using repository method
            userRepository.updateProfileImageMediaId(userId, mediaId, userId, new Date());
            
            logger.info("Successfully updated profileImageMediaId for user: {}", userId);
            
        } catch (Exception e) {
            logger.error("Error updating profileImageMediaId for user: {}", userId, e);
            throw new UserServiceException("Failed to update profile image media ID: " + e.getMessage(), e);
        }
    }

    /**
     * Update privacy settings for a user
     * 
     * @param userId User ID
     * @param privacySettings UserPrivacy object containing privacy settings to update
     * @throws UserServiceException if update fails
     */
    public void updateUserPrivacySettings(String userId, com.anchor.app.users.dto.UserPrivacy privacySettings) throws UserServiceException {
        try {
            logger.info("Updating privacy settings for user: {}", userId);
            
            // Validate user ID
            if (userId == null || userId.isEmpty()) {
                throw new UserServiceException("User ID cannot be null or empty");
            }
            
            // Validate privacy settings object
            if (privacySettings == null) {
                throw new UserServiceException("Privacy settings cannot be null");
            }
            
            // Perform Authorization - reqUserID will be populated from authenticated user in AuthService
            AuthReq authReq = new AuthReq(null, userId, PermissionType.UsrEdit);
            boolean hasPermission = authService.hasPersmission(authReq);
            
            if (!hasPermission) {
                throw new AuthServiceException("Invalid Permission");
            }
            
            // Check if user exists
            if (!userRepository.existsById(userId)) {
                throw new UserServiceException("User not found with ID: " + userId);
            }
            
            // Update using repository method
            userRepository.updatePrivacySettings(userId, privacySettings, userId, new Date());
            
            logger.info("Successfully updated privacy settings for user: {}", userId);
            
        } catch (AuthServiceException e) {
            logger.error("Authorization failed for user: {}", userId, e);
            throw new UserServiceException("Not authorized to update privacy settings: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating privacy settings for user: {}", userId, e);
            throw new UserServiceException("Failed to update privacy settings: " + e.getMessage(), e);
        }
    }

    /**
     * Update notification settings for a user
     * 
     * @param userId User ID
     * @param notificationSettings UserNotification object containing notification settings to update
     * @throws UserServiceException if update fails
     */
    public void updateUserNotificationSettings(String userId, com.anchor.app.users.dto.UserNotification notificationSettings) throws UserServiceException {
        try {
            logger.info("Updating notification settings for user: {}", userId);
            
            // Validate user ID
            if (userId == null || userId.isEmpty()) {
                throw new UserServiceException("User ID cannot be null or empty");
            }
            
            // Validate notification settings object
            if (notificationSettings == null) {
                throw new UserServiceException("Notification settings cannot be null");
            }
            
            // Perform Authorization - reqUserID will be populated from authenticated user in AuthService
            AuthReq authReq = new AuthReq(null, userId, PermissionType.UsrEdit);
            boolean hasPermission = authService.hasPersmission(authReq);
            
            if (!hasPermission) {
                throw new AuthServiceException("Invalid Permission");
            }
            
            // Check if user exists
            if (!userRepository.existsById(userId)) {
                throw new UserServiceException("User not found with ID: " + userId);
            }
            
            // Update using repository method
            userRepository.updateNotificationSettings(userId, notificationSettings, userId, new Date());
            
            logger.info("Successfully updated notification settings for user: {}", userId);
            
        } catch (AuthServiceException e) {
            logger.error("Authorization failed for user: {}", userId, e);
            throw new UserServiceException("Not authorized to update notification settings: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating notification settings for user: {}", userId, e);
            throw new UserServiceException("Failed to update notification settings: " + e.getMessage(), e);
        }
    }

    /**
     * Update nickName for a user
     * 
     * @param userId User ID
     * @param nickName Nick name to update
     * @throws UserServiceException if update fails
     */
    public void updateUserNickName(String userId, String nickName) throws UserServiceException {
        try {
            logger.info("Updating nickName for user: {}", userId);
            
            // Validate user ID
            if (userId == null || userId.isEmpty()) {
                throw new UserServiceException("User ID cannot be null or empty");
            }
            
            // Validate nickName
            if (nickName == null || nickName.trim().isEmpty()) {
                throw new UserServiceException("Nick name cannot be null or empty");
            }
            
            // Perform Authorization - reqUserID will be populated from authenticated user in AuthService
            AuthReq authReq = new AuthReq(null, userId, PermissionType.UsrEdit);
            boolean hasPermission = authService.hasPersmission(authReq);
            
            if (!hasPermission) {
                throw new AuthServiceException("Invalid Permission");
            }
            
            // Check if user exists
            if (!userRepository.existsById(userId)) {
                throw new UserServiceException("User not found with ID: " + userId);
            }
            
            // Update using repository method
            userRepository.updateNickName(userId, nickName.trim(), userId, new Date());
            
            logger.info("Successfully updated nickName for user: {}", userId);
            
        } catch (AuthServiceException e) {
            logger.error("Authorization failed for user: {}", userId, e);
            throw new UserServiceException("Not authorized to update nick name: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating nickName for user: {}", userId, e);
            throw new UserServiceException("Failed to update nick name: " + e.getMessage(), e);
        }
    }

    /**
     * Update mobile for a user
     * 
     * @param userId User ID
     * @param mobile Mobile number to update
     * @throws UserServiceException if update fails
     */
    public void updateUserMobile(String userId, String mobile) throws UserServiceException {
        try {
            logger.info("Updating mobile for user: {}", userId);
            
            // Validate user ID
            if (userId == null || userId.isEmpty()) {
                throw new UserServiceException("User ID cannot be null or empty");
            }
            
            // Validate mobile
            if (mobile == null || mobile.trim().isEmpty()) {
                throw new UserServiceException("Mobile number cannot be null or empty");
            }
            
            // Perform Authorization - reqUserID will be populated from authenticated user in AuthService
            AuthReq authReq = new AuthReq(null, userId, PermissionType.UsrEdit);
            boolean hasPermission = authService.hasPersmission(authReq);
            
            if (!hasPermission) {
                throw new AuthServiceException("Invalid Permission");
            }
            
            // Check if user exists
            if (!userRepository.existsById(userId)) {
                throw new UserServiceException("User not found with ID: " + userId);
            }
            
            // Check if mobile already exists for another user
            Optional<User> existingUser = userRepository.findByMobile(mobile);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                throw new UserServiceException("Mobile number already registered with another user");
            }
            
            // Update using repository method with current timestamp
            Long mobileBindTime = System.currentTimeMillis();
            userRepository.updateMobile(userId, mobile.trim(), mobileBindTime, userId, new Date());
            
            logger.info("Successfully updated mobile for user: {}", userId);
            
        } catch (AuthServiceException e) {
            logger.error("Authorization failed for user: {}", userId, e);
            throw new UserServiceException("Not authorized to update mobile number: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating mobile for user: {}", userId, e);
            throw new UserServiceException("Failed to update mobile number: " + e.getMessage(), e);
        }
    }

    /**
     * Validate structural errors for user info update (field-level validation)
     * 
     * @param userId User ID
     * @param request UserInfoUpdateRequest object
     */
    private void validateUserInfoStructuralErrors(String userId, UserInfoUpdateRequest request) {
        try {
            // Validate user ID
            if (userId == null || userId.isEmpty()) {
                request.setValid(false);
                request.getErrors().add(new ErrorMsg(
                    ValidationErrorType.Invalid_Request.name(),
                    "userId",
                    "User ID cannot be null or empty"
                ));
            }
            
            // Validate that at least one field is provided
            if (request.getFirstName() == null && request.getLastName() == null && 
                request.getNickName() == null && request.getMobile() == null && request.getEmail() == null) {
                request.setValid(false);
                request.getErrors().add(new ErrorMsg(
                    ValidationErrorType.Invalid_Request.name(),
                    "request",
                    "At least one field must be provided for update"
                ));
            }
            
            // Validate email format if provided
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
                if (!request.getEmail().matches(emailRegex)) {
                    request.setValid(false);
                    request.getErrors().add(new ErrorMsg(
                        ValidationErrorType.Invalid_Request.name(),
                        "email",
                        "Invalid email format"
                    ));
                }
            }
            
        } catch (Exception e) {
            request.setValid(false);
            request.getErrors().add(new ErrorMsg(
                ValidationErrorType.Invalid_Request.name(),
                "general",
                "Error during validation: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Validate business rules for user info update (database-related checks)
     * 
     * @param userId User ID
     * @param request UserInfoUpdateRequest object
     */
    private void validateUserInfoBusinessRules(String userId, UserInfoUpdateRequest request) {
        try {
            // Check if user exists and fetch for reuse
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                request.setValid(false);
                request.getErrors().add(new ErrorMsg(
                    ValidationErrorType.Invalid_UserId.name(),
                    "userId",
                    "User not found with ID: " + userId
                ));
                return;
            }
            
            // Store the fetched user in request for reuse in performUserInfoUpdate
            request.setCurrentUser(userOpt.get());
            
            // Validate mobile doesn't exist for another user
            if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
                Optional<User> existingUser = userRepository.findByMobile(request.getMobile());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                    logger.warn("Update failed - mobile already exists: {}", request.getMobile());
                    request.setValid(false);
                    request.getErrors().add(new ErrorMsg(
                        ValidationErrorType.Invalid_Mobile.name(),
                        "mobile",
                        "Mobile number already registered with another user"
                    ));
                }
            }
            
            // Validate email doesn't exist for another user
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                    logger.warn("Update failed - email already exists: {}", request.getEmail());
                    request.setValid(false);
                    request.getErrors().add(new ErrorMsg(
                        ValidationErrorType.Email_Already_Present.name(),
                        "email",
                        "Email already registered with another user"
                    ));
                }
            }
            
        } catch (Exception e) {
            request.setValid(false);
            request.getErrors().add(new ErrorMsg(
                ValidationErrorType.Invalid_Request.name(),
                "general",
                "Database error during validation: " + e.getMessage()
            ));
        }
    }
    
    /**
     * Update user info (firstName, lastName, nickName, mobile, email)
     * Only updates fields that are provided (not null)
     * When email is updated, userName is also updated with the same value
     * 
     * @param userId User ID
     * @param request UserInfoUpdateRequest containing fields to update
     * @throws UserServiceException if update fails
     */
    public void updateUserInfo(String userId, UserInfoUpdateRequest request) throws UserServiceException {
        
        logger.info("Starting user info update for userId: {}", userId);
        
        try {
            request.setValid(true);
            
            // Validate structural errors (field-level validation)
            validateUserInfoStructuralErrors(userId, request);
            
            // If structural validation failed, throw exception
            if (!request.isValid()) {
                throw new ValidationException("Structural validation failed");
            }
            
            // Perform Authorization
            AuthReq authReq = new AuthReq(null, userId, PermissionType.UsrEdit);
            boolean hasPermission = authService.hasPersmission(authReq);
            
            if (!hasPermission) {
                request.setValid(false);
                request.getErrors().add(new ErrorMsg(
                    ValidationErrorType.Permission_Error.name(),
                    "authorization",
                    "Not authorized to update user info"
                ));
                throw new AuthServiceException("Invalid Permission");
            }
            
            // Validate business rules (database-related checks)
            validateUserInfoBusinessRules(userId, request);
            
            // If business validation failed, throw exception
            if (!request.isValid()) {
                throw new ValidationException("Business validation failed");
            }
            
            // Perform the actual update
            performUserInfoUpdate(userId, request);
            
            logger.info("User info update completed successfully for userId: {}", userId);
            
        } catch (ValidationException e) {
            throw new UserServiceException("Failed to update user info: " + e.getMessage());
        } catch (AuthServiceException e) {
            logger.error("Authorization failed for user: {}", userId, e);
            throw new UserServiceException("Not authorized to update user info: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating user info for user: {}", userId, e);
            request.setValid(false);
            request.setErrorMessage("Failed to update user info: " + e.getMessage());
            throw new UserServiceException("Failed to update user info: " + e.getMessage(), e);
        }
    }
    
    /**
     * Perform the actual user info update operation
     * 
     * @param userId User ID
     * @param request UserInfoUpdateRequest containing fields to update
     * @throws UserServiceException if update fails
     */
    private void performUserInfoUpdate(String userId, UserInfoUpdateRequest request) throws UserServiceException {
        try {
            // Use the user object already fetched during validation to avoid additional DB call
            User currentUser = request.getCurrentUser();
            if (currentUser == null) {
                throw new UserServiceException("User data not found in request. Validation may have failed.");
            }
            
            // Prepare values for update - use new values if provided, otherwise keep current values
            String firstName = request.getFirstName() != null ? request.getFirstName().trim() : currentUser.getFirstName();
            String lastName = request.getLastName() != null ? request.getLastName().trim() : currentUser.getLastName();
            String nickName = request.getNickName() != null ? request.getNickName().trim() : currentUser.getNickName();
            
            // Handle mobile update
            String mobile = currentUser.getMobile();
            Long mobileBindTime = currentUser.getMobileBindTime();
            if (request.getMobile() != null && !request.getMobile().trim().isEmpty()) {
                mobile = request.getMobile().trim();
                mobileBindTime = System.currentTimeMillis();
            }
            
            // Handle email update - when email is updated, userName is also updated with the same value
            String email = currentUser.getEmail();
            String userName = currentUser.getUserName();
            Long emailBindTime = currentUser.getEmailBindTime();
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                email = request.getEmail().trim();
                userName = email; // Update userName with email value
                emailBindTime = System.currentTimeMillis();
            }
            
            // Update using repository method
            userRepository.updateUserInfo(userId, firstName, lastName, nickName, mobile, mobileBindTime, 
                                         email, userName, emailBindTime, userId, new Date());
            
        } catch (Exception e) {
            throw new UserServiceException("Failed to perform user info update: " + e.getMessage(), e);
        }
    }
    
}
