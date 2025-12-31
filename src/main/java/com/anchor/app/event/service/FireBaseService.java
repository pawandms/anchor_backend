package com.anchor.app.event.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.event.exceptions.FireBaseServiceException;
import com.anchor.app.util.EnvProp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FireBaseService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private EnvProp env;

	@Autowired
	private FirebaseMessaging fcm;
	
	
	
	public String sendFcnNotification(String fcnToken, String msg) throws FireBaseServiceException
	{
		String msgId = null;
		try {
			
			Random random = new Random();
			int randomWithNextInt = random.nextInt();
			
			Notification fcmNotification = Notification.builder().setTitle("Spring-FCM-Title-"+randomWithNextInt)
					.setBody(msg)
					.build();
			
			  Message fcmMsg = Message.builder()
					  .setToken(fcnToken)
					  .setNotification(fcmNotification)
					  .build();
			  
			  
			  msgId =  fcm.send(fcmMsg);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new FireBaseServiceException(e.getMessage(), e);
		}
		
		return msgId;
	}
	  
	  
}
