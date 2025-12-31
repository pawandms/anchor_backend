package com.anchor.app.msg.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.anchor.app.msg.model.Item;
import com.anchor.app.msg.repository.ItemRepository;

@Component
public class ItemCache {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ItemRepository itemRep;
	
	 @Cacheable(value="itemCache", key="#id")
	    public Item getItem(String id){
	      logger.info("In get Item Cache component...");
	        Item item = null;
	        try{
	           Optional<Item> itemopt = itemRep.findById(id);
	           if(itemopt.isPresent())
	           {
	        	   item = itemopt.get();
	           }
	            Thread.sleep(2000);
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return item;
	    }

	    @CacheEvict(value="itemCache",key = "#id")
	    public int deleteItem(String id){
	    	int result = 0;
	    	logger.info("In delete ItemCache Component..");
	        itemRep.deleteById(id);
	        result = 1;
	        
	        return result;
	    }

	    @CachePut(value="itemCache")
	    public void updateItem(Item item){
	    	logger.info("In Update ItemCache Component..");
	        itemRep.save(item);
	    }
}
