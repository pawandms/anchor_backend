package com.anchor.app.users.dto;

import java.util.Date;

import com.anchor.app.dto.BaseVo;
import com.anchor.app.oauth.enums.GenderType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.anchor.app.users.enums.UserRoleType;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest extends BaseVo {
    
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    private String userName;
    private String mobile;

    private UserRoleType role;
    
    //Optional 
    private VisibilityType profileType;
    
    //Optional 
    private GenderType gender;
    
    //Optional 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob;

    // Response fields (populated by service)
    private String userId;
    private String verificationToken;
    
}
