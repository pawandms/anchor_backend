package com.anchor.app.users.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserLanguageType {

    English("English"),
    Hindi("Hindi");

    private String value;
    private static Map<String, UserLanguageType> map = new HashMap<String, UserLanguageType>();

    private UserLanguageType(String value) {
        this.value = value;
    }

    static {
        for (UserLanguageType type : UserLanguageType.values()) {
            map.put(type.value, type);
        }
    }

    public static UserLanguageType getType(String type) {
        return (UserLanguageType) map.get(type);
    }

    public String getValue() {
        return value;
    }

}
