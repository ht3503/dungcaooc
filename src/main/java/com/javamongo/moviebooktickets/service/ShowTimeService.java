package com.javamongo.moviebooktickets.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javamongo.moviebooktickets.dto.MyResponse;
import com.javamongo.moviebooktickets.dto.ShowTimeDto;
import com.javamongo.moviebooktickets.entity.Movie;
import com.javamongo.moviebooktickets.entity.ShowTime;
import com.javamongo.moviebooktickets.repository.MovieRepository;
import com.javamongo.moviebooktickets.repository.ShowTimeRepository;

@Service
public class ShowTimeService {

    @Autowired
    private ShowTimeRepository showTimeRepository;
    @Autowired
    private MovieRepository movieRepository;

    public MyResponse<ShowTime> addShowTime(ShowTimeDto showTime) {
        MyResponse<ShowTime> myResponse = new MyResponse<>();
        // Kiểm tra ngày chiếu phải lớn hơn hoặc bằng ngày hiện tại
        if (showTime.getShowTimeDate().isBefore(LocalDate.now())) {
            myResponse.setStatus(400);
            myResponse.setMessage("Ngày chiếu phải lớn hơn hoặc bằng ngày hiện tại");
            return myResponse;
        }
        // Kiểm tra giờ bắt đầu phải lớn hơn giờ hiện tại
        if (showTime.getShowTimeDate().isEqual(LocalDate.now())
                && showTime.getStartTime().isBefore(java.time.LocalTime.now())) {
            myResponse.setStatus(400);
            myResponse.setMessage("Giờ bắt đầu phải lớn hơn giờ hiện tại");
            return myResponse;
        }
        // Kiểm tra phim có tồn tại không
        Movie movie = movieRepository.findById(showTime.getMovieId()).orElse(null);
        if (movie == null) {
            myResponse.setStatus(404);
            myResponse.setMessage("Không tìm thấy phim");
            return myResponse;
        }
        // Kiểm tra giờ chiếu không được trùng ngày giờ với các showtime khác

        // Lọc các showtime có ngày và phòng chiếu giống với showtime mới thêm
        List<ShowTime> showTimes = showTimeRepository.findAll().stream()
                .filter(s -> s.getShowTimeDate().equals(showTime.getShowTimeDate()))
                .filter(s -> s.getRoomId().equals(showTime.getRoomId()))
                .collect(Collectors.toList());
        if (showTimes.size() > 0) {
            LocalTime sStartTime = showTime.getStartTime(); // Giờ bắt đầu
            LocalTime sEndTime = showTime.getStartTime().plusMinutes(movie.getDuration()); // Giờ kết thúc

            for (ShowTime s : showTimes) {
                LocalTime startTime = s.getStartTime(); // Giờ bắt đầu
                LocalTime endTime = s.getStartTime().plusMinutes(s.getMovie().getDuration()); // Giờ kết thúc
                boolean condition1 = (sStartTime.equals(startTime) || sStartTime.isAfter(startTime)) && (sStartTime.equals(endTime) || sStartTime.isBefore(endTime));
                boolean condition2 = (sEndTime.equals(startTime) || sEndTime.isAfter(startTime)) && (sEndTime.isBefore(endTime) || sEndTime.isBefore(endTime));

                // LocalTime a = LocalTime.of(19, 30);
                // LocalTime b = LocalTime.of(19, 40);
                // boolean condition3 = b.isAfter(a);

                if (condition1 || condition2) {
                    myResponse.setStatus(400);
                    myResponse.setMessage("Suất chiếu này có thể bị tràn sang suất chiếu: " + s.getId());
                    return myResponse;
                }
            }
        }
        ShowTime showTimeEntity = new ShowTime();
        showTimeEntity.setShowTimeDate(showTime.getShowTimeDate());
        showTimeEntity.setStartTime(showTime.getStartTime());
        showTimeEntity.setPrice(showTime.getPrice());
        showTimeEntity.setRoomId(showTime.getRoomId());
        showTimeEntity.setMovie(movie);

        myResponse.setStatus(200);
        myResponse.setMessage("Thêm showtime thành công");
        myResponse.setData(showTimeRepository.save(showTimeEntity));
        return myResponse;
    }

    public MyResponse<List<ShowTime>> getAllShowTime(LocalDate showTimeDate, String roomId, String movieId) {
        MyResponse<List<ShowTime>> myResponse = new MyResponse<>();
        // Lọc các showtime có ngày chiếu bắt đầu từ hôm nay trở đi
        List<ShowTime> showTimes = showTimeRepository.findAll().stream()
                .filter(s -> s.getShowTimeDate().isAfter(LocalDate.now()/* .minusDays(1) */) || s.getShowTimeDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());
        if (showTimeDate != null) {
            showTimes = showTimes.stream()
            .filter(s -> s.getShowTimeDate().equals(showTimeDate))
            .collect(Collectors.toList());
            // for (ShowTime item : showTimes) {
            //     if (!item.getShowTimeDate().equals(showTimeDate)) {
            //         showTimes.remove(item);
            //     }
            // }
        }
        if (roomId.length() > 0) {
            showTimes = showTimes.stream()
                    .filter(s -> s.getRoomId().equals(roomId))
                    .collect(Collectors.toList());
        }
        if (movieId.length() > 0) {
            showTimes = showTimes.stream()
                    .filter(s -> s.getMovie().getId().equals(movieId))
                    .collect(Collectors.toList());
        }
        myResponse.setStatus(200);
        myResponse.setMessage("Lấy thông tin suất chiếu thành công");
        myResponse.setData(showTimes);
        return myResponse;
    }

}
