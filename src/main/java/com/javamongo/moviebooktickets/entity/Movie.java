package com.javamongo.moviebooktickets.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "movies")
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    private String id; // Mã phim
    private String name; // Tên phim
    private String description; // Mô tả
    @Min(60)
    private int duration; // Thời lượng
    private List<String> languages; // Ngôn ngữ
    private Date releaseDate; // Ngày công chiếu
    private String poster; // Ảnh poster
    private String trailer; // Link trailer (frame youtube)
    private List<String> categories; // Nhúng danh sách thể loại
}
