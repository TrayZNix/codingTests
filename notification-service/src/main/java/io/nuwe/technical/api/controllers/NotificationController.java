package io.nuwe.technical.api.controllers;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.repositories.NotificationRepository;
import io.nuwe.technical.api.services.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@RestController
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Retrieves all notifications from the system.
     *
     * @return A ResponseEntity containing a list of notifications and an HTTP status code.
     *         If no notifications are found, returns HTTP status 204 (NO_CONTENT).
     *         Otherwise, returns HTTP status 200 (OK) with the list of notifications.
     */
    @GetMapping("/notification/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();

        if (notifications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }
}
