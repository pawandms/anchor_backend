package com.anchor.app.notification.listner;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anchor.app.notification.service.UserPresenceService;

import io.github.centrifugal.centrifuge.Client;
import io.github.centrifugal.centrifuge.ConnectedEvent;
import io.github.centrifugal.centrifuge.ConnectingEvent;
import io.github.centrifugal.centrifuge.DisconnectedEvent;
import io.github.centrifugal.centrifuge.ErrorEvent;
import io.github.centrifugal.centrifuge.EventListener;
import io.github.centrifugal.centrifuge.MessageEvent;
import io.github.centrifugal.centrifuge.ServerJoinEvent;
import io.github.centrifugal.centrifuge.ServerLeaveEvent;
import io.github.centrifugal.centrifuge.ServerPublicationEvent;
import io.github.centrifugal.centrifuge.ServerSubscribedEvent;
import io.github.centrifugal.centrifuge.ServerSubscribingEvent;
import io.github.centrifugal.centrifuge.ServerUnsubscribedEvent;

public class CentrifugoEventListener extends EventListener  {

     private final Logger logger = LoggerFactory.getLogger(this.getClass());
     private UserPresenceService userPresenceService;
     
     public CentrifugoEventListener() {
     }
     
     public void setUserPresenceService(UserPresenceService userPresenceService) {
         this.userPresenceService = userPresenceService;
     }

    @Override
    public void onConnected(Client client, ConnectedEvent event) {
        logger.info("Event connected with client id {}", event.getClient());
    }

    @Override
    public void onConnecting(Client client, ConnectingEvent event) {

         logger.info("Event  connecting: {}", event.getReason());
    }

    @Override
    public void onDisconnected(Client client, DisconnectedEvent event) {
         logger.info("Event disconnected - code: {}, reason: {}", event.getCode(), event.getReason());
    }

    @Override
    public void onError(Client client, ErrorEvent event) {
        logger.error("Event connection error: {}", event.getError().toString());
    }

    @Override
    public void onJoin(Client client, ServerJoinEvent event) {
         logger.info("Event  onJoin Event - Channel: {}, User: {}, Client: {}", 
             event.getChannel(), event.getInfo().getUser(), event.getInfo().getClient());
         
         // Track user presence for user:status channel
         if ("user:status".equals(event.getChannel()) && userPresenceService != null) {
             String userId = event.getInfo().getUser();
             String clientId = event.getInfo().getClient();
             userPresenceService.userJoined(userId, clientId);
         }
    }

    @Override
    public void onLeave(Client client, ServerLeaveEvent event) {
         logger.info("Event  onLeave Event - Channel: {}, User: {}, Client: {}", 
             event.getChannel(), event.getInfo().getUser(), event.getInfo().getClient());
         
         // Track user presence for user:status channel
         if ("user:status".equals(event.getChannel()) && userPresenceService != null) {
             String userId = event.getInfo().getUser();
             String clientId = event.getInfo().getClient();
             userPresenceService.userLeft(userId, clientId);
         }
    }

    @Override
    public void onMessage(Client client, MessageEvent event) {
         String data = new String(event.getData(), StandardCharsets.UTF_8);
        logger.info("Event  message received: " + data+", From Client:"+client.toString());
    }

    @Override
    public void onPublication(Client client, ServerPublicationEvent event) {
           logger.info("Event  onPublication Event: "+event.getChannel());
    }

    @Override
    public void onSubscribed(Client client, ServerSubscribedEvent event) {
          logger.info("Event  onSubscribed Event: "+event.getChannel());
    }

    @Override
    public void onSubscribing(Client client, ServerSubscribingEvent event) {
        logger.info("Event  onSubscribing Event: "+event.getChannel());
    }

    @Override
    public void onUnsubscribed(Client client, ServerUnsubscribedEvent event) {
        logger.info("Event  onUnsubscribed Event: "+event.getChannel());
    }
    

}
