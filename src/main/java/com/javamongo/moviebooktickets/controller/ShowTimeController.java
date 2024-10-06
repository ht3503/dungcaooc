package com.javamongo.moviebooktickets.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javamongo.moviebooktickets.dto.ShowTimeDto;
import com.javamongo.moviebooktickets.service.ShowTimeService;

@RestController
@RequestMapping("/api/v1/showtime")
public class ShowTimeController {

    @Autowired
    private ShowTimeService showTimeService;

    @GetMapping
    public ResponseEntity<?> getAllShowTime(@RequestParam LocalDate showTimeDate, @RequestParam String roomId, @RequestParam String movieId) {
        return ResponseEntity.ok(showTimeService.getAllShowTime(showTimeDate, roomId, movieId));
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<?> addShowTime(@RequestBody ShowTimeDto showTime) {
        return ResponseEntity.ok(showTimeService.addShowTime(showTime));
    }

}
