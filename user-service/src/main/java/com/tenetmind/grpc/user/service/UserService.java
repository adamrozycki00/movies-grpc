package com.tenetmind.grpc.user.service;

import com.tenetmind.grpc.common.Genre;
import com.tenetmind.grpc.user.UserGenreUpdateRequest;
import com.tenetmind.grpc.user.UserResponse;
import com.tenetmind.grpc.user.UserSearchRequest;
import com.tenetmind.grpc.user.UserServiceGrpc;
import com.tenetmind.grpc.user.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository repository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder responseBuilder = UserResponse.newBuilder();
        repository.findById(request.getLoginId()).ifPresent(user ->
                responseBuilder
                        .setName(user.getName())
                        .setLoginId(user.getLogin())
                        .setGenre(Genre.valueOf(user.getGenre().toUpperCase()))
        );
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder responseBuilder = UserResponse.newBuilder();
        repository.findById(request.getLoginId()).ifPresent(user -> {
            user.setGenre(request.getGenre().toString());
            responseBuilder
                    .setName(user.getName())
                    .setLoginId(user.getLogin())
                    .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
        });
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
