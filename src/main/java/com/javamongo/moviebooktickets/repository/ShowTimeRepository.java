package com.javamongo.moviebooktickets.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.javamongo.moviebooktickets.entity.ShowTime;
import java.util.List;


@Repository
public interface ShowTimeRepository extends MongoRepository<ShowTime, String> {
    List<ShowTime> findByMovieId(String movieId);
}
