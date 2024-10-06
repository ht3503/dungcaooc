package com.javamongo.moviebooktickets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostBookedCustomers {
    private String email;
    private int totalTickets;
}
