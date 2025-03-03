package io.nuwe.technical.api.grpc;


import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import io.grpc.StatusRuntimeException;

import io.nuwe.technical.api.entities.*;

import io.nuwe.technical.api.lib.*;
import io.nuwe.technical.api.lib.UserProto.*;
import io.nuwe.technical.api.lib.UserServiceGrpc.UserServiceBlockingStub;

import io.nuwe.technical.api.lib.NotificationProto.*;
import io.nuwe.technical.api.lib.NotificationServiceGrpc.NotificationServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;


@Service
public class GrpcClientService {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

	@GrpcClient("user")
	private UserServiceBlockingStub userStub;

	@GrpcClient("notification")
	private NotificationServiceBlockingStub notificationStub;

	public Optional<UserResponse> getUser(final long id) {
		try {
			UserResponse res = userStub.getUser(UserRequest.newBuilder().setId(id).build());
			return Optional.ofNullable(res).filter(response -> response.getId() >= 0);
		} catch (StatusRuntimeException e) {
			return Optional.empty();
		}
	}

	public boolean pushNotification(Message message) {
		try {
			NotificationRequest notificationRequest = buildNotificationRequest(message);
			NotificationResponse res = notificationStub.pushNotification(notificationRequest);
			return res != null && res.getNotificationArrived();
		} catch (StatusRuntimeException e) {
			return false;
		}
	}

	private NotificationRequest buildNotificationRequest(Message message) {
		return NotificationRequest.newBuilder()
				.setId(0)
				.setUserSenderId(message.getUserSenderId())
				.setUserReceiverId(message.getUserReceiverId())
				.setMessageId(message.getId())
				.setBody(message.getBody())
				.setSentAt(message.getSentAt().format(DATE_TIME_FORMATTER))
				.build();
	}
}
