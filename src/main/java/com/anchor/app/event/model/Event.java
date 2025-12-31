package com.anchor.app.event.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.anchor.app.msg.enums.EventEntityType;
import com.anchor.app.msg.enums.EventType;
import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.vo.UserChannel;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Event implements Serializable {

	private int eventId;
	private EventType type;
	private EventEntityType entity;
	private String entityId;
	private String author;
    private List<String> targetUser= new ArrayList<String>();
    private UserChannel channel;
    private Message message;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date actionDate;
    
    
    public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public EventEntityType getEntity() {
		return entity;
	}
	public void setEntity(EventEntityType entity) {
		this.entity = entity;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<String> getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(List<String> targetUser) {
		this.targetUser = targetUser;
	}
	public UserChannel getChannel() {
		return channel;
	}
	public void setChannel(UserChannel channel) {
		this.channel = channel;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
	
	
	public Date getActionDate() {
		return actionDate;
	}
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", type=" + type + ", entity=" + entity + ", entityId=" + entityId
				+ ", author=" + author + ", targetUser=" + targetUser + ", channel=" + channel + ", message=" + message
				+ ", actionDate=" + actionDate + "]";
	}
	
	
	
    
    
	

}
