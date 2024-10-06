package com.javamongo.moviebooktickets.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javamongo.moviebooktickets.entity.Image;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {

}
