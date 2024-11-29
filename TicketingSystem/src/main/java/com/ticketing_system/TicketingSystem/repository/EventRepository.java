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

    @Query("SELECT e.eventID FROM Event e WHERE e.eventName = :eventName")
    int findEventIDByEventName(String eventName);

//    /**
//     * A custom query to get the number of tickets sold for the event
//     * Logic : For an event there can be multiple pools to simulate situations like early bird tickets, on-site and etc.
//     * The maxTicketCapacity of an event will be the maximum amount of tickets for an event
//     * Ex:
//     * Lets say that there are 5000 tickets for an event and the vendor can plan to release those tickets in like 2000 (Early bird tickets),
//     * 2000(normal tickets) and now there are 1000 more tickets available for the event
//     * This method will return 3000 (No of tickets sold)
//     * @param eventId - eventId for which we need the ticket count
//     * @return the number tickets sold for an event
//     */
//    @Query("SELECT COALESCE(SUM(tp.totalTickets), 0) FROM DummyTicketPool tp WHERE tp.event.id = :eventId")
//    int findTotalTicketsByEvent(@Param("eventId") int eventId);


}
