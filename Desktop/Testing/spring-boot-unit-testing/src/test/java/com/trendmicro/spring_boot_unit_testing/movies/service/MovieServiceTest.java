package com.trendmicro.spring_boot_unit_testing.movies.service;

import com.trendmicro.spring_boot_unit_testing.movies.entity.MovieEntity;
import com.trendmicro.spring_boot_unit_testing.movies.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

//for Assertions
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //To use Mockito Framework use this annotation.
public class MovieServiceTest {

    @InjectMocks //create an instance of the object and inject the mocks on it
    private MovieService movieService;

    @Mock //annotation used to mock the Object
    private MovieRepository movieRepository;

    private MovieEntity avatarMovie;
    private MovieEntity titanicMovie;

    //Arrange - we used this Lifecycle method which will be called first when we ran the test
    @BeforeEach
    void init(){
        avatarMovie = new MovieEntity();
        avatarMovie.setId(1L); //for Mockito to return a value for the id attribute.
        avatarMovie.setName("Avatar");
        avatarMovie.setGenre("Action");
        avatarMovie.setReleaseDate(LocalDate.of(2000, Month.APRIL, 22));

        titanicMovie = new MovieEntity();
        titanicMovie.setId(2L); //for Mockito to return a value for the id attribute.
        titanicMovie.setName("Titanic");
        titanicMovie.setGenre("Romantic");
        titanicMovie.setReleaseDate(LocalDate.of(1999, Month.MAY, 6));

    }

    @Test
    @DisplayName("Should save the movie to the database")
    void save(){

        //when(...): This is a Mockito method used to specify that when a certain method call is made,
        // it should return a predefined value. This is a stubbing
        when(movieRepository.save(any(MovieEntity.class))) //This part tells Mockito to watch for any calls to the save() method of the movieRepository, where the method is passed any MovieEntity object as an argument (any(MovieEntity.class)).
                            .thenReturn(avatarMovie); //.thenReturn(avatarMovie): This specifies that when the save() method is called with any MovieEntity, Mockito should return the avatarMovie object.

        MovieEntity newMovie = movieService.save(avatarMovie);
        assertNotNull(newMovie);
        assertThat(newMovie.getName()).isEqualTo("Avatar");
    }

    @Test
    @DisplayName("Should retrieve all movies with size 2")
    void getMovies(){

        List<MovieEntity> list = new ArrayList<>();
        list.add(avatarMovie);
        list.add(titanicMovie);

        when(movieRepository.findAll()).thenReturn(list);

        List<MovieEntity> movies =  movieService.getAllMovies();

        assertEquals(2, movies.size());
        assertNotNull(movies);

    }

    @Test
    @DisplayName("Should return the Movie object")
    void getMovieById(){
        when(movieRepository.findById(anyLong()))
                            .thenReturn(Optional.of(avatarMovie));

        MovieEntity existingMovie = movieService.getMovieByID(1L);

        assertNotNull(existingMovie);
        assertThat(existingMovie.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception when get movie by id")
    void getMovieByIdForException(){

        when(movieRepository.findById(1L))
                            .thenReturn(Optional.of(avatarMovie)); //simulate the behavior of findById(1L) when it's called in your application.
                                        // Handle the possibility that the entity with the given ID may not exist in the database. Using Optional allows the method to either return the entity wrapped in an Optional if it exists or an empty Optional if it does not.

        assertThrows(RuntimeException.class, () -> {
            movieService.getMovieByID(2L); //For runtime exceptions testing
        });
    }

    @Test
    @DisplayName("Should update the movie")
    void updateMovie(){

        when(movieRepository.findById(anyLong()))
                            .thenReturn(Optional.of(avatarMovie));
        when(movieService.save(any(MovieEntity.class)))
                        .thenReturn(avatarMovie);
        avatarMovie.setGenre("Fantasy");

        MovieEntity updatedMovie = movieService.updateMovie(avatarMovie, 1L);

        assertNotNull(updatedMovie);
        assertEquals("Fantasy", updatedMovie.getGenre());
    }

    @Test
    @DisplayName("Should delete the movie")
    void deleteMovie(){

        // Mock the behavior of the repository's findById method to return an Optional containing avatarMovie
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(avatarMovie));

        // Mock the delete method to do nothing when called with any MovieEntity instance
        doNothing().when(movieRepository).delete(any(MovieEntity.class));

        // Call the deleteMovie method on the service, passing in the ID 1L
        movieService.deleteMovie(1L);

        // Verify that the delete method was called exactly once with avatarMovie as the argument
        verify(movieRepository, times(1)).delete(avatarMovie);

    }
}
