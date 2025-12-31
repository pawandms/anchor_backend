package com.anchor.app.msg.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anchor.app.msg.model.Item;
import com.anchor.app.msg.service.impl.ItemCache;

@RestController
public class ItemController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	  @Autowired
	    ItemCache itemCache;
	    
	  @GetMapping("/item/{itemId}")
	    @ResponseBody
	    public ResponseEntity<Item> getItem(@PathVariable String itemId){
		  logger.info("getItem RestController..");
	        long start = System.currentTimeMillis();
	        Item item = itemCache.getItem(itemId);
	        long end = System.currentTimeMillis();
	        logger.info("getItem Took : " + ((end - start) / 1000+" sec."));
	        return new ResponseEntity<Item>(item, HttpStatus.OK);
	    }

	    @PutMapping("/updateItem")
	    @ResponseBody
	    public ResponseEntity<Item> updateItem(@RequestBody Item item){
	        if(item != null){
	            itemCache.updateItem(item);
	        }
	        return new ResponseEntity<Item>(item, HttpStatus.OK);
	    }

	    @DeleteMapping("/delete/{id}")
	    @ResponseBody
	    public ResponseEntity<Void> deleteItem(@PathVariable String id){
	        itemCache.deleteItem(id);
	        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	    }
}
