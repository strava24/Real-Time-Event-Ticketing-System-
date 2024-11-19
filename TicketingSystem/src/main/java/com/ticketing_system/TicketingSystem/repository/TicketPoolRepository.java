package com.ticketing_system.TicketingSystem.repository;

import com.ticketing_system.TicketingSystem.model.TicketPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketPoolRepository extends JpaRepository<TicketPool, Integer> {

    @Query("SELECT tp FROM TicketPool tp WHERE tp.event.eventID = :eventId")
    List<TicketPool> findByEventId(@Param("eventId") int eventId); // One event can have multiple ticket pools so returning a list

}