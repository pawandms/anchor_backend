package com.anchor.app.msg.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.enums.ContactType;
import com.anchor.app.msg.model.UserContact;

@Repository
public interface UserContactRepository extends MongoRepository<UserContact, String> {

    boolean existsByUserIdAndContactIdAndContactType(String userId, String contactId, ContactType contactType);

}
