package io.nuwe.technical.api.grpc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import io.grpc.StatusRuntimeException;

import io.nuwe.technical.api.entities.*;

import io.nuwe.technical.api.lib.UserProto.*;

import io.nuwe.technical.api.lib.UserServiceGrpc.UserServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;


@Service
public class GrpcClientService {

	private static final Logger logger = LoggerFactory.getLogger(GrpcClientService.class);

	@GrpcClient("myself")
	private UserServiceBlockingStub userStub;

	/**
	 * Gets the user by its ID using a gRPC request.
	 * @param id User ID.
	 * @return An optional with the user if it exists, or empty if it doesn't.
	 */
	public Optional<User> getUser(final long id) {
		if (!isValidId(id)) {
			return Optional.empty();
		}

		try {
			UserResponse res = userStub.getUser(UserRequest.newBuilder().setId(id).build());
			return Optional.ofNullable(res)
					.filter(response -> response.getId() >= 0)
					.map(this::mapResponseToUser);
		} catch (StatusRuntimeException e) {
			logger.error("gRPC call failed: {}", e.getStatus(), e);
			return Optional.empty();
		}
	}

	/**
	 * Checks if the ID is valid.
	 * @param id User ID.
	 * @return True if the ID is valid, false otherwise.
	 */
	private boolean isValidId(long id) {
		return id >= 0;
	}

	/**
	 * Maps gRPC response (UserResponse) to User.
	 * @param res gRPC response.
	 * @return Mapped user object.
	 */
	private User mapResponseToUser(UserResponse res) {
		User user = new User();
		user.setId(res.getId());
		user.setName(res.getName());
		user.setEmail(res.getEmail());
		user.setAge(res.getAge());
		user.setSubscribed(res.getIsSubscribed());
		return user;
	}
}
