package com.ticketing_system.TicketingSystem.repository;

import com.ticketing_system.TicketingSystem.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
}
