package com.anchor.app.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.anchor.app.messaging.enums.ChannelPermission;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelSettings {
    
    private Boolean onlyAdminsCanPost;
    private Boolean onlyAdminsCanEdit;
    private Boolean membersCanAddOthers;
    private Boolean postPermission;
    private Boolean approveNewMembers;
    private ChannelPermission whoCanSeeMembers; // EVERYONE or ADMINS
    private Boolean allowComments;
    private Boolean allowLikes;
}
