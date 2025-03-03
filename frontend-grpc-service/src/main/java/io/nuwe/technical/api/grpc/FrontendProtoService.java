package io.nuwe.technical.api.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.nuwe.technical.api.lib.NotificationProto.*;
import io.nuwe.technical.api.lib.NotificationServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import io.nuwe.technical.api.entities.Notification;
import io.nuwe.technical.api.repositories.NotificationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class FrontendProtoService extends NotificationServiceGrpc.NotificationServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(FrontendProtoService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void pushNotification(NotificationRequest req, StreamObserver<NotificationResponse> responseObserver) {
        try {
            logIncomingRequest(req);

            Notification notification = mapRequestToNotification(req);
            saveNotification(notification);

            NotificationResponse reply = buildNotificationResponse(true);
            sendResponse(responseObserver, reply);

        } catch (IllegalArgumentException e) {
            handleInvalidArgumentError(responseObserver, req, e);
        } catch (Exception e) {
            handleGenericError(responseObserver, req, e);
        }
    }

    private void logIncomingRequest(NotificationRequest req) {
        logger.info("Received NotificationRequest: id={}, userSenderId={}, userReceiverId={}, messageId={}, body={}, sentAt={}",
                req.getId(), req.getUserSenderId(), req.getUserReceiverId(), req.getMessageId(), req.getBody(), req.getSentAt());
    }

    private Notification mapRequestToNotification(NotificationRequest req) {
        LocalDateTime sentAt = parseSentAt(req.getSentAt());

        Notification notification = new Notification();
        notification.setId(req.getId());
        notification.setUserSenderId(req.getUserSenderId());
        notification.setUserReceiverId(req.getUserReceiverId());
        notification.setMessageId(req.getMessageId());
        notification.setBody(req.getBody());
        notification.setSentAt(sentAt);
        notification.setNotificationArrived(true);

        return notification;
    }

    private LocalDateTime parseSentAt(String sentAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        try {
            return LocalDateTime.parse(sentAt, formatter);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format: {}", sentAt, e);
            throw new IllegalArgumentException("Invalid date format: " + sentAt);
        }
    }

    private void saveNotification(Notification notification) {
        logger.info("Saving Notification: {}", notification);
        notificationRepository.save(notification);
    }

    private NotificationResponse buildNotificationResponse(boolean notificationArrived) {
        return NotificationResponse.newBuilder()
                .setNotificationArrived(notificationArrived)
                .build();
    }

    private void sendResponse(StreamObserver<NotificationResponse> responseObserver, NotificationResponse response) {
        logger.info("Sending NotificationResponse: notificationArrived={}", response.getNotificationArrived());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void handleInvalidArgumentError(StreamObserver<NotificationResponse> responseObserver, NotificationRequest req, IllegalArgumentException e) {
        logger.error("Invalid request data: {}", req, e);
        responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
    }

    private void handleGenericError(StreamObserver<NotificationResponse> responseObserver, NotificationRequest req, Exception e) {
        logger.error("Error processing NotificationRequest: {}", req, e);
        responseObserver.onError(Status.INTERNAL.withDescription("Internal server error").asRuntimeException());
    }
}
