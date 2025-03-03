package io.nuwe.technical.api.services;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.repositories.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;


    /**
     * Saves a notification to the database if it does not already exist.
     *
     * @param notification The notification to save.
     * @throws IllegalArgumentException If the notification is null.
     */
    public void saveNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }

        Optional<Notification> existingNotification = notificationRepository.findByMessageId(notification.getMessageId());
        if (existingNotification.isEmpty()) {
            notificationRepository.save(notification);
        }
    }
}
