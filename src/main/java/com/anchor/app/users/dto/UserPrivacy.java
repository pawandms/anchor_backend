package com.anchor.app.users.dto;

import java.io.Serializable;

import com.anchor.app.users.enums.UserPrivacyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivacy implements Serializable {

    private UserPrivacyType lastSeen;
    private UserPrivacyType profileVisiblity;
    private UserPrivacyType statusVisibility;
    private boolean readReceipts;
    private UserPrivacyType callVisibility;


    

}
