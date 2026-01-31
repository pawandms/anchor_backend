package com.anchor.app.users.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserAvatarType {

    Avatar("Avatar"),
    Media("Media"),
    None("None");

    private String value;
    private static Map<String, UserAvatarType> map = new HashMap<String, UserAvatarType>();

    private UserAvatarType(String value) {
        this.value = value;
    }

    static {
        for (UserAvatarType type : UserAvatarType.values()) {
            map.put(type.value, type);
        }
    }

    public static UserAvatarType getType(String type) {
        return (UserAvatarType) map.get(type);
    }

    public String getValue() {
        return value;
    }

}
