package com.anchor.app.oauth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.users.enums.UserRoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("userAuth")
public class UserAuth implements Serializable {

    // id
    @Id
    private String id;
    // 1 mobile number 2 email 3 user name 4qq 5 wechat 6 Tencent Weibo 7 Sina Weibo
    private Integer identityType;
    // Mobile phone number, email address, user name or unique identification of
    // third-party application
    private String identifier;
    // Password Certificate (stored password in the station, not saved or saved
    // token outside the station)
    private String certificate;
    // MD5 salt value encryption
    private String md5;

    // Role ID
    private List<UserRoleType> roles;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    // Audit fields
    private String crUser;
    private Date crDate;
    private String modUser;
    private Date modDate;

}
