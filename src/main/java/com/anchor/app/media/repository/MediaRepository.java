package com.anchor.app.media.repository;

import com.anchor.app.media.enums.MediaEntityType;
import com.anchor.app.media.model.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends MongoRepository<Media, String> {
    
    /**
     * Find media by entity type and entity ID
     */
    Optional<Media> findByEntityTypeAndEntityId(MediaEntityType entityType, String entityId);
    
    /**
     * Find all media for an entity
     */
    List<Media> findAllByEntityTypeAndEntityId(MediaEntityType entityType, String entityId);
    
    /**
     * Find all non-deleted media for an entity
     */
    List<Media> findAllByEntityTypeAndEntityIdAndIsDeletedFalse(MediaEntityType entityType, String entityId);
    
    /**
     * Delete media by entity type and entity ID
     */
    void deleteByEntityTypeAndEntityId(MediaEntityType entityType, String entityId);

    List<Media> findByContentKey(String contentKey);

     List<Media> findAllByIdAndEntityType(String id , MediaEntityType entityType );
   
}
