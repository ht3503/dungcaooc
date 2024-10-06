package com.javamongo.moviebooktickets.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowTimeDto {
    private LocalDate showTimeDate; // Ngày chiếu
    private LocalTime startTime; // Giờ chiếu
    private double price; // Giá vé
    private String movieId; // Thông tin phim
    private String roomId; // Mã phòng chiếu
}
