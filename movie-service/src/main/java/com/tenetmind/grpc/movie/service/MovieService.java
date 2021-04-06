package com.tenetmind.grpc.movie.service;

import com.tenetmind.grpc.movie.MovieDto;
import com.tenetmind.grpc.movie.MovieSearchRequest;
import com.tenetmind.grpc.movie.MovieSearchResponse;
import com.tenetmind.grpc.movie.MovieServiceGrpc;
import com.tenetmind.grpc.movie.repository.MovieRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    @Autowired
    private MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        String genre = request.getGenre().toString();
        List<MovieDto> retrievedMovies = repository.findByGenreOrderByYearDesc(genre).stream()
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setYear(movie.getYear())
                        .setRating(movie.getRating())
                        .build())
                .collect(Collectors.toList());

        MovieSearchResponse response = MovieSearchResponse.newBuilder()
                .addAllMovie(retrievedMovies)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
