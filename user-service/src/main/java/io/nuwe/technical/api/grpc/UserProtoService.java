package io.nuwe.technical.api.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.nuwe.technical.api.lib.UserProto.*;
import io.nuwe.technical.api.lib.UserServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import io.nuwe.technical.api.entities.*;
import io.nuwe.technical.api.services.UserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@GrpcService
public class UserProtoService extends UserServiceGrpc.UserServiceImplBase {

	private static final Logger logger = LoggerFactory.getLogger(UserProtoService.class);

	@Autowired
	private UserService userService;

	/**
	 * Handles the getUser gRPC call. Retrieves a user by their ID and sends the response back to the client.
	 *
	 * @param req              The UserRequest containing the user ID.
	 * @param responseObserver The StreamObserver to send the UserResponse back to the client.
	 */
	@Override
	public void getUser(UserRequest req, StreamObserver<UserResponse> responseObserver) {
		long id = req.getId();

		if (!isValidId(id)) {
			handleError(responseObserver, Status.INVALID_ARGUMENT, "Invalid user ID: " + id);
			return;
		}

		Optional<User> optUser = userService.getUserById(id);

		if (optUser.isEmpty()) {
			handleError(responseObserver, Status.NOT_FOUND, "User not found for ID: " + id);
			return;
		}

		User user = optUser.get();
		UserResponse reply = buildUserResponse(user);

		logger.info("User found: ID={}, Email={}", user.getId(), user.getEmail());

		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

	/**
	 * Checks if the ID is valid.
	 * @param id User ID.
	 * @return True if the ID is valid, false otherwise.
	 */
	private boolean isValidId(long id) {
		return id > 0;
	}

	/**
	 * Handles errors by logging and sending an error response to the client.
	 * @param responseObserver The StreamObserver to send the error response.
	 * @param status The status of the error.
	 * @param description The description of the error.
	 */
	private void handleError(StreamObserver<UserResponse> responseObserver, Status status, String description) {
		logger.warn(description);
		responseObserver.onError(status.withDescription(description).asRuntimeException());
	}

	/**
	 * Builds a UserResponse from a User object.
	 * @param user The User object.
	 * @return The built UserResponse.
	 */
	private UserResponse buildUserResponse(User user) {
		return UserResponse.newBuilder()
				.setId(user.getId())
				.setName(user.getName())
				.setEmail(user.getEmail())
				.setAge(user.getAge())
				.setIsSubscribed(user.getIsSubscribed())
				.build();
	}
}
