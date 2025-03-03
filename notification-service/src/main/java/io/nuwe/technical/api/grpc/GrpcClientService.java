package io.nuwe.technical.api.grpc;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

import io.grpc.StatusRuntimeException;

import io.nuwe.technical.api.entities.*;

import io.nuwe.technical.api.lib.*;
import io.nuwe.technical.api.lib.NotificationProto.*;
import io.nuwe.technical.api.lib.NotificationServiceGrpc.NotificationServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import io.grpc.StatusRuntimeException;
import java.time.format.DateTimeFormatter;
import java.time.DateTimeException;

@Service
public class GrpcClientService {

	private static final Logger logger = LoggerFactory.getLogger(GrpcClientService.class);

	@GrpcClient("frontend")
	private NotificationServiceBlockingStub notificationStub;

	/**
	 * Sends a notification to the gRPC server.
	 *
	 * @param notification The notification to send.
	 * @return true if the notification was successfully sent and acknowledged by the server,
	 *         false otherwise.
	 * @throws IllegalArgumentException If the notification is null or contains invalid data.
	 */
	public boolean pushNotification(final Notification notification) {
		validateNotification(notification);

		try {
			final NotificationRequest request = buildNotificationRequest(notification);
			final NotificationResponse response = sendNotificationRequest(request);
			return handleNotificationResponse(response, notification);
		} catch (final StatusRuntimeException e) {
			logger.error("Failed to send notification: {}", notification, e);
			return false;
		} catch (DateTimeException e) {
			logger.error("Invalid date format in notification: {}", notification, e);
			return false;
		}
	}

	private void validateNotification(final Notification notification) {
		if (notification == null) {
			logger.error("Notification cannot be null");
			throw new IllegalArgumentException("Notification cannot be null");
		}
	}

	private NotificationRequest buildNotificationRequest(final Notification notification) {
		String sentAtFormatted = formatDate(notification.getSentAt());
		return NotificationRequest.newBuilder()
				.setMessageId(notification.getMessageId())
				.setUserSenderId(notification.getUserSenderId())
				.setUserReceiverId(notification.getUserReceiverId())
				.setBody(notification.getBody())
				.setSentAt(sentAtFormatted)
				.build();
	}

	private String formatDate(final LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
		return dateTime.format(formatter);
	}

	private NotificationResponse sendNotificationRequest(final NotificationRequest request) {
		return this.notificationStub.pushNotification(request);
	}

	private boolean handleNotificationResponse(final NotificationResponse response, final Notification notification) {
		if (response == null || !response.getNotificationArrived()) {
			logger.warn("Notification was not acknowledged by the server: {}", notification);
			return false;
		}

		logger.info("Notification successfully sent and acknowledged: {}", notification);
		return true;
	}
}
