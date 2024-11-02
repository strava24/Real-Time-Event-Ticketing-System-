package com.ticketing_system.TicketingSystem.repository;

import com.ticketing_system.TicketingSystem.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
}
