package io.nuwe.technical.api.services;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.grpc.GrpcClientService;
import io.nuwe.technical.api.repositories.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    /**
     * Retrieves all messages received by a specific user.
     *
     * @param userReceiverId The ID of the user who received the messages.
     * @return A list of messages received by the user. If no messages are found, the list will be empty.
     */
    public List<Message> getAllMessagesByUserReceiverId(long userReceiverId) {
        return messageRepository.findAllByUserReceiverId(userReceiverId);
    }

    /**
     * Retrieves all messages sent by a specific user.
     *
     * @param userSenderId The ID of the user who sent the messages.
     * @return A list of messages sent by the user. If no messages are found, the list will be empty.
     */
    public List<Message> getAllMessagesByUserSenderId(long userSenderId) {
        return messageRepository.findAllByUserSenderId(userSenderId);
    }

    /**
     * Retrieves all messages sent by one user to another.
     *
     * @param userSenderId   The ID of the user who sent the messages.
     * @param userReceiverId The ID of the user who received the messages.
     * @return A list of messages sent by the sender to the receiver. If no messages are found, the list will be empty.
     */
    public List<Message> getAllMessagesByUserSenderIdAndUserReceiverId(long userSenderId, long userReceiverId) {
        return messageRepository.findAllByUserSenderIdAndUserReceiverId(userSenderId, userReceiverId);
    }

    /**
     * Retrieves a specific message by its ID.
     *
     * @param id The ID of the message to retrieve.
     * @return An Optional containing the message if found, or empty if not.
     */
    public Optional<Message> getMessageById(int id) {
        return messageRepository.findById(id);
    }
}
