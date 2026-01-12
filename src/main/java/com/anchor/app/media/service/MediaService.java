package com.anchor.app.media.service;

import com.anchor.app.dto.ErrorMsg;
import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.media.dto.ImageInfo;
import com.anchor.app.media.dto.StreamMediaInfo;
import com.anchor.app.media.enums.MediaEntityType;
import com.anchor.app.media.enums.MediaType;
import com.anchor.app.media.exceptions.MediaServiceException;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.repository.MediaRepository;
import com.anchor.app.oauth.dto.AuthReq;
import com.anchor.app.oauth.enums.PermissionType;
import com.anchor.app.oauth.exceptions.AuthServiceException;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;
import com.anchor.app.util.enums.SequenceType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
    
    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);
    
    @Autowired
    private EnvProp envProp;
    
    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private HelperBean helperBean;

    @Autowired
    private StorageService storageService;

    @Autowired
	private AuthService authService;


    /**
     * Upload user profile image to MinIO
     * 
     * @param file MultipartFile to upload
     * @param userId User ID
     * @param uploadedBy User who uploaded
     * @return Media object with metadata
     */
    public Media saveUserProfileImage( String userId, String mediaID, MultipartFile file ) 
            throws MediaServiceException {
        
        logger.info("Uploading profile image for user: {}", userId);
       
        Media result = null;
        try{

           MediaEntityType   entityType = MediaEntityType.USER_PROFILE;
           
            if( !helperBean.isEmptyString(mediaID))
            {
                // Check MediaID Present in Media Collection
             Optional<Media> mediaOptional = mediaRepository.findByEntityTypeAndEntityId(entityType, mediaID);
             
             if(mediaOptional.isEmpty())
             {
                throw new MediaServiceException("Invalid MediaID"+mediaID+", With Type:"+entityType); 
             }
             result = mediaOptional.get();  
               
            }
            
             String bucketName = envProp.getUserProfileBucketName();
             
              // Generate unique file name
                String originalFilename = file.getOriginalFilename();

                
                if( null == result)
                {
                    String Id = helperBean.getSequanceNo(SequenceType.Media);
                    ImageInfo imageInfo = helperBean.getImageInfo(originalFilename, file.getInputStream());
                    MediaType mediaType = helperBean.getMediaTypeByExtension(imageInfo.getExtn());
                    String contentKey = Id+"."+imageInfo.getExtn().toLowerCase();
                
                // Store into Storage    
                storageService.putObject(contentKey, bucketName, file.getInputStream()); 

                // Create and save media metadata
                result = Media.builder()
                .id(Id)
                .userId(userId)
                .entityType(entityType)
                .entityId(Id)
                .uploadedBy(userId)
                .type(mediaType)
                .fileName(originalFilename)
                .name(imageInfo.getName())
                .extension(imageInfo.getExtn())
                .fileSize(file.getSize())
                .width(imageInfo.getWidth())
                .height(imageInfo.getHeight())
                .uploadedAt(new Date())
                .s3Bucket(bucketName)
                .contentKey(contentKey)
                .isDeleted(false)
                .crUser(userId)
                .crDate(new Date())
                .build();
      
                 result = mediaRepository.save(result);       
                }
                else {
                     // Store into Storage    
                storageService.putObject(result.getContentKey(), bucketName, file.getInputStream()); 

                }
                
             
        
        }
        catch(Exception e)
        {
            throw new MediaServiceException(e.getMessage(), e);
        }

        
        return result;
    }
    
    
    public void getUserProfileByMediaId(StreamMediaInfo req) throws MediaServiceException
    {
     
        try{
            req.setValid(true);
            if( null == req.getMediaId())
            {
                req.setValid(false);
                 req.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Media_ID.name(), "mediaId", req.getMediaId()));
                 throw new MediaServiceException("Invalid media ID:"+req.getMediaId());
            }   

            List<Media> mediaList   = mediaRepository.findAllByIdAndEntityType(req.getMediaId(), MediaEntityType.USER_PROFILE);
            if(mediaList.isEmpty())
            {
                req.setValid(false);
                 req.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Media_ID.name(), "mediaId", req.getMediaId()));
                 throw new MediaServiceException("Invalid media ID:"+req.getMediaId());
            }
              Media media = mediaList.get(0);
               req.setMediaType(media.getType()); 

            
               // Perform Authorization - reqUserID will be populated from authenticated user in AuthService
			AuthReq authReq = new AuthReq(null, media.getUserId(), PermissionType.CnView);

            boolean hasPermission = authService.hasPersmission(authReq);
		
		 if(!hasPermission)
		 {
			 throw new AuthServiceException("Invalid Permission");
		 }
         
               InputStream mediaStream = storageService.getContenStream(media.getContentKey(), media.getS3Bucket());
               
               if( null != mediaStream)
               {
                req.setMediaStream(mediaStream);
                req.setValid(true);

               }
                else {
                req.setValid(false);
                 req.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Media_ID.name(), "mediaId", req.getMediaId()));
                
                }   
            }
        catch(Exception e)
        {
            throw new MediaServiceException(e.getMessage(), e);
        }

    }
    
 
}    
