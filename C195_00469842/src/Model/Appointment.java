package Model;

import java.time.ZonedDateTime;

public class Appointment {
    private int appointmentID;
    private String customer;
    private String user;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;

    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    //Constructors


    public Appointment() {
        appointmentID = 0;
        customer = null;
        user = null;
        title = null;
        description = null;
        location = null;
        contact = null;
        type = null;
        startDateTime = null;
        endDateTime = null;
    }

    public Appointment(int appointmentID, String customer, String user, String title, String description, String location, String contact, String type, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        this.appointmentID = appointmentID;
        this.customer = customer;
        this.user = user;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    //Getters and Setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
