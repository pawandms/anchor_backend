package com.anchor.app.media.model;

import com.anchor.app.media.enums.MediaEntityType;
import com.anchor.app.media.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String userId;
    private MediaEntityType entityType;
    private String entityId;
    private String uploadedBy;
    private MediaType type;
    private Integer sequence;
    private String fileName;
    private String name;
    private String extension;
    private Long fileSize;
    private Integer duration;
    private Integer width;
    private Integer height;
    private String caption;
    private Date uploadedAt;
    private String s3Bucket;
    private String s3Region;
    private String contentKey;
    //private String s3VersionId;
    //private String storageClass;
    //private String cdnUrl;
    private String thumbnailContentKey;
    private Map<String, Object> s3Metadata;
    private Boolean isDeleted;
    private Date deletedAt;
    
    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
}
