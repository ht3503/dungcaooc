package com.javamongo.moviebooktickets.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private List<String> seats; // Danh sách ghế
    private String showTimeId; // Mã lịch chiếu
    private String email; // Email người dùng đặt vé
}
