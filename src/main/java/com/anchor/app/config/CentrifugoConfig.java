package com.anchor.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.anchor.app.notification.listner.CentrifugoEventListener;
import com.anchor.app.notification.service.UserPresenceService;
import com.anchor.app.util.EnvProp;

import io.github.centrifugal.centrifuge.Client;

@Configuration
public class CentrifugoConfig {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp envProp;

	@Autowired
	private UserPresenceService userPresenceService;

	private Client centrifugeClient;
	private CentrifugoEventListener eventListener;

}
