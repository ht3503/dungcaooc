package com.javamongo.moviebooktickets.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "tickets")
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    private String id; // Mã vé
    private Date bookingDate; // Ngày đặt
    private List<String> seats; // Danh sách ghế
    private ShowTime showTime; // Thông tin lịch chiếu
    private String email; // Email người dùng đặt vé
}
