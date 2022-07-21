package com.demo.lk.userService.stub;

import com.demo.lk.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserStubFactory {

	private final ManagedChannel managedChannel;

	public UserStubFactory(@Value("${demo.grpc.host}") String grpcHost, @Value("${demo.grpc.port}") int grpcPort) {
		managedChannel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext().build();
	}

	public UserServiceGrpc.UserServiceBlockingStub getUserStub () {
		return UserServiceGrpc.newBlockingStub(managedChannel);
	}
}
