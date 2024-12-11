import java.time.LocalDate;

public class Event {

    private int eventID;
    private String eventName;
    private int vendorID; // Only vendor ID instead of full Vendor object
    private String date;
    private String location;

    public Event(int eventID, String eventName, int vendorID, String date, String location) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.vendorID = vendorID;
        this.date = date;
        this.location = location;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", eventName='" + eventName + '\'' +
                ", vendorID=" + vendorID +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
