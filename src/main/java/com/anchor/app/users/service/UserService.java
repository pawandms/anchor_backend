package com.anchor.app.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.dto.ErrorMsg;
import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.exceptions.UserServiceException;
import com.anchor.app.exceptions.ValidationException;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.service.MediaService;
import com.anchor.app.oauth.dto.AuthReq;
import com.anchor.app.oauth.enums.PermissionType;
import com.anchor.app.oauth.enums.UserIdentifyType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.anchor.app.oauth.exceptions.AuthServiceException;
import com.anchor.app.msg.service.ChannelService;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.model.UserAuth;
import com.anchor.app.oauth.model.UserVerifyToken;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.users.dto.SignUpRequest;
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
	private IAuthenticationFacade authfacade;

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
                    request.getErrors().add(new ErrorMsg("MOBILE_EXISTS", "mobile", request.getMobile()));
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
                .admin(false)
                .status(2) // Normal user
                .profileType(request.getProfileType())
                .isTwoStepVerificationEnabled(false)
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

    
    public String addUpdateUserProfileImage(String userID, MultipartFile profileImage) throws UserServiceException
    {
        String mediaId = null;
        try{

            // Perform Authorization
            User user = authfacade.getApiAuthenticationDetails();
        	if( null == user)
        	{
        		throw new UserServiceException("Invalid authenticated user");
        	}
        	
        	// perform Authorization
			AuthReq authReq = new AuthReq(user.getId(),userID, PermissionType.UsrEdit);

            boolean hasPermission = authService.hasPersmission(authReq);
		
		 if(!hasPermission)
		 {
			 throw new AuthServiceException("Invalid Perssion");
		 }
         
         mediaId = user.getProfileImageMediaId();

         Media media = mediaService.saveUserProfileImage(userID, mediaId, profileImage);

         // Update User Object for ProfileImageID
         updateProfileImageMediaId(userID, media.getId());

         mediaId = media.getId();
			
        }
        catch(Exception e)
        {
            throw new UserServiceException(e.getMessage(), e);
        }

        return mediaId;

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


    

    

    
}
