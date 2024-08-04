package com.trendmicro.spring_boot_unit_testing.movies.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


//Lombok Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

//JPA Annotations
@Entity
@Table(name = "tbl_movies")
public class MovieEntity {
    @Id //to know if this is a primary attribute
    @GeneratedValue(strategy = GenerationType.IDENTITY) //for auto increment
    private Long id;

    private String name;

    private String genre;

    private LocalDate releaseDate;
}
