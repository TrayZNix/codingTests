#!/bin/bash

# Clean install the proto definition
(cd proto-definition/ && ./mvnw clean install)

# Build the User service 
(cd user-service/ && ./mvnw clean package)

# Build the Message service 
(cd message-service/ && ./mvnw clean package)

# # Build the Notification service 
(cd notification-service/ && ./mvnw clean package)

# # Build the Frontend service
(cd frontend-grpc-service/ && ./mvnw clean package)

