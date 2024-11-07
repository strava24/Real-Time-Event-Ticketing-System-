package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.model.TicketPool;
import com.ticketing_system.TicketingSystem.repository.TicketPoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketPoolService {

    @Autowired
    private TicketPoolRepository ticketPoolRepository;

    public void createTicketPool(TicketPool ticketPool) {
        ticketPoolRepository.save(ticketPool);
    }

}
