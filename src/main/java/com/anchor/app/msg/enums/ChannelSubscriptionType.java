package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelSubscriptionType {

	Free("Free"),
	MonthlySubscription("Monthly Subscription"),
	DailySubscription("Daily Subscription"),
	PayPerView("Pay Per View"),
	;
	
	
	private String value;
    private static Map<String, ChannelSubscriptionType> map = new HashMap<String, ChannelSubscriptionType>();

	
    private ChannelSubscriptionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ChannelSubscriptionType type : ChannelSubscriptionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ChannelSubscriptionType getType(String type) {
        return (ChannelSubscriptionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
