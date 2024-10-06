package com.javamongo.moviebooktickets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javamongo.moviebooktickets.entity.Movie;
import com.javamongo.moviebooktickets.service.MovieService;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    @GetMapping("/category")
    public ResponseEntity<?> getAllMoviesByCategory(@RequestParam String category) {
        return ResponseEntity.ok(movieService.getAllMoviesByCategory(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable String id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.saveMovie(movie));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<?> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        movie.setId(id);
        return ResponseEntity.ok(movieService.saveMovie(movie));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<?> deleteMovie(@PathVariable String id) {
        return ResponseEntity.ok(movieService.deleteMovie(id));
    }

    // Top 3 phim có doanh thu cao nhất
    @GetMapping("/top-revenue")
    public ResponseEntity<?> getTopRevenueMovies() {
        return ResponseEntity.ok(movieService.getTopRevenueMovies());
    }
}
