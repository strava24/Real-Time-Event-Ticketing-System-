package com.ticketing_system.TicketingSystem.service;

import com.ticketing_system.TicketingSystem.DTO.EventDTO;
import com.ticketing_system.TicketingSystem.model.Event;
import com.ticketing_system.TicketingSystem.model.Vendor;
import com.ticketing_system.TicketingSystem.repository.EventRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private static final Logger logger = LogManager.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private VendorService vendorService;

    /**
     * Method to get all the events on the system
     * @return - List of event data transfer objects on the system
     */
    public List<EventDTO> getAllEvents() {
        logger.info("Retrieving all events");
        return eventRepo.findAll().stream()
                .map(event -> new EventDTO(
                        event.getEventID(),
                        event.getEventName(),
                        event.getVendor().getVendorID(),
                        event.getEventDate().toString(),
                        event.getLocation()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Method to create an event
     * @param event - Event object
     * @return - The ID of the event
     */
    public int createEvent(Event event) {
        eventRepo.save(event);
        event.getVendor().addNewEvent(event);
        logger.info("Created an event with ID: E{}", event.getEventID());
        return event.getEventID();
    }


//    public int createEvent(String eventName, Vendor vendor, String date, String location) {
//        Event event = new Event(eventName, date, vendor, location);
//        eventRepo.save(event);
//        vendor.addNewEvent(event);
//
//        return event.getEventID();
//    }

    /**
     * Method to get an event by ID
     * @param eventID - Event ID
     * @return - Event
     */
    public Event getEventByID(int eventID) {
        return eventRepo.findById(eventID).orElse(null);
    }

    /**
     * Method to get a data transfer version of event
     * @param eventID - Event ID
     * @return - Event data transfer object
     */
    public EventDTO getEvenDTOByID(int eventID) {
        Event event = eventRepo.findById(eventID).orElse(null);

        if (event == null) {
            return null;
        } else {

            return new EventDTO(
                    event.getEventID(),
                    event.getEventName(),
                    event.getVendor().getVendorID(),
                    event.getEventDate().toString(),
                    event.getLocation(),
                    event.getImageName(),
                    event.getImageType(),
                    event.getImageData());
        }

    }

//    public int getEventIDByName(String eventName) {
//        return eventRepo.findEventIDByEventName(eventName);
//    }

    /**
     * Method get an event object from an Event DTO
     * @param eventDTO - Event data transfer object
     * @return - The event object
     */
    public Event convertToEventFromDTO(EventDTO eventDTO) {
        Vendor vendor = vendorService.getVendorByID(eventDTO.getVendorID());

        if (vendor == null) {
            return null;
        } else {
            return new Event(eventDTO.getEventName(), eventDTO.getDate().toString(), vendor, eventDTO.getLocation(), eventDTO.getImageName(), eventDTO.getImageData(), eventDTO.getImageType());
        }

    }

    /**
     * Method to get all of the events hosted by a vendor
     * @param vendorID + Vendor ID
     * @return - List of event data transfer objects of hhe events hosted by a vendor
     */
    public List<EventDTO> getAllEventsByVendorID(int vendorID) {

        logger.info("Retrieving all events by Vendor ID V{}", vendorID);

        return eventRepo.findByVendorId(vendorID).stream()
                .map(event -> new EventDTO(
                        event.getEventID(),
                        event.getEventName(),
                        event.getVendor().getVendorID(),
                        event.getEventDate().toString(),
                        event.getLocation()
                ))
                .collect(Collectors.toList());

    }

    /**
     * Method to update event
     * @param event - Event Object
     */
    public void updateEvent(Event event) {
        eventRepo.save(event);
    }

    /**
     * Method to delete event
     * @param event - Event object
     */
    public void deleteEvent(Event event) {
        eventRepo.delete(event);
    }

    /**
     * Method to create an event with image data
     * @param eventDTO - Event data transfer object
     * @param imageFile - Image file
     * @return - The new event object
     * @throws IOException - Possibility of IOException cause working with image files
     */
    public Event createEvent(EventDTO eventDTO, MultipartFile imageFile) throws IOException {
        eventDTO.setImageName(imageFile.getOriginalFilename());
        eventDTO.setImageType(imageFile.getContentType());
        eventDTO.setImageData(imageFile.getBytes());

        Event event = convertToEventFromDTO(eventDTO);
        System.out.println(event);
        logger.info("Created an event with image data");
        return eventRepo.save(event);
    }

    /**
     * Method to update an event with image data
     * @param eventDTO - Event data transfer object
     * @param imageFile - Image file
     * @return - Event object
     * @throws IOException - Possibility of IOException cause working with image files
     */
    public Event updateEvent(EventDTO eventDTO, MultipartFile imageFile) throws IOException {

        System.out.println("Event ID : " + eventDTO.getEventID());

        eventDTO.setImageName(imageFile.getOriginalFilename());
        eventDTO.setImageType(imageFile.getContentType());
        eventDTO.setImageData(imageFile.getBytes());

        Event event = convertToEventFromDTO(eventDTO);
        event.setEventID(eventDTO.getEventID());
        System.out.println(event);
        logger.info("Updated an event with image data");
        return eventRepo.save(event);
    }

}
