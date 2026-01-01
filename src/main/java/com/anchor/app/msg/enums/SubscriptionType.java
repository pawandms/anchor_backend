package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum SubscriptionType {

	FREE("FREE"),
	MONTHLY_SUBSCRIPTION("MONTHLY_SUBSCRIPTION"),
	DAILY_SUBSCRIPTION("DAILY_SUBSCRIPTION"),
	PAY_PER_VIEW("PAY_PER_VIEW"),
	
	;
	
	
	private String value;
    private static Map<String, SubscriptionType> map = new HashMap<String, SubscriptionType>();

	
    private SubscriptionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (SubscriptionType type : SubscriptionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static SubscriptionType getType(String type) {
        return (SubscriptionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
