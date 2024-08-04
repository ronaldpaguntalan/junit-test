package com.trendmicro.spring_boot_unit_testing.movies.repository;

import com.trendmicro.spring_boot_unit_testing.movies.entity.MovieEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

//for Assertions
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private MovieEntity avatarMovie;
    private MovieEntity titanicMovie;

    //Arrange - we used this Lifecycle method which will be called first when we ran the test
    @BeforeEach
    void init(){
        avatarMovie = new MovieEntity();
        avatarMovie.setName("Avatar");
        avatarMovie.setGenre("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        titanicMovie = new MovieEntity();
        titanicMovie.setName("Titanic");
        titanicMovie.setGenre("Romantic");
        titanicMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 6));

    }

    @Test
    @DisplayName("It should saved the movie to the database")
    void saveMovie(){
        //Act - calling the method or unit to be tested
        MovieEntity newMovie = movieRepository.save(avatarMovie);

        //Assert - verifying the data is saved correctly in the database
        assertNotNull(newMovie);
        assertThat(newMovie.getId()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("It should return the list with size of 2")
    void getMovies(){
        movieRepository.save(avatarMovie);

        movieRepository.save(titanicMovie);

        List<MovieEntity> list = movieRepository.findAll();

        assertNotNull(list);
        assertThat(list).isNotNull();
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("It should return the movie by its id")
    void getByMovieById(){

        movieRepository.save(avatarMovie);

        MovieEntity existingMovie = movieRepository.findById(avatarMovie.getId()).get();

        assertNotNull(existingMovie);
        assertEquals("Action", existingMovie.getGenre());
        assertThat(avatarMovie.getReleaseDate()).isBefore(LocalDate.of(2000, Month.APRIL,23));
    }

    @Test
    @DisplayName("It should update the movie with genre FANTASY")
    void updateMovie(){
        movieRepository.save(avatarMovie);
        MovieEntity existingMovie = movieRepository.findById(avatarMovie.getId()).get();

        existingMovie.setGenre("Fantasy");
        MovieEntity newMovie = movieRepository.save(existingMovie);

        assertEquals("Fantasy", newMovie.getGenre());
        assertEquals("Avatar", newMovie.getName());
    }

    @Test
    @DisplayName("It should delete the movie by its id")
    void deleteMovie(){
        movieRepository.save(avatarMovie);
        Long idAvatarMovie = avatarMovie.getId();

        movieRepository.save(titanicMovie);

        movieRepository.delete(avatarMovie);
        Optional<MovieEntity> existingMovie = movieRepository.findById(idAvatarMovie);
        List<MovieEntity> list = movieRepository.findAll();

        assertEquals(1, list.size());
        assertThat(existingMovie).isEmpty();
    }
    
    @Test
    @DisplayName("It should return list of movies with genre ROMANCE")
    void getMoviesByGenre(){
        movieRepository.save(avatarMovie);

        movieRepository.save(titanicMovie);

        List<MovieEntity> list = movieRepository.findByGenre("Romantic");

        assertNotNull(list);
        assertThat(list.size()).isEqualTo(1);
    }

}
