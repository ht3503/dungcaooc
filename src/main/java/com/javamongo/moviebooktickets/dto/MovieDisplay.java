package com.javamongo.moviebooktickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

import com.javamongo.moviebooktickets.entity.ShowTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDisplay {
    private String name; // Tên phim
    private String description; // Mô tả
    private int duration; // Thời lượng
    private List<String> languages; // Ngôn ngữ
    private Date releaseDate; // Ngày công chiếu
    private String poster; // Ảnh poster
    private String trailer; // Link trailer (frame youtube)
    private List<String> categories; // Nhúng danh sách thể loại
    private List<ShowTime> showTimes; // Danh sách lịch chiếu
}
