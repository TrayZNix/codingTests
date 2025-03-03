package io.nuwe.technical.api.entities;

import java.util.List;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="Notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long messageId;

    private long userSenderId;
    private long userReceiverId;

    private String body;

    private boolean notificationArrived;

    // Message is sent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime sentAt; // Message is sent

    // Notification is send
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime sentNotificationAt; 

    public Notification(){
	super();
	this.notificationArrived = false;
	this.sentNotificationAt= LocalDateTime.now();
    }

    public Notification(long messageId, long userSenderId, long userReceiverId, String body, LocalDateTime sentAt){
	super();
	this.messageId = messageId;
	this.userSenderId = userSenderId;
	this.userReceiverId = userReceiverId;
	this.body = body;
	this.notificationArrived = false;
	this.sentAt = sentAt;
	this.sentNotificationAt = LocalDateTime.now();
    }

    public long getId() {
	return this.id;
    }

    public long getMessageId() {
	return this.messageId;
    }

    public long getUserSenderId() {
	return this.userSenderId;
    }

    public long getUserReceiverId() {
	return this.userReceiverId;
    }

    public String getBody(){
	return this.body;
    }

    public LocalDateTime getSentNotificationAt() {
	return this.sentNotificationAt;
    }

    public LocalDateTime getSentAt() {
	return this.sentAt;
    }

    public boolean getNotificationArrived(){
	return this.notificationArrived;
    }

    public void setId(long id){
	this.id = id;
    }

    public void setMessageId(long messageId){
	this.messageId= messageId;
    }

    public void setUserSenderId(long userSenderId){
	this.userSenderId= userSenderId;
    }

    public void setUserReceiverId(long userReceiverId){
	this.userReceiverId= userReceiverId;
    }

    public void setBody(String body){
	this.body = body;
    }

    public void setNotificationArrived(boolean notificationArrived){
	this.notificationArrived = notificationArrived;
    }

    public void setSentNotificationAt(LocalDateTime sentNotificationAt){
	this.sentNotificationAt = sentNotificationAt;
    }

    public void setSentAt(LocalDateTime sentAt){
	this.sentAt = sentAt;
    }

}
