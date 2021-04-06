package com.tenetmind.grpc.aggregator.controller;

import com.tenetmind.grpc.aggregator.dto.Movie;
import com.tenetmind.grpc.aggregator.dto.UserGenre;
import com.tenetmind.grpc.aggregator.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;

    @GetMapping("/user/{loginId}")
    public List<Movie> getMovies(@PathVariable String loginId) {
        return userMovieService.getUserMovieSuggestions(loginId);
    }

    @PostMapping("/user")
    public void setUserGenre(@RequestBody UserGenre userGenre) {
        userMovieService.setUserGenre(userGenre);
    }

}
