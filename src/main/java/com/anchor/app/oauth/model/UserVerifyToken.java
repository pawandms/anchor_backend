package com.anchor.app.oauth.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection= "userVerifyToken")
public class UserVerifyToken implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 882016696573227002L;

	@Id
	private String id;
	private String userName;
	private String verifyToken;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private boolean valid;
   
	// Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;
	

}
