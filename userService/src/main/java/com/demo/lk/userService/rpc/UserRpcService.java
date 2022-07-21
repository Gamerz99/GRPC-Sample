package com.demo.lk.userService.rpc;

import com.demo.lk.User;
import com.demo.lk.UserServiceGrpc;
import com.demo.lk.userService.dto.UserDto;
import com.demo.lk.userService.stub.UserStubFactory;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@GRpcService
public class UserRpcService extends UserServiceGrpc.UserServiceImplBase {

	@Autowired
	private UserStubFactory userStubFactory;

	@Override
	public void getUser(User.GetUserRequest request, StreamObserver<User.GetUserResponse> responseObserver) {

		log.info("User Service called {}", request.getUserId());
		Optional<UserDto> userDto = getUser(request.getUserId());
		if (userDto.isPresent()){
			String userName = userDto.get().getUserName();
			String email = userDto.get().getEmail();
			User.GetUserResponse.Builder response = User.GetUserResponse.newBuilder();
			response.setUserId(request.getUserId()).setUserName(userName).setEmail(email);

			if (request.getWithAdditionalData()){
				Optional<User.UserAddressResponse> userAddressDto = getUserAddress(request.getUserId());
				userAddressDto.ifPresent(response::setAddress);
			}

			responseObserver.onNext(response.build());
			responseObserver.onCompleted();
		} else {
			responseObserver.onError(Status.NOT_FOUND.withDescription("Not Found").asRuntimeException());
		}
	}

	private Optional<User.UserAddressResponse> getUserAddress(String userId){
		Optional<User.UserAddressResponse> userAddressDto = Optional.empty();
		try {
			User.UserAddressResponse response = userStubFactory.getUserStub().getUserAddress(User.GetUserAddressRequest.newBuilder().setUserId(userId).build());
			userAddressDto = Optional.of(response);
		} catch (Exception e){
			log.error("Error", e);
		}

		return userAddressDto;
	}

	private Optional<UserDto> getUser(String userId){
		List<UserDto> userDto = new ArrayList<>();
		userDto.add(new UserDto("1","User1", "user1@gmail.com"));
		userDto.add(new UserDto("2","User2", "user2@gmail.com"));
		userDto.add(new UserDto("3","User3", "user3@gmail.com"));
		userDto.add(new UserDto("4","User4", "user4@gmail.com"));
		userDto.add(new UserDto("5","User5", "user5@gmail.com"));

		return userDto.stream().filter(u-> u.getUserId().equals(userId)).findFirst();
	}
}
