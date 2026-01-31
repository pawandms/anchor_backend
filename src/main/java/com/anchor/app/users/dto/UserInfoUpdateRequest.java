package com.anchor.app.users.dto;

import org.springframework.data.annotation.Transient;

import com.anchor.app.dto.BaseVo;
import com.anchor.app.oauth.model.User;
import com.anchor.app.users.enums.UserLanguageType;
import com.anchor.app.oauth.enums.GenderType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

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
public class UserInfoUpdateRequest extends BaseVo {

    private String firstName;
    private String lastName;
    private String nickName;
    private String mobile;
    private String email;
    private UserLanguageType userLanguage;
    private GenderType gender;
    private VisibilityType profileType;
    private Date dob;
    private double[] location;

    @Transient
    @JsonIgnore
    private User currentUser; // Fetched user object to avoid multiple DB calls
}
