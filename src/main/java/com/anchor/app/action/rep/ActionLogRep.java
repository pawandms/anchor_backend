package com.anchor.app.action.rep;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.action.model.ActionLog;

@Repository
public interface ActionLogRep extends MongoRepository<ActionLog, String>
{



}
