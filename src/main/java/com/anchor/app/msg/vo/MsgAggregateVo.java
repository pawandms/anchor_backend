package com.anchor.app.msg.vo;

import java.util.List;

import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.model.MsgBoxRelation;

/**
 * Used to get Messages for User for respective Channel
 * @author pawan
 *
 */
public class MsgAggregateVo extends MsgBoxRelation {

 private MsgBoxRelation msgBoxRelation;	
 private List<Message> msgList;
 private List<Message> parentMsgList;
 
public MsgBoxRelation getMsgBoxRelation() {
	return msgBoxRelation;
}
public void setMsgBoxRelation(MsgBoxRelation msgBoxRelation) {
	this.msgBoxRelation = msgBoxRelation;
}
public List<Message> getMsgList() {
	return msgList;
}
public void setMsgList(List<Message> msgList) {
	this.msgList = msgList;
}
public List<Message> getParentMsgList() {
	return parentMsgList;
}
public void setParentMsgList(List<Message> parentMsgList) {
	this.parentMsgList = parentMsgList;
}
 
 

}
