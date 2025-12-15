package com.anchor.app.oauth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.anchor.app.exceptions.AuthServiceException;
import com.anchor.app.oauth.model.UserAuth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseUserDetailsService implements UserDetailsManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String USER_CONLLECTION = "userAuth";
    @Autowired
    MongoTemplate mongoTemplate;


    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      //Identifier: 1 mobile number 2 email 3 user name 4qq 5 wechat 6 Tencent Weibo 7 Sina Weibo
        UserAuth userAuth = mongoTemplate.findOne(new Query(Criteria.where("identifier").is(username)), UserAuth.class, USER_CONLLECTION);
        if(userAuth == null) {
            throw new RuntimeException ("no user information is found");
        }
        User user=  new User(username, userAuth.getCertificate(), userAuth.isEnabled(), userAuth.isAccountNonExpired(), 
        		userAuth.isCredentialsNonExpired(), userAuth.isAccountNonLocked(), mapToGrantedAuthorities(userAuth.getRoles()));
       
        return user;
    }

    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("Create user not implemented");
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Update user not implemented");
    }

    @Override
    public void deleteUser(String username) {
       throw new UnsupportedOperationException("Delete user not implemented");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // This would require current authentication context
        throw new UnsupportedOperationException("Change password not implemented");
    }

    

    
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

        @Override
    public boolean userExists(String username) 
    {
        boolean result = false;
        try {
            UserAuth userAuth = mongoTemplate.findOne(new Query(Criteria.where("identifier").is(username)), UserAuth.class, USER_CONLLECTION);
            if(userAuth != null) 
            {
            result = true;
            }

        } catch (Exception e) {
            logger.error("Error checking if user exists: " + e.getMessage(), e);
        }
       return result;
    }



    
}
