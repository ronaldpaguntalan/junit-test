package com.trendmicro.spring_boot_unit_testing.movies.repository;

import com.trendmicro.spring_boot_unit_testing.movies.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//We will write here all JUnit tests
//With JpaRepository we can perform database operations
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    List<MovieEntity> findByGenre(String genre);

}
