package com.anchor.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.anchor.app.util.EnvProp;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.anchor")
@EnableMongoAuditing
public class MongoConfig {

    	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;
	
	@Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = null;
        try{
              final ConnectionString connectionString = new ConnectionString(env.getMongourl());
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        mongoClient =  MongoClients.create(mongoClientSettings);
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
      
        return mongoClient;
    }
    
    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
    
}
