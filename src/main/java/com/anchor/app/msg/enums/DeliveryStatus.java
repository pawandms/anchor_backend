package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum DeliveryStatus {

	SENT("SENT"),
	DELIVERED("DELIVERED"),
	READ("READ"),
	
	;
	
	
	private String value;
    private static Map<String, DeliveryStatus> map = new HashMap<String, DeliveryStatus>();

	
    private DeliveryStatus(String value )
    {
    this.value =value;	
    }
    
    static {
        for (DeliveryStatus type : DeliveryStatus.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static DeliveryStatus getType(String type) {
        return (DeliveryStatus) map.get(type);
    }

    
    public String getValue() {
        return value;
    }

}
