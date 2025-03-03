package io.nuwe.technical.api.grpc;

import io.grpc.stub.StreamObserver;
import io.nuwe.technical.api.entities.Message;
import io.nuwe.technical.api.lib.MessageProto;
import io.nuwe.technical.api.lib.MessageProto.*;
import io.nuwe.technical.api.lib.MessageServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.MessageService;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Grcp service implementation class
 */
@GrpcService
public class MessageProtoService extends MessageServiceGrpc.MessageServiceImplBase {

	@Autowired
	private MessageService messageService;

	/**
	 * Retrieves all messages sent from one user to another and streams them to the client.
	 *
	 * @param req              The request containing the sender and receiver IDs.
	 * @param responseObserver The observer to stream the response back to the client.
	 */
	@Override
	public void getMessagesFromTo(MessageFromToRequest req, StreamObserver<MessageResponse> responseObserver) {
		long userSenderId = req.getUserSenderId();
		long userReceiverId = req.getUserReceiverId();

		List<Message> messages = messageService.getAllMessagesByUserSenderIdAndUserReceiverId(userSenderId, userReceiverId);

		MessageResponse reply = buildMessageResponse(messages);
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

	/**
	 * Retrieves all messages received by a specific user and streams them to the client.
	 *
	 * @param req              The request containing the receiver ID.
	 * @param responseObserver The observer to stream the response back to the client.
	 */
	@Override
	public void getMessagesReceivedFrom(MessageUserRequest req, StreamObserver<MessageResponse> responseObserver) {
		long userReceiverId = req.getUserId();

		List<Message> messages = messageService.getAllMessagesByUserReceiverId(userReceiverId);

		MessageResponse reply = buildMessageResponse(messages);
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

	/**
	 * Retrieves all messages sent by a specific user and streams them to the client.
	 *
	 * @param req              The request containing the sender ID.
	 * @param responseObserver The observer to stream the response back to the client.
	 */
	@Override
	public void getMessagesSentFrom(MessageUserRequest req, StreamObserver<MessageResponse> responseObserver) {
		long userSenderId = req.getUserId();

		List<Message> messages = messageService.getAllMessagesByUserSenderId(userSenderId);

		MessageResponse reply = buildMessageResponse(messages);
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

	/**
	 * Builds a MessageResponse from a list of messages.
	 *
	 * @param messages The list of messages to include in the response.
	 * @return A MessageResponse containing the messages.
	 */
	private MessageResponse buildMessageResponse(List<Message> messages) {
		MessageResponse.Builder messageBuilder = MessageResponse.newBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

		for (Message msg : messages) {
			messageBuilder.addMessages(MessageProto.Message.newBuilder()
					.setId(msg.getId())
					.setUserSenderId(msg.getUserSenderId())
					.setUserReceiverId(msg.getUserReceiverId())
					.setBody(msg.getBody())
					.setSentAt(msg.getSentAt().format(formatter))
					.build());
		}

		return messageBuilder.build();
	}
}
