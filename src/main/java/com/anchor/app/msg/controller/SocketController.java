package com.anchor.app.msg.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anchor.app.event.model.Event;

//@Component
public class SocketController {
	
	/*
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private SocketIOServer socketServer;
    
    SocketController(SocketIOServer socketServer){
        this.socketServer=socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

        this.socketServer.addEventListener("messageSendToUser", Event.class, onSendMessage);

    }


    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
        	logger.info("Perform operation on user connect in controller");
        }
    };


    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
        	logger.info("Perform operation on user disconnect in controller");
        }
    };

    public DataListener<Event> onSendMessage = new DataListener<Event>() {
        @Override
        public void onData(SocketIOClient client, Event message, AckRequest acknowledge) throws Exception {

            
            // Sending message to target user target user should subscribe the socket event with his/her name.Send the same payload to user
             

        	logger.info(message.getSenderName()+" user send message to user "+message.getTargetUserName()+" and message is "+message.getMessage());
            socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(),client, message);


            //After sending message to target user we can send acknowledge to sender
            
            acknowledge.sendAckData("Message send to target user successfully");
        }
    };

    */

}
