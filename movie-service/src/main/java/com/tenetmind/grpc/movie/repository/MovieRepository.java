package com.tenetmind.grpc.movie.repository;

import com.tenetmind.grpc.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByGenreOrderByYearDesc(String genre);
}
