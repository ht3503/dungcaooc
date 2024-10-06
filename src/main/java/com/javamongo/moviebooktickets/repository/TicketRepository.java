package com.javamongo.moviebooktickets.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javamongo.moviebooktickets.entity.Ticket;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByEmail(String email);
    List<Ticket> findByShowTimeId(String showTimeId);
}
