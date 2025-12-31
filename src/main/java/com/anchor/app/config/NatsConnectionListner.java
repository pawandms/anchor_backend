package com.anchor.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.nats.client.Connection;
import io.nats.client.ConnectionListener;

public class NatsConnectionListner implements ConnectionListener {

	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Override
	public void connectionEvent(Connection conn, Events type) {
		// TODO Auto-generated method stub
		logger.info("Connect Event:"+type);
	}
	

}
