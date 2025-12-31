package com.anchor.app.action.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserOperationType {

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
	;


	private String value;
    private static Map<String, UserOperationType> map = new HashMap<String, UserOperationType>();


    private UserOperationType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (UserOperationType type : UserOperationType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static UserOperationType getType(String type) {
        return (UserOperationType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
