package com.ticketing_system.TicketingSystem.repository;

import com.ticketing_system.TicketingSystem.model.TicketPool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketPoolRepository extends JpaRepository<TicketPool, Integer> {
}
