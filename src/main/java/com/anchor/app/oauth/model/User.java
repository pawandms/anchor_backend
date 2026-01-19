package com.anchor.app.oauth.model;

import java.io.Serializable;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.anchor.app.oauth.enums.GenderType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.anchor.app.users.dto.UserNotification;
import com.anchor.app.users.dto.UserPrivacy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@TextIndexed 
    private String firstName;
    
    @TextIndexed 
    private String lastName;
    

    //User name
    @Field("userName")
	private String userName;
    //User nickname
    private String nickName;
    //Is it super administrator
    private boolean admin;
    //Gender
    private GenderType gender;
    //Birthday
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob;
    //Personal signature
    private String signature;
    //email
    private String email;
    //email
    private Long emailBindTime;
    //mobile
    private String mobile;
    //mobile
    private Long mobileBindTime;
    //Head portrait
    private String face;
    //Head portrait200*200
    private String face200;
    //Original image
    private String profileImageMediaId;
    //State 2 normal user 3 forbidden user 4 virtual user 5 operation
    private Integer status;
    
     // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
    
    private VisibilityType profileType;
    private Date lastLogin;
    
    private Integer type;
	
	
	private String about;
    private String publicKey;
    private UserPrivacy privacySettings;
    private UserNotification notificationSettings;
    private Boolean isTwoStepVerificationEnabled;
   

	

	
}
