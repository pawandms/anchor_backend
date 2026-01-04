package com.anchor.app.enums;

import java.util.HashMap;
import java.util.Map;

public enum ValidationErrorType {
	Invalid_UserName("invalid username"),
	Invalid_Password("invalid password"),
	Invalid_Email("invalid email"),
	Invalid_FName("invalid first name"),
	Invalid_LName("invalid last name"),
	Invalid_Media_ID("invalid ImediaID"),
	Invalid_MetaData_Key("invalid MetadataKey"),
	Invalid_MetaData_Value("invalid MetadataValue"),
	Invalid_MetaData_RelationShip("invalid MetadataRelationship"),
	Invalid_Verificaiton_Token("invalid VerificationToken"),
	UserName_Already_Taken("Username already present"),
	Email_Already_Present("Email already present"),
	Invalid_Channel_Name("invalid ChannelName"),
	Invalid_Channel_Type("invalid ChannelType"),
	Invalid_Channel_Visibility("invalid ChannelVisibility"),
	Invalid_Channel_Subscription("invalid ChannelSubscription"),
	Invalid_Channel_Participant("invalid ChannelParticipant"),
	Invalid_Channel_ID("invalid ChannelID"),
	Invalid_Channel_User("ChannelUser"),
	Channel_Already_Present("Channel already present"),
	Channel_User_Already_Present("ChannelUser"),
	Invalid_Channel_Action("ChannelAction"),
	Invalid_Msg_ID("MessageID"),
	Invalid_Msg_Action("MessageAction"),
	Invalid_Msg_Type("MessageType"),
	Invalid_Msg_Content("MessageContent"),
	Invalid_Action_User("User"),
	Permission_Error("InvalidPermission"),
	Invalid_Request("Invalid_Request"),
	Add_User_Block("Add_User_Block"),
	;
	
	private String value;
    private static Map<String, ValidationErrorType> map = new HashMap<String, ValidationErrorType>();

	
    private ValidationErrorType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (ValidationErrorType type : ValidationErrorType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static ValidationErrorType getType(String type) {
        return (ValidationErrorType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
