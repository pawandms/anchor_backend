package com.anchor.app.oauth.enums;

import java.util.HashMap;
import java.util.Map;

public enum PermissionType {

	CnAdd("Add content"),
	CnEdit("Edit content"),
	CnDelete("Delete content"),
	CnDeleteSelf("Delete content self"),
	CnView("View content"),
	CnApprove("Approve content"),
	UsrAdd("Add user"),
	UsrEdit("Edit user"),
	UsrDelete("Delete user"),
	UsrDeleteSelf("Delete user self"),
	UsrView("View user"),
	ChnlAdd("Add channel"),
	ChnlEdit("Edit channel"),
	ChnlDelete("Delete channel"),
	ChnlView("View channel"),
	;
	
	
	private String value;
    private static Map<String, PermissionType> map = new HashMap<String, PermissionType>();

	
    private PermissionType(String value )
    {
    this.value =value;	
    }
    
    static {
        for (PermissionType type : PermissionType.values()) {
            map.put(type.value, type);
        }
    }
    
    
    public static PermissionType getType(String type) {
        return (PermissionType) map.get(type);
    }

    
    public String getValue() {
        return value;
    }


}
