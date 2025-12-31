package com.anchor.app.action.model;

import com.anchor.app.event.model.Meta;
import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.UserActionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Document(collection= "actionLog")
public class ActionLog implements Serializable {

    @Id
    private String id;

    private UserActionType actionType;
    private EventEntityType srcType;
    private String srcKey;
    private EventEntityType trgType;
    private String trgUserID;
    private String trgChnlID;
    private String trgMsgID;
    private Meta attributes;
    private boolean isUserNotification;
    private boolean isRead;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crDate = new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modDate = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserActionType getActionType() {
        return actionType;
    }

    public void setActionType(UserActionType actionType) {
        this.actionType = actionType;
    }

    public EventEntityType getSrcType() {
        return srcType;
    }

    public void setSrcType(EventEntityType srcType) {
        this.srcType = srcType;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public void setSrcKey(String srcKey) {
        this.srcKey = srcKey;
    }

    public EventEntityType getTrgType() {
        return trgType;
    }

    public void setTrgType(EventEntityType trgType) {
        this.trgType = trgType;
    }

    public String getTrgUserID() {
        return trgUserID;
    }

    public void setTrgUserID(String trgUserID) {
        this.trgUserID = trgUserID;
    }

    public String getTrgChnlID() {
        return trgChnlID;
    }

    public void setTrgChnlID(String trgChnlID) {
        this.trgChnlID = trgChnlID;
    }

    public String getTrgMsgID() {
        return trgMsgID;
    }

    public void setTrgMsgID(String trgMsgID) {
        this.trgMsgID = trgMsgID;
    }

    public boolean isUserNotification() {
        return isUserNotification;
    }

    public void setUserNotification(boolean userNotification) {
        isUserNotification = userNotification;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Date getCrDate() {
        return crDate;
    }

    public void setCrDate(Date crDate) {
        this.crDate = crDate;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public Meta getAttributes() {
        return attributes;
    }

    public void setAttributes(Meta attributes) {
        this.attributes = attributes;
    }
}
