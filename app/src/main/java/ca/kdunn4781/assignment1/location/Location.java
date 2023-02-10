package ca.kdunn4781.assignment1.location;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * This class stores information pertaining to a location
 */
public class Location implements Serializable {
    /** The location name */
    private String location;
    /** The arrival date */
    private LocalDateTime date;

    /** The distance to travel to the next location */
    private float distanceToTravel;

    /** The cost to travel to the destination */
    private double costToLocation;

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
        this.distanceToTravel = 50;
    }

    ///////// GETTERS AND SETTERS /////////


    public double getCostToLocation() {
        return costToLocation;
    }

    public void setCostToLocation(double costToLocation) {
        this.costToLocation = costToLocation;
    }

    public float getDistanceToTravel() {
        return distanceToTravel;
    }

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
