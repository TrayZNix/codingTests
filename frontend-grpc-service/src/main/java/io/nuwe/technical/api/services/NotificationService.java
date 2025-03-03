package io.nuwe.technical.api.services;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.repositories.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private  NotificationRepository notificationRepository;

    /**
     * Retrieves all notifications from the database.
     *
     * @return A list of all notifications. If no notifications are found, the list will be empty.
     */
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}