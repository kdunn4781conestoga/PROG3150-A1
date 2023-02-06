package ca.kdunn4781.assignment1.location;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * This class stores information pertaining to a location
 */
public class Location {
    /** The location name */
    private String location;
    /** The arrival date */
    private LocalDateTime date;

    /** The time to travel to the next location */
    private Duration timeToTravel;

    /** The next location */
    private Location nextLocation;

    /**
     * This constructor creates an instance with the location name
     * @param location the location name
     */
    public Location(String location) {
        this(location, null);
    }

    /**
     * This constructor creates an instance with the location name and arrival date
     * @param location the location name
     * @param date the arrival date
     */
    public Location(String location, LocalDateTime date) {
        this.location = location;
        this.date = date;
    }

    ///////// GETTERS AND SETTERS /////////

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime calendar) {
        this.date = calendar;
    }

    public Location getNextLocation() {
        return nextLocation;
    }

    public void setNextLocation(Location nextLocation) {
        this.nextLocation = nextLocation;
    }

    public Duration getTimeToTravel() {
        return timeToTravel;
    }

    public void setTimeToTravel(Duration timeToTravel) {
        this.timeToTravel = timeToTravel;
    }
}
