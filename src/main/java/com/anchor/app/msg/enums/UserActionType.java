package com.anchor.app.msg.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserActionType {

	Change_ProfileType("Change_ProfileType"),
	Add_Friend_Request("Add_Friend_Request"),
	Add_Friend_Response("Add_Friend_Response"),
	Remove_Friend_Request("Add_Friend_Request"),
	Event_Action_Response("User_Action_Response"),
	Block_User_Request("Block_User_Request"),
	Search_Users("Search Users"),
	Create_User("Create_User"),
	Create_Msg_Chnl("Create_Msg_Chnl"),
	Get_User_Notification("Get_User_Notification"),
	Add_Msg_Reaction("Add_Msg_Reaction"),

    // ActionLog Events
    Add_User_Connection("Add_User_Connection"),
    Add_Friend_Req_Sent("Add_Friend_Req_Sent"),
    Add_Friend_Req_Receive("Add_Friend_Req_Receive"),
	;
	
	
	private String value;
    private static Map<String, UserActionType> map = new HashMap<String, UserActionType>();

	
    private UserActionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (UserActionType type : UserActionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserActionType getType(String type) {
        return (UserActionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
