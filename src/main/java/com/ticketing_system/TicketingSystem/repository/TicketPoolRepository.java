package com.ticketing_system.TicketingSystem.repository;

import com.ticketing_system.TicketingSystem.model.TicketPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketPoolRepository extends JpaRepository<TicketPool, Integer> {
}
