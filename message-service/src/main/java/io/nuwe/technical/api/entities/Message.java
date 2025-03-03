package io.nuwe.technical.api.entities;

import java.util.List;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="messages")
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userSenderId;
    private long userReceiverId;
    private String body;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime sentAt;

    public Message(){
        super();
        this.sentAt = LocalDateTime.now();
    }

    public Message(long userSenderId, long userReceiverId, String body){
        super();
        this.userSenderId = userSenderId;
        this.userReceiverId = userReceiverId;
        this.body = body;
        this.sentAt = LocalDateTime.now();
    }
}
