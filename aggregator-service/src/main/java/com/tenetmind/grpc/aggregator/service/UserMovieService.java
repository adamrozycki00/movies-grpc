package com.tenetmind.grpc.aggregator.service;

import com.tenetmind.grpc.aggregator.dto.Movie;
import com.tenetmind.grpc.aggregator.dto.UserGenre;
import com.tenetmind.grpc.common.Genre;
import com.tenetmind.grpc.movie.MovieSearchRequest;
import com.tenetmind.grpc.movie.MovieSearchResponse;
import com.tenetmind.grpc.movie.MovieServiceGrpc;
import com.tenetmind.grpc.user.UserGenreUpdateRequest;
import com.tenetmind.grpc.user.UserResponse;
import com.tenetmind.grpc.user.UserSearchRequest;
import com.tenetmind.grpc.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<Movie> getUserMovieSuggestions(String loginId) {
        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder()
                .setLoginId(loginId)
                .build();
        UserResponse userResponse = userStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder()
                .setGenre(userResponse.getGenre())
                .build();
        MovieSearchResponse movieSearchResponse = movieStub.getMovies(movieSearchRequest);

        return movieSearchResponse.getMovieList().stream()
                .map(movieDto -> new Movie(movieDto.getTitle(), movieDto.getYear(), movieDto.getRating()))
                .collect(Collectors.toList());
    }

    public void setUserGenre(UserGenre userGenre) {
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        userStub.updateUserGenre(userGenreUpdateRequest);
    }

}
