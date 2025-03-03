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
@Table(name="USERS")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String email;
    private int age;
    private boolean isSubscribed;

    public User(){
	super();
    }

    public User(String name, String email, int age){
	super();
	this.name = name;
	this.email = email;
	this.age = age;
	this.isSubscribed = true;
    }

    //Hardcoded as lombok isn't generating this getter
    public boolean getIsSubscribed() {
        return isSubscribed;
    }
}
