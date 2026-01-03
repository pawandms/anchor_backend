package com.anchor.app.media.service;

import com.anchor.app.media.dto.ImageInfo;
import com.anchor.app.media.enums.MediaEntityType;
import com.anchor.app.media.enums.MediaMimeType;
import com.anchor.app.media.enums.MediaType;
import com.anchor.app.media.exceptions.MediaServiceException;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.repository.MediaRepository;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;
import com.anchor.app.util.enums.SequenceType;

import io.minio.*;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaService {
    
    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);
    
    @Autowired
    private MinioClient minioClient;
    
    @Autowired
    private EnvProp envProp;
    
    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private HelperBean helperBean;

    @Autowired
    private StorageService storageService;
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
    
    
    
    
    
}
