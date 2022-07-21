package com.demo.lk.userAddressService.rpc;

import com.demo.lk.User;
import com.demo.lk.UserServiceGrpc;
import com.demo.lk.userAddressService.dto.UserAddressDto;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@GRpcService
public class UserAddressService extends UserServiceGrpc.UserServiceImplBase {

	@Override
	public void getUserAddress(User.GetUserAddressRequest request, StreamObserver<User.UserAddressResponse> responseObserver) {

		log.info("User Address Service called {}", request.getUserId());
		Optional<UserAddressDto> userAddressDto = getUserAddress(request.getUserId());
		if (userAddressDto.isPresent()){
			String addressLine = userAddressDto.get().getAddressLine();
			String city = userAddressDto.get().getCity();
			String country = userAddressDto.get().getCountry();

			User.UserAddressResponse response = User.UserAddressResponse.newBuilder().setAddressLine(addressLine).setCity(city).setCountry(country).build();

			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} else {
			responseObserver.onError(Status.NOT_FOUND.withDescription("Not Found").asRuntimeException());
		}
	}

	private Optional<UserAddressDto> getUserAddress (String userId) {
		List<UserAddressDto> userAddressDto = new ArrayList<>();
		userAddressDto.add(new UserAddressDto("1","No 1","City 1","Sri Lanka"));
		userAddressDto.add(new UserAddressDto("2","No 2","City 2","Sri Lanka"));
		userAddressDto.add(new UserAddressDto("3","No 3","City 3","Sri Lanka"));
		userAddressDto.add(new UserAddressDto("4","No 4","City 4","Sri Lanka"));
		userAddressDto.add(new UserAddressDto("5","No 5","City 5","Sri Lanka"));

		return userAddressDto.stream().filter(a-> a.getUserId().equals(userId)).findFirst();
	}
}
