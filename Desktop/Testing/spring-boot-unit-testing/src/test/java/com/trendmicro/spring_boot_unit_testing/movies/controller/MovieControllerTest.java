package com.trendmicro.spring_boot_unit_testing.movies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendmicro.spring_boot_unit_testing.movies.entity.MovieEntity;
import com.trendmicro.spring_boot_unit_testing.movies.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Indicates that this is a Spring Boot test for the MovieController, using MockMvc to test the controller layer
@WebMvcTest
public class MovieControllerTest {

    // Mocks the MovieService bean, allowing you to simulate its behavior without invoking the actual service layer
    @MockBean
    private MovieService movieService;

    // Autowires MockMvc, which is used to perform HTTP requests and validate responses in the test
    @Autowired
    private MockMvc mockMvc;

    // Autowires ObjectMapper, which is used to serialize and deserialize Java objects to and from JSON in tests
    @Autowired
    private ObjectMapper objectMapper;

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
    void test_should_create_new_movie() throws Exception {

        // Mock the save method of movieService to return the created movie entity
        when(movieService.save(any(MovieEntity.class))).thenReturn(avatarMovie);

        // Perform a POST request to the /movies endpoint with the movie data as JSON
        this.mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avatarMovie)))
                // Expect the response status to be 201 Created
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(avatarMovie.getName())))
                .andExpect(jsonPath("$.genre", is(avatarMovie.getGenre())))
                .andExpect(jsonPath("$.releaseDate", is(avatarMovie.getReleaseDate().toString())));
    }

    @Test
    void test_should_fetch_all_movies() throws Exception {

        List<MovieEntity> list = new ArrayList<MovieEntity>();
        list.add(avatarMovie);
        list.add(titanicMovie);

        when(movieService.getAllMovies()).thenReturn(list);

        this.mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())));
    }

    @Test
    void test_should_fetch_one_movie_by_id() throws Exception {

        when(movieService.getMovieByID(anyLong())).thenReturn(avatarMovie);

        this.mockMvc.perform(get("/movies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(avatarMovie.getName())))
                .andExpect(jsonPath("$.genre", is(avatarMovie.getGenre())));
    }

    @Test
    void test_should_delete_the_movie() throws Exception{

        doNothing().when(movieService).deleteMovie(anyLong());

        this.mockMvc.perform(delete("/movies/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_should_update_the_movie() throws Exception{

        when(movieService.updateMovie(any(MovieEntity.class), anyLong())).thenReturn(avatarMovie);

        this.mockMvc.perform(put("/movies/{id}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(avatarMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(avatarMovie.getName())))
                .andExpect(jsonPath("$.genre", is(avatarMovie.getGenre())));
    }
}
