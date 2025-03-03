package io.nuwe.technical.api.repositories;

import java.util.List;
import java.util.Optional;

import io.nuwe.technical.api.entities.Message;

import org.springframework.data.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {
    List<Message> findAll();
    Optional<Message> findById(long id);
    List<Message> findAllByUserReceiverId(long userReceiverId);
    List<Message> findAllByUserSenderId(long userSenderId);
    List<Message> findAllByUserSenderIdAndUserReceiverId(long userSenderId, long userReceiverId);
    Message save(Message s);
    void delete(Message s);
    void deleteAll();
}
