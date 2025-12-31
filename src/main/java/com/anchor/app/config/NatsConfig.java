package com.anchor.app.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anchor.app.util.EnvProp;
import com.corundumstudio.socketio.SocketIOServer;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;

/**
 * Commented as Currently Nats Server not in use
 * @author pawan
 *
 */
@Configuration
public class NatsConfig {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;
	
	private SocketIOServer server;
	
	@Bean
	public Connection  createNatsConnection()
	{
		logger.info("Nats Connection creation bean initilized....");
		Connection con = null;
		 try {
			 String serverUrl = env.getNatsServerUrl();
			 
			 Options options = new Options.Builder().
                     server(serverUrl).
                     connectionListener(new NatsConnectionListner()). // Set the listener
                     build();
			 con = Nats.connect(options);
			 
	         
	        } catch (IOException | InterruptedException e) {
	            
	        	logger.error("Error in Connecting Nats Server",e );
	        	 throw new RuntimeException(e.getMessage(), e);
	        }
		 
		 return con;
	}
	
	
	
	/*
	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		config.setHostname(env.getSocketServer());
		config.setPort(env.getSocketPort());
		server = new SocketIOServer(config);
		server.start();
		
		server.addConnectListener(new ConnectListener() {
			@Override
			public void onConnect(SocketIOClient client) {

				logger.info("new user connected with socket " + client.getSessionId());
			}
		});

		server.addDisconnectListener(new DisconnectListener() {
			@Override
			public void onDisconnect(SocketIOClient client) {
				client.getNamespace().getAllClients().stream().forEach(data-> {
					logger.info("user disconnected "+data.getSessionId().toString());});
			}
		});
		return server;
	}

	*/
}
