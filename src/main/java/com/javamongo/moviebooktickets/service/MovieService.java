package com.javamongo.moviebooktickets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.javamongo.moviebooktickets.dto.MovieDisplay;
import com.javamongo.moviebooktickets.dto.MovieRevenue;
import com.javamongo.moviebooktickets.dto.MyResponse;
import com.javamongo.moviebooktickets.dto.ShowTimeDto;
import com.javamongo.moviebooktickets.entity.Movie;
import com.javamongo.moviebooktickets.entity.ShowTime;
import com.javamongo.moviebooktickets.repository.MovieRepository;
import com.javamongo.moviebooktickets.repository.ShowTimeRepository;
import java.util.ArrayList;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ShowTimeRepository showTimeRepository;

    public MyResponse<List<MovieDisplay>> getAllMovies() {
        MyResponse<List<MovieDisplay>> myResponse = new MyResponse<>();
        List<Movie> movies = movieRepository.findAll();
        List<MovieDisplay> movieDisplays = new ArrayList<>();
        for (Movie movie : movies) {
            List<ShowTime> showTimes = showTimeRepository.findByMovieId(movie.getId());
            MovieDisplay movieDisplay = new MovieDisplay();

            for (ShowTime showTime : showTimes) {
                showTime.setMovie(null);
            }
            movieDisplay.setShowTimes(showTimes);
            movieDisplay.setName(movie.getName());
            movieDisplay.setDescription(movie.getDescription());
            movieDisplay.setDuration(movie.getDuration());
            movieDisplay.setLanguages(movie.getLanguages());
            movieDisplay.setCategories(movie.getCategories());
            movieDisplay.setPoster(movie.getPoster());
            movieDisplay.setTrailer(movie.getTrailer());
            movieDisplay.setReleaseDate(movie.getReleaseDate());
            movieDisplays.add(movieDisplay);
        }
        myResponse.setStatus(200);
        myResponse.setMessage("Lấy danh sách thành công");
        myResponse.setData(movieDisplays);
        return myResponse;
    }

    public MyResponse<Movie> getMovieById(String id) {
        MyResponse<Movie> myResponse = new MyResponse<>();
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) {
            myResponse.setStatus(404);
            myResponse.setMessage("Không tìm thấy phim");
            return myResponse;
        }
        myResponse.setStatus(200);
        myResponse.setMessage("Lấy thông tin thành công");
        myResponse.setData(movieRepository.findById(id).orElse(null));
        return myResponse;
    }

    public MyResponse<Movie> saveMovie(Movie movie) {
        MyResponse<Movie> myResponse = new MyResponse<>();
        Optional<Movie> existingMovie = movieRepository.findByName(movie.getName());
        if (existingMovie.isPresent()) {
            myResponse.setStatus(400);
            myResponse.setMessage("Phim đã tồn tại");
            myResponse.setData(existingMovie.get());
            return myResponse;
        }
        myResponse.setStatus(200);
        myResponse.setMessage("Lưu phim thành công");
        myResponse.setData(movieRepository.save(movie));
        return myResponse;
    }

    public MyResponse<?> deleteMovie(String id) {
        movieRepository.deleteById(id);
        MyResponse<?> myResponse = new MyResponse<>();
        myResponse.setStatus(200);
        myResponse.setMessage("Xóa phim thành công");
        return myResponse;
    }

    public MyResponse<List<Movie>> getAllMoviesByCategory(String category) {
        MyResponse<List<Movie>> myResponse = new MyResponse<>();
        myResponse.setStatus(200);
        myResponse.setMessage("Lấy danh sách thành công");
        myResponse
                .setData(movieRepository.findAll().stream().filter(m -> m.getCategories().contains(category)).toList());
        return myResponse;
    }

    public MyResponse<List<MovieRevenue>> getTopRevenueMovies() {
        MyResponse<List<MovieRevenue>> myResponse = new MyResponse<>();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("seats"),
                Aggregation.group("showTime.movie._id")
                        .first("showTime.movie.name").as("movieName")
                        .sum("showTime.price").as("totalRevenue"),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "totalRevenue")),
                Aggregation.project("showTime.movie._id","movieName", "totalRevenue"),
                Aggregation.limit(3));

        AggregationResults<MovieRevenue> results = mongoTemplate.aggregate(aggregation, "tickets", MovieRevenue.class);
        myResponse.setStatus(200);
        myResponse.setMessage("Lấy danh sách thành công");
        List<MovieRevenue> movieRevenues = results.getMappedResults();
        for (MovieRevenue movieRevenue : movieRevenues) {
            movieRevenue.setMovieId(movieRevenue.get_id().toString());
            movieRevenue.set_id(null);
        }
        myResponse.setData(movieRevenues);
        return myResponse;
    }

}
