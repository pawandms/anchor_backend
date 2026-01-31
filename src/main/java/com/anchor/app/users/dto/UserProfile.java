package com.anchor.app.users.dto;

import java.util.Date;
import com.anchor.app.dto.BaseVo;
import com.anchor.app.oauth.enums.GenderType;
import com.anchor.app.oauth.enums.UserStatusType;
import com.anchor.app.oauth.enums.VisibilityType;
import com.anchor.app.oauth.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.anchor.app.users.dto.UserPrivacy;
import com.anchor.app.users.enums.UserLanguageType; // [NEW]
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for User Profile
 * This DTO allows for flexible field management and separation from the domain
 * model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfile extends BaseVo {
    private static final long serialVersionUID = 1L;

    private String id;
    private String firstName;
    private String lastName;
    private String userName;
    private String nickName;
    private String email;
    private String mobile;
    private GenderType gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob;

    private String signature;
    private String about;
    private String face;
    private String face200;
    private String profileImageMediaId;
    private VisibilityType profileType;
    private UserStatusType status;
    private Date lastLogin;
    private Date crDate;
    private UserPrivacy privacySettings;
    private UserNotification notificationSettings;
    private Boolean isTwoStepVerificationEnabled;
    private UserLanguageType userLanguage;
    private double[] location;

    /**
     * Convert User entity to UserProfileDTO
     * 
     * @param user User entity
     * @return UserProfileDTO
     */
    public static UserProfile fromUser(User user) {
        if (user == null) {
            return null;
        }

        return UserProfile.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .gender(user.getGender())
                .dob(user.getDob())
                .signature(user.getSignature())
                .about(user.getAbout())
                .face(user.getFace())
                .face200(user.getFace200())
                .profileImageMediaId(user.getProfileImageMediaId())
                .location(user.getLocation())
                .profileType(user.getProfileType())
                .status(user.getStatus() != null ? UserStatusType.valueOf(user.getStatus()) : null)
                .lastLogin(user.getLastLogin())
                .crDate(user.getCrDate())
                .privacySettings(user.getPrivacySettings())
                .notificationSettings(user.getNotificationSettings())
                .isTwoStepVerificationEnabled(user.getIsTwoStepVerificationEnabled())
                .userLanguage(user.getUserLanguage() != null ? user.getUserLanguage()
                        : com.anchor.app.users.enums.UserLanguageType.English)
                .location(user.getLocation())
                .build();
    }
}
