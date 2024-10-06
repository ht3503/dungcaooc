package com.javamongo.moviebooktickets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javamongo.moviebooktickets.entity.Movie;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    Optional<Movie> findByName(String name);
}
