package com.anchor.app.users.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification implements Serializable {

    private Boolean msgNotification;
    private Boolean groupNotificaiton;
    private Boolean callNotificaiton;
    private Boolean statusNotification;


}
