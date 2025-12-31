package com.anchor.app.event.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.anchor.app.enums.SequenceType;
import com.anchor.app.event.exceptions.EventServiceException;
import com.anchor.app.event.model.Event;
import com.anchor.app.event.model.EventLogReq;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.EventType;
import com.anchor.app.msg.enums.NatsSubjectType;
import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.service.AppUserService;
import com.anchor.app.msg.service.NatsService;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;
import com.anchor.app.util.JsonUtil;

@Service
public class EventService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private HelperBean helper;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private AppUserService appUserService; 

	public void addEvent(Event event) throws EventServiceException
	{
		try {
			String eventQueName = env.getEventQueueName();
			rabbitTemplate.convertAndSend(eventQueName, event);
			
		}
		catch(Exception e)
		{
			logger.error("Error in Adding Even to Hz Queue");
			throw new EventServiceException(e.getMessage(), e);
		}
		
	}
	
	public void addEventLogReqToQueue(EventLogReq eventLogReq) throws EventServiceException
	{
		try {
			String evnetLogQueueName = env.getEventLogQueueName();
			rabbitTemplate.convertAndSend(evnetLogQueueName, eventLogReq);
			
		}
		catch(Exception e)
		{
			logger.error("Error in Adding Even to Hz Queue");
			throw new EventServiceException(e.getMessage(), e);
		}
		
	}
	
	@Async
	public void AddMessageToChannelEvent(String actionUserID, Date actionDate, String chnlID, Message msg) throws EventServiceException
	{
		try {
			
			// Prepare Event Object
			Event event = new Event();
			int id = helper.getIntegerSequanceNo(SequenceType.EVENT);
			event.setEventId(id);
			event.setType(EventType.Chnl_Add_Msg);
			event.setEntity(EventEntityType.Channel);
			event.setEntityId(chnlID);
			event.setAuthor(actionUserID);
			event.setMessage(msg);
			event.setActionDate(actionDate);
			
			// Send Event to Queue for Processing
			addEvent(event);
			
		}
		catch(Exception e)
		{
			logger.error("Error in Adding Even to Hz Queue");
			throw new EventServiceException(e.getMessage(), e);
		}
	}
	
	
	
	public void ProcessIncomingEvent(Event event) throws EventServiceException
	{
		try {
		
			if( null != event)
			{
				if( null != event.getType())
				{
					switch (event.getType()) 
					{
					case User_Status:
						appUserService.updateUserLoginStatus(event.getEntityId());
					break;
					
					default:
						break;	
					}
					
					}
			}
			
		}
		catch(Exception e )
		{
			throw new EventServiceException(e.getMessage(), e);
		}
	}

}
