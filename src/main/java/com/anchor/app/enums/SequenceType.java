package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum SequenceType {

	GENRE("GENRE"),
	IMAGE("IMAGE"),
	PERSON("PERSON"),
	COLLECTION("COLLECTION"),
	COMPANY("COMPANY"),
	MEDIA("MEDIA"),
	YOUTUBE_MEDIA("YOUTUBE_MEDIA"),
	LANGUAGE("LANGUAGE"),
	COUNTRY("COUNTRY"),
	CAST_CREW("CAST_CREW"),
	IMPORT_REQUEST("IMPORT_REQUEST"),
	USER("USER"),
	CLIENT("CLIENT"),
	USERAUTH("USERAUTH"),
	METADATA("METADATA"),
	UserVerifyToken("UserVerifyToken"),
	Channel("Channel"),
	ChannelParticipant("ChannelParticipant"),
	Message("Message"),
	MsgBoxRelation("MsgBoxRelation"),
	ChnlMsgRelation("ChnlMsgRelation"),
	EVENT("Event"),
	MsgAttachment("MsgAttachment"),
	ProfileImage("ProfileImage"),
	EventNotification("EventNotification"),
	UserConnection("UserConnection"),
	EventLog("EventLog"),
	ActionLog("ActionLog"),
	UUID("UUID"),

	MsgChannel("MsgChannel"),
	MsgChannelUser("MsgChannelUser"),
	;
	
	
	private String value;
    private static Map<String, SequenceType> map = new HashMap<String, SequenceType>();

	
    private SequenceType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (SequenceType type : SequenceType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static SequenceType getType(String type) {
        return (SequenceType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
