package com.anchor.app.oauth.service;

import com.anchor.app.oauth.enums.AuthClientType;
import com.anchor.app.oauth.model.OauthClientDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DatabaseRegisteredClientRepository implements RegisteredClientRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String CONLLECTION_NAME = "oauth_client_details";

    @Autowired
    MongoTemplate mongoTemplate;

   // private final ObjectMapper objectMapper;

    public DatabaseRegisteredClientRepository() {
       /// this.objectMapper = new ObjectMapper();
        //this.objectMapper.registerModule(new JavaTimeModule());
        //this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException("registeredClient not implemented");
       /* 
        AuthClientDetails entity = toEntity(registeredClient);
        repository.save(entity);
        */
    }

    @Override
    public RegisteredClient findById(String id) {

        RegisteredClient result =null;
        try {

        OauthClientDetails client = mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), OauthClientDetails.class, CONLLECTION_NAME);
        if(client==null){
            throw new RuntimeException("no client information found");
        }

        result = toRegisteredClient(client);
   
        } catch (Exception e) {
            // If it's not a valid integer, return null
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {

        RegisteredClient result = null;

        try{
             OauthClientDetails client = mongoTemplate.findOne(new Query(Criteria.where("clientId").is(clientId)), OauthClientDetails.class, CONLLECTION_NAME);
        if(client==null){
            throw new RuntimeException("no client information found");
        }

        result = toRegisteredClient(client);
   
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        
        return result;        
    }

     private RegisteredClient toRegisteredClient(OauthClientDetails entity) {
        // Convert comma-separated strings back to Sets
        final Set<ClientAuthenticationMethod> clientAuthenticationMethods;
        if (entity.getClientAuthenticationMethod() != null && !entity.getClientAuthenticationMethod().isEmpty()) {
            clientAuthenticationMethods = Arrays.stream(entity.getClientAuthenticationMethod().split(","))
                    .map(String::trim)
                    .map(ClientAuthenticationMethod::new)
                    .collect(Collectors.toSet());
        } else {
            clientAuthenticationMethods = new HashSet<>();
        }

        final Set<AuthorizationGrantType> authorizationGrantTypes;
        if (entity.getAuthorizedGrantTypes() != null && !entity.getAuthorizedGrantTypes().isEmpty()) {
            authorizationGrantTypes = Arrays.stream(entity.getAuthorizedGrantTypes().split(","))
                    .map(String::trim)
                    .map(AuthorizationGrantType::new)
                    .collect(Collectors.toSet());
        } else {
            authorizationGrantTypes = new HashSet<>();
        }

        final Set<String> redirectUris;
        if (entity.getRegisteredRedirectUris() != null && !entity.getRegisteredRedirectUris().isEmpty()) {
            redirectUris = Arrays.stream(entity.getRegisteredRedirectUris().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
        } else {
            redirectUris = new HashSet<>();
        }

        final Set<String> postLogoutRedirectUris;
        if (entity.getPostLogoutRedirectUris() != null && !entity.getPostLogoutRedirectUris().isEmpty()) {
            postLogoutRedirectUris = Arrays.stream(entity.getPostLogoutRedirectUris().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
        } else {
            postLogoutRedirectUris = new HashSet<>();
        }

        final Set<String> scopes;
        if (entity.getScope() != null && !entity.getScope().isEmpty()) {
            scopes = Arrays.stream(entity.getScope().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
        } else {
            scopes = new HashSet<>();
        }

        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId().toString())
                .clientId(entity.getClientId())
                .clientIdIssuedAt(entity.getClientIdIssuedAt())
                .clientSecret(entity.getClientSecret())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
                .clientName(entity.getClientName())
                .clientAuthenticationMethods(methods -> methods.addAll(clientAuthenticationMethods))
                .authorizationGrantTypes(grantTypes -> grantTypes.addAll(authorizationGrantTypes))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
                .scopes(scopeSet -> scopeSet.addAll(scopes));

        // Rebuild settings from individual fields
        // Add External or Internal Client Setting
        String clientType = AuthClientType.Internal.getValue();
         if(entity.getClientType() != null )
         {
            clientType = entity.getClientType();
         }
       
        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(entity.getRequireProofKey() != null ? entity.getRequireProofKey() : false)
                .requireAuthorizationConsent(entity.getRequireAuthorizationConsent() != null ? entity.getRequireAuthorizationConsent() : false)
                .setting("clientType", clientType)
                .build();

        builder.clientSettings(clientSettings);
                 

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(java.time.Duration.ofSeconds(entity.getAccessTokenValiditySeconds() != null ? entity.getAccessTokenValiditySeconds() : 3600))
                .refreshTokenTimeToLive(java.time.Duration.ofSeconds(entity.getRefreshTokenValiditySeconds() != null ? entity.getRefreshTokenValiditySeconds() : 604800))
                .build();
        builder.tokenSettings(tokenSettings);

        return builder.build();
    }

    
}
