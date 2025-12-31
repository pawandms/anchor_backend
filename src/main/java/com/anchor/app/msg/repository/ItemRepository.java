package com.anchor.app.msg.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anchor.app.msg.model.Item;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

}
