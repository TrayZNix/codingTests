package io.nuwe.technical.api.repositories;

import java.util.List;

import io.nuwe.technical.api.entities.Notification;

import org.springframework.data.repository.Repository;

public interface NotificationRepository extends Repository<Notification, Long> {
    List<Notification> findAll();
    Notification save(Notification n);
}
