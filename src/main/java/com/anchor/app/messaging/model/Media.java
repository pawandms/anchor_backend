package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.messaging.enums.MediaEntityType;
import com.anchor.app.messaging.enums.MediaType;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "media")
public class Media {
    
    @Id
    private String id;
    
    private String entityType; // MESSAGE, USER_PROFILE, CHANNEL_AVATAR, STATUS, CALL
    private String entityId;
    private String uploadedBy;
    private String type; // IMAGE, VIDEO, AUDIO, DOCUMENT, VOICE, PROFILE_IMAGE, THUMBNAIL
    private Integer sequence;
    private String fileName;
    private Long fileSize;
    private String mimeType;
    private Integer duration;
    private Integer width;
    private Integer height;
    private String caption;
    private Date uploadedAt;
    private String s3Bucket;
    private String s3Region;
    private String s3Key;
    private String s3VersionId;
    private String storageClass;
    private String cdnUrl;
    private String thumbnailS3Key;
    private Map<String, Object> s3Metadata;
    private Boolean isDeleted;
    private Date deletedAt;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
