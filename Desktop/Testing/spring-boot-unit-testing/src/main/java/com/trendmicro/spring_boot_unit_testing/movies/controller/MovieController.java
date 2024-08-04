package com.trendmicro.spring_boot_unit_testing.movies.controller;

import com.trendmicro.spring_boot_unit_testing.movies.entity.MovieEntity;
import com.trendmicro.spring_boot_unit_testing.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService moviesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieEntity create(@RequestBody MovieEntity movie) {
        return moviesService.save(movie);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MovieEntity> read(){
        return moviesService.getAllMovies();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieEntity read(@PathVariable Long id){
        return moviesService.getMovieByID(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        moviesService.deleteMovie(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieEntity update(@PathVariable Long id, @RequestBody MovieEntity movie){
        return moviesService.updateMovie(movie, id);
    }
}
