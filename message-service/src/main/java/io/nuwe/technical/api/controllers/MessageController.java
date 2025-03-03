package io.nuwe.technical.api.controllers;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.repositories.MessageRepository;
import io.nuwe.technical.api.services.*;

import io.nuwe.technical.api.lib.UserProto.UserResponse;

import io.nuwe.technical.api.grpc.GrpcClientService;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/message")
@Validated
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private GrpcClientService grpcClientService;

	@Autowired
	private MessageRepository messageRepository;

	/**
	 * Retrieves all messages from the system.
	 *
	 * @return A ResponseEntity containing a list of messages and an HTTP status code.
	 *         If no messages are found, returns HTTP status 204 (NO_CONTENT).
	 *         Otherwise, returns HTTP status 200 (OK) with the list of messages.
	 */
	@GetMapping("/all")
	public ResponseEntity<List<Message>> getAllMessages() {
		List<Message> messages = messageRepository.findAll();

		if (messages.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(messages, HttpStatus.OK);
	}

	/**
	 * Retrieves all messages sent to a specific user by their ID.
	 *
	 * @param id The ID of the user who received the messages.
	 * @return A ResponseEntity containing a list of messages and an HTTP status code.
	 *         If the user is not found, returns HTTP status 404 (NOT_FOUND).
	 *         If no messages are found, returns HTTP status 204 (NO_CONTENT).
	 *         Otherwise, returns HTTP status 200 (OK) with the list of messages.
	 */
	@GetMapping("/to/{id}")
	public ResponseEntity<?> getMessagesByUserReceiverId(@PathVariable("id") @Valid @Min( 1 ) long id) {
		Optional<UserResponse> user = grpcClientService.getUser(id);

		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Message> messages = messageService.getAllMessagesByUserReceiverId(id);

		if (messages.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(messages, HttpStatus.OK);
	}

	/**
	 * Retrieves all messages sent by a specific user by their ID.
	 *
	 * @param id The ID of the user who sent the messages.
	 * @return A ResponseEntity containing a list of messages and an HTTP status code.
	 *         If the user is not found, returns HTTP status 404 (NOT_FOUND).
	 *         If no messages are found, returns HTTP status 204 (NO_CONTENT).
	 *         Otherwise, returns HTTP status 200 (OK) with the list of messages.
	 */
	@GetMapping("/from/{id}")
	public ResponseEntity<?> getMessagesByUserSenderId(@PathVariable("id") @Valid @Min( 1 ) long id) {
		Optional<UserResponse> user = grpcClientService.getUser(id);

		if (!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Message> messages = messageService.getAllMessagesByUserSenderId(id);

		if (messages.isEmpty()) {
			return new ResponseEntity<>("User: " + user.get().getName() + " and Id " + user.get().getId(), HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(messages, HttpStatus.OK);
	}

	/**
	 * Retrieves all messages sent by one user to another.
	 *
	 * @param userSenderId   The ID of the user who sent the messages.
	 * @param userReceiverId The ID of the user who received the messages.
	 * @return A ResponseEntity containing a list of messages and an HTTP status code.
	 *         If either user is not found, returns HTTP status 404 (NOT_FOUND).
	 *         If no messages are found, returns HTTP status 204 (NO_CONTENT).
	 *         Otherwise, returns HTTP status 200 (OK) with the list of messages.
	 */
	@GetMapping("/from/{from}/to/{to}")
	public ResponseEntity<List<Message>> getMessagesByUserSenderIdAndUserReceiverId(
			@PathVariable("from") long userSenderId,
			@PathVariable("to") long userReceiverId) {

		Optional<UserResponse> userSender = grpcClientService.getUser(userSenderId);
		Optional<UserResponse> userReceiver = grpcClientService.getUser(userReceiverId);

		if (!userSender.isPresent() || !userReceiver.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Message> messages = messageService.getAllMessagesByUserSenderIdAndUserReceiverId(userSenderId, userReceiverId);

		if (messages.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(messages, HttpStatus.OK);
	}

	/**
	 * Retrieves a specific message by its ID.
	 *
	 * @param id The ID of the message to retrieve.
	 * @return A ResponseEntity containing the message and an HTTP status code.
	 *         If the message is not found, returns HTTP status 404 (NOT_FOUND).
	 *         Otherwise, returns HTTP status 200 (OK) with the message.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Message> getMessageById(@PathVariable("id") @Valid @Min(1) int id) {
		Optional<Message> optMessage = messageService.getMessageById(id);

		return optMessage.map(message -> new ResponseEntity<>(message, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Creates a new message.
	 *
	 * @param message The message to create.
	 * @return A ResponseEntity containing the created message and an HTTP status code.
	 *         If the sender or receiver is not found, returns HTTP status 404 (NOT_FOUND).
	 *         Otherwise, returns HTTP status 201 (CREATED) with the created message.
	 */
	@PostMapping("")
	public ResponseEntity<Message> createMessage(@RequestBody Message message) {
		Optional<UserResponse> userSender = grpcClientService.getUser(message.getUserSenderId());
		Optional<UserResponse> userReceiver = grpcClientService.getUser(message.getUserReceiverId());

		if (!userSender.isPresent() || !userReceiver.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Message createdMessage = messageRepository.save(message);
		grpcClientService.pushNotification(createdMessage);
		return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
	}

	/**
	 * Deletes a specific message by its ID.
	 *
	 * @param id The ID of the message to delete.
	 * @return A ResponseEntity containing the deleted message and an HTTP status code.
	 *         If the message is not found, returns HTTP status 404 (NOT_FOUND).
	 *         Otherwise, returns HTTP status 200 (OK) with the deleted message.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Message> deleteMessageById(@PathVariable("id") @Valid @Min( 1 ) int id) {
		Optional<Message> optMessage = messageService.getMessageById(id);

		return optMessage.map(message -> {messageRepository.delete(message);
			return new ResponseEntity<>(message, HttpStatus.OK);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Deletes all messages.
	 *
	 * @return A ResponseEntity with an HTTP status code 200 (OK).
	 */
	@DeleteMapping("/all")
	public ResponseEntity<Void> deleteAllMessages() {
		messageRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
