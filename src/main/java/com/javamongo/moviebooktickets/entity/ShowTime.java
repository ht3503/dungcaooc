package com.javamongo.moviebooktickets.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "showtimes")
@NoArgsConstructor
@AllArgsConstructor
public class ShowTime {
    @Id
    private String id; // Mã lịch chiếu
    private LocalDate showTimeDate; // Ngày chiếu
    private LocalTime startTime; // Giờ chiếu
    private double price; // Giá vé
    private Movie movie; // Thông tin phim
    private String roomId; // Mã phòng chiếu

}
