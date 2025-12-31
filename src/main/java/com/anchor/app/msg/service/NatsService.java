package com.anchor.app.msg.service;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.event.exceptions.EventServiceException;
import com.anchor.app.event.model.Event;
import com.anchor.app.event.service.EventService;
import com.anchor.app.msg.enums.EventType;
import com.anchor.app.msg.enums.NatsSubjectType;
import com.anchor.app.msg.exceptions.NatsServiceException;
import com.anchor.app.util.JsonUtil;

import io.nats.client.Connection;
import io.nats.client.Connection.Status;
import io.nats.client.Dispatcher;

@Service
public class NatsService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Connection nc;
	
	private Dispatcher dispatcher;

	@Autowired
	private EventService eventService;
	
	  @PostConstruct
	    void postConstruct() throws NatsServiceException 
	  {
	        createNatsDispatcher();
	        logger.info("Nats Service post construction done...");
	        
	  }
	 
	  
		private void createNatsDispatcher() throws NatsServiceException
		{
			try {
				
				
				// Create Nats Dispatcher
				dispatcher = nc.createDispatcher(msg -> {
		        });
				
				// Subscript Application for User_Status Event
				 
				NatsSubjectType userStatus = NatsSubjectType.User_Status;
				subscribeToEvent(userStatus, dispatcher);
				
				// Subscribe to Nats SubjectType
				/* 
				NatsSubjectType.getList().forEach(event -> {
		    		try {
		    			subscribeToEvent(event, dispatcher);	
		    		}
		    		catch(Exception e)
		    		{
		    			throw new RuntimeException(e.getMessage(), e);
		    		}
	    			
	    		 });
				*/
			}
			catch(Exception e)
			{
				throw new NatsServiceException(e.getMessage(), e);
			}
		}
	
		private void subscribeToEvent(NatsSubjectType subType, Dispatcher dispatcher) throws NatsServiceException
		{
			try {
				String subject = subType.subject+".*";
				dispatcher.subscribe(subject, msg ->{
					try {
						String incommingMsg = new String(msg.getData());
						Event event = (Event) JsonUtil.getInstance().json2Pojo(incommingMsg, new Event());
						
						eventService.ProcessIncomingEvent(event);
						
					}
					catch(Exception e)
					{
						logger.error(e.getMessage(), e);
						throw new RuntimeException(e.getMessage(), e);
					}
					
				});
			}
			catch(Exception e)
			{
				throw new NatsServiceException(e.getMessage(), e);
			}
		}
	
		
		
	public void sendMessage(String subject, String msg) throws NatsServiceException
	{
		try {
			
			Status sts = nc.getStatus();
			if(sts.equals(Status.CONNECTED))
			{
				nc.publish(subject, msg.getBytes(StandardCharsets.UTF_8));
			}
			else {
				throw new NatsServiceException("Current Connection status is:"+sts.name());
			}
		}
		catch(Exception e)
		{
			throw new NatsServiceException(e.getMessage(), e);
		}
	}


	public void sendEvent(Event event) throws EventServiceException
	{
		try {
			
			if( null != event)
			{
				// Generate Event Subject 
				String subject = generateSubjectForEvent(event);
				String payload = JsonUtil.getInstance().pojo2Json(event);
				sendMessage(subject, payload);
				
				
			}
			
		}
		catch(Exception e)
		{
			logger.error("Error in Adding Even to Hz Queue");
			throw new EventServiceException(e.getMessage(), e);
		}
	}

	private String generateSubjectForEvent(Event event) throws EventServiceException
	{
		String result = null;
		
		try {
			
			// Get Nats Subject type for Respective Event
			
			NatsSubjectType subType = NatsSubjectType.byEventType(event.getType());
			
			if( null == subType)
			{
				throw new EventServiceException("Nat SubType not found for EventType:"+event.getType());
				
			}
			
			result = subType.subject+"."+event.getEntityId();
			
		}
		catch(Exception e)
		{
			throw new EventServiceException(e.getMessage(), e);
		}
		
		return result;
	}


}
