package com.trendmicro.spring_boot_unit_testing.service;

import com.trendmicro.spring_boot_unit_testing.movies.entity.MovieEntity;
import com.trendmicro.spring_boot_unit_testing.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieEntity save(MovieEntity movie){
        return movieRepository.save(movie);
    }

    public List<MovieEntity> getAllMovies(){
        return movieRepository.findAll();
    }

    public MovieEntity getMovieByID(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie found for the id " + id));
    }

    public MovieEntity updateMovie(MovieEntity movie, Long id){
        MovieEntity existingMovie = movieRepository.findById(id).get();
        existingMovie.setGenre(movie.getGenre());
        existingMovie.setName(movie.getName());
        existingMovie.setReleaseDate(movie.getReleaseDate());
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long id){
        MovieEntity existingMovie = movieRepository.findById(id).get();
        movieRepository.delete(existingMovie);
    }
}
