package com.anchor.app.msg.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum NatsSubjectType {

	Chnl_Add_User(EventType.Chnl_Add_User, "chnl.add.user"),
	Chnl_Remove_User(EventType.Chnl_Remove_User, "chnl.remove.user"),
	Chnl_Add_Msg(EventType.Chnl_Add_Msg, "chnl.add.msg"),
	Chnl_Remove_msg(EventType.Chnl_Remove_msg, "chnl.remove.msg"),
	Add_Friend(EventType.Add_Friend, "user.add.friend"),
	Remove_Friend(EventType.Remove_Friend, "user.remove.friend"),
	User_Status(EventType.User_Status, "user.status"),
	;
	 public final EventType eventType;
	 public final String subject;
	
	 private static final Map<EventType, NatsSubjectType> BY_EventType = new HashMap<>();
	 private static final Map<String, NatsSubjectType> BY_Subject = new HashMap<>();
	   
	
    private NatsSubjectType(EventType eventType, String subject )
    {
    this.eventType = eventType;
    this.subject = subject;
    }
    
    
    static {
        for (NatsSubjectType e : values()) {
        	BY_EventType.put(e.eventType, e);
        	BY_Subject.put(e.subject, e);
           
        }
    }
    

    public static NatsSubjectType byEventType(EventType type) {
        return BY_EventType.get(type);
    }

    public static NatsSubjectType bySuject(String subject ) {
        return BY_Subject.get(subject);
    }
    
    public static List<NatsSubjectType> getList()
    {
    	return Arrays.asList(NatsSubjectType.values());
    }
    
}
