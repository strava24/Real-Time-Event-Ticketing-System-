package com.ticketing_system.TicketingSystem.repository;

import com.ticketing_system.TicketingSystem.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Integer> {

    /**
     * a JPQL query to get the amount of events a vendor has hosted
     * @param vendorId - vendor ID to see the number of events he has hosted
     * @return the number of events a vendor has hosted
     */
    @Query("SELECT COUNT(e) FROM Event e WHERE e.vendor.vendorID = :vendorId")
    int countByVendorId(@Param("vendorId") int vendorId);

}
