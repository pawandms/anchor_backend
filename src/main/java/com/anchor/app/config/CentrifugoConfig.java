package com.anchor.app.config;

import java.util.Base64;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.anchor.app.notification.listner.CentrifugoEventListener;
import com.anchor.app.notification.listner.CentrifugoSubscriptionEventListener;
import com.anchor.app.notification.service.UserPresenceService;
import com.anchor.app.oauth.exceptions.AuthServiceException;
import com.anchor.app.util.EnvProp;

import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.ConnectionTokenEvent;
import io.github.centrifugal.centrifuge.ConnectionTokenGetter;
import io.github.centrifugal.centrifuge.Options;
import io.github.centrifugal.centrifuge.Subscription;
import io.github.centrifugal.centrifuge.SubscriptionEventListener;
import io.github.centrifugal.centrifuge.SubscriptionOptions;
import io.github.centrifugal.centrifuge.TokenCallback;

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
