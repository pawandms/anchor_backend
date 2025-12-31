package com.anchor.app.action.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.anchor.app.action.model.ActionLog;
import com.anchor.app.msg.enums.UserActionType;

public class ActionLogReq implements Serializable {

    private String reqUserID;
    private UserActionType actionType;
    private List<ActionLog> logs = new ArrayList<>();

    public String getReqUserID() {
        return reqUserID;
    }

    public void setReqUserID(String reqUserID) {
        this.reqUserID = reqUserID;
    }

    public UserActionType getActionType() {
        return actionType;
    }

    public void setActionType(UserActionType actionType) {
        this.actionType = actionType;
    }

    public List<ActionLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ActionLog> logs) {
        this.logs = logs;
    }
}
