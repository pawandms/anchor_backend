package com.anchor.app.cache.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.exceptions.CacheServiceException;
import com.anchor.app.oauth.model.UserVerifyToken;
import com.anchor.app.util.EnvProp;


@Service
public class CacheServiceImpl implements CacheService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//@Autowired
	//private HazelcastInstance hazelcastInstance;
	
	@Autowired
	private EnvProp env;
	
	private String emailConfirmationQueueName;

	/* 
	@Autowired
	private RabbitTemplate rabbitTemplate;
	*/
	
		
	/**
	 * Create RabbitMQ queue Bean
	 * @return
	 */
	/*
	@Bean
	Queue queue() {
		
		return new Queue(emailConfirmationQueueName, true);
	}
	*/
	
	
	@Override
	public void sendSingupVerificationEmailMsg(UserVerifyToken confirmationMsg) throws CacheServiceException {
		// TODO Auto-generated method stub
		
		logger.info("Sending Message to RabbitMQ");
		
		try {
			// Send Message to RabbitMq
			//rabbitTemplate.convertAndSend(emailConfirmationQueueName, confirmationMsg);	
			
			logger.info("Disable Email Sending for Email Configuraiton for Payload:"+confirmationMsg.toString());
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new CacheServiceException(e.getMessage(), e);
		}
		
		
	
		
	}


}
