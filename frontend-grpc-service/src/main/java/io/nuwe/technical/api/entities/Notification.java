package io.nuwe.technical.api.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Entity
@Table(name="Notifications")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long messageId;
    private long userSenderId;
    private long userReceiverId;
    private String body;
    private boolean notificationArrived;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime sentAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime sentNotificationAt; 

    public Notification(){
        super();
        this.notificationArrived = false;
        this.sentNotificationAt= LocalDateTime.now();
    }
}
