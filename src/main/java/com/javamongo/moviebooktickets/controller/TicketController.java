package com.javamongo.moviebooktickets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javamongo.moviebooktickets.dto.TicketDto;
import com.javamongo.moviebooktickets.service.TicketService;

@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
    public ResponseEntity<?> getAllTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(ticketService.getAllTickets(email));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
    public ResponseEntity<?> addTicket(@RequestBody TicketDto ticket) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ticket.setEmail(email);
        return ResponseEntity.ok(ticketService.addTicket(ticket));
    }

    // Kiểm tra ghế đã được đặt chưa
    @GetMapping("/checkseat/{showTimeId}/{seat}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
    public ResponseEntity<?> checkSeat(@PathVariable String showTimeId, @PathVariable String seat) {
        return ResponseEntity.ok(ticketService.checkSeat(showTimeId, seat));
    }

    // Hủy vé
    @GetMapping("/cancel/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Customer')")
    public ResponseEntity<?> cancelTicket(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.cancelTicket(id));
    }
    // Thống kê doanh thu theo n ngày gần nhất
    @GetMapping("/revenue/{n}")
    @PreAuthorize("hasAnyAuthority('Admin')")
    public ResponseEntity<?> getRevenue(@PathVariable int n) {
        return ResponseEntity.ok(ticketService.getRevenue(n));
    }

    //Khách hàng đặt vé nhiều nhất (email)
    @GetMapping("/most-customer")
    public ResponseEntity<?> getMostCustomer() {
        return ResponseEntity.ok(ticketService.getMostCustomer());
    }
}
