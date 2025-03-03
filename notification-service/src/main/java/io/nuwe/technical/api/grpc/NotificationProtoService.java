package io.nuwe.technical.api.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.nuwe.technical.api.lib.NotificationProto.*;
import io.nuwe.technical.api.lib.NotificationServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.NotificationService;
import io.nuwe.technical.api.grpc.GrpcClientService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class NotificationProtoService extends NotificationServiceGrpc.NotificationServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NotificationProtoService.class);
    private final Set<String> processingRequests = ConcurrentHashMap.newKeySet();

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private GrpcClientService grpcClientService;

    @Override
    public void pushNotification(NotificationRequest req, StreamObserver<NotificationResponse> responseObserver) {
        final String REQUEST_ID = req.getId() + "-" + req.getMessageId();

        if (isDuplicateRequest(REQUEST_ID)) {
            handleDuplicateRequest(responseObserver, REQUEST_ID);
            return;
        }

        processingRequests.add(REQUEST_ID);

        try {
            Notification notification = mapRequestToNotification(req);
            boolean notificationSent = processNotification(notification);
            NotificationResponse response = buildNotificationResponse(notificationSent);
            sendResponse(responseObserver, response);
        } catch (DateTimeParseException e) {
            handleDateTimeParseError(responseObserver, req.getSentAt(), e);
        } catch (Exception e) {
            handleGenericError(responseObserver, e);
        } finally {
            processingRequests.remove(REQUEST_ID);
        }
    }

    private boolean isDuplicateRequest(String requestId) {
        return processingRequests.contains(requestId);
    }

    private void handleDuplicateRequest(StreamObserver<NotificationResponse> responseObserver, String requestId) {
        log.warn("Duplicate request detected: {}", requestId);
        responseObserver.onError(new StatusRuntimeException(Status.ALREADY_EXISTS));
    }

    private Notification mapRequestToNotification(NotificationRequest req) throws DateTimeParseException {
        Notification notification = new Notification();
        notification.setId(req.getId());
        notification.setUserSenderId(req.getUserSenderId());
        notification.setUserReceiverId(req.getUserReceiverId());
        notification.setMessageId(req.getMessageId());
        notification.setBody(req.getBody());
        notification.setSentAt(LocalDateTime.parse(req.getSentAt(), DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
        notification.setNotificationArrived(false); // Default value
        return notification;
    }

    private boolean processNotification(Notification notification) {
        boolean notificationSent = grpcClientService.pushNotification(notification);
        notification.setNotificationArrived(notificationSent);
        notificationService.saveNotification(notification);
        return notificationSent;
    }

    private NotificationResponse buildNotificationResponse(boolean notificationSent) {
        return NotificationResponse.newBuilder()
                .setNotificationArrived(notificationSent)
                .build();
    }

    private void sendResponse(StreamObserver<NotificationResponse> responseObserver, NotificationResponse response) {
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void handleDateTimeParseError(StreamObserver<NotificationResponse> responseObserver, String sentAt, DateTimeParseException e) {
        log.error("Invalid date format in request: {}", sentAt, e);
        responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT
                .withDescription("Invalid date format: " + sentAt)));
    }

    private void handleGenericError(StreamObserver<NotificationResponse> responseObserver, Exception e) {
        log.error("Error processing notification: {}", e.getMessage(), e);
        responseObserver.onError(new StatusRuntimeException(Status.INTERNAL
                .withDescription("Internal server error: " + e.getMessage())));
    }
}
