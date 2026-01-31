package com.anchor.app.msg.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.anchor.app.msg.model.ContactRequest;
import com.anchor.app.msg.enums.ResponseType;

import java.util.List;

@Repository
public interface ContactRequestRepository extends MongoRepository<ContactRequest, String> {

    /**
     * Find all contact requests involving the user with specific response types.
     * 
     * @param userId The user ID (either sender or receiver)
     * @param types  List of response types (e.g., PENDING, REJECTED)
     * @return List of matching ContactRequest objects
     */
    @Query("{ '$or': [ { 'fromUserId': ?0 }, { 'toUserId': ?0 } ], 'responseType': { '$in': ?1 } }")
    List<ContactRequest> findByUserIdAndResponseTypeIn(String userId, List<ResponseType> types);
}
