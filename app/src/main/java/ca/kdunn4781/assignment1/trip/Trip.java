package ca.kdunn4781.assignment1.trip;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.kdunn4781.assignment1.location.Location;

/**
 * This class holds information about the trip
 */
@Entity(tableName = "trips")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    @Nullable
    private String description;

    private int numOfAdults;
    private int numOfChildren;

    @Ignore
    private List<TripPoint> travelPoints;

    public Trip() {
        this(0, "Default", null, 1, 0, new ArrayList<>());
    }

    public Trip(@NonNull String name, @Nullable String description, @NonNull Location startLocation, @Nullable Location endLocation)
    {
        this(0, name, description, 1, 0, new ArrayList<>());

        setTravelPoints(startLocation, endLocation);
    }

    private Trip(int id, @NonNull String name, @Nullable String description, int numOfAdults, int numOfChildren, @NonNull List<TripPoint> travelPoints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numOfAdults = numOfAdults;
        this.numOfChildren = numOfChildren;
        this.travelPoints = travelPoints;
    }

    /**
     * This function sets the start and end travel points
     *
     * @param startLocation the start location
     * @param endLocation the end location
     */
    public void setTravelPoints(Location startLocation, Location endLocation) {
        Log.d("Trip", "Setting start/end travel points for trip " + name);

        TripPoint startPoint = new TripPoint(0, startLocation, 50);
        startPoint.setArrivalDate(LocalDateTime.now());
        startPoint.setDepartDate(startPoint.getArrivalDate());

        TripPoint endPoint;
        if (endLocation == null)
            endPoint = startPoint;
        else
            endPoint = new TripPoint(1, endLocation, 50);

        startPoint.setNextPoint(endPoint);
        endPoint.setPrevPoint(startPoint);

        // prevents adding more points than needed
        if (getPoint(0) != null) {
            travelPoints.set(0, startPoint);

            revalidate();
        } else {
            addTravelPoint(0, startPoint);
        }

        if (getPoint(1) != null) {
            travelPoints.set(1, endPoint);

            revalidate();
        } else {
            addTravelPoint(1, endPoint);
        }

        calculateDistances();
    }

    /**
     * This function adds a trip point to the trip
     *
     * @param index the index to add
     * @param point the point to add
     */
    public void addTravelPoint(int index, TripPoint point) {
        point.setIndex(index);

        travelPoints.add(index, point);

        revalidate();

        calculateDistances();
    }

    /**
     * This function removes a trip point to the trip
     *
     * @param index the index to remove
     */
    public void removeTripPoint(int index) {
        travelPoints.remove(index);

        revalidate();

        calculateDistances();
    }

    /**
     * This function gets the point at the index
     *
     * @param index the index of the point
     * @return the point
     */
    public TripPoint getPoint(int index) {
        if (index < 0 || index >= travelPoints.size())
            return null;

        return travelPoints.get(index);
    }

    /**
     * This function revalidates all trip points to have the values correct
     */
    private void revalidate() {
        for (int i = 0; i < travelPoints.size(); i++) {
            TripPoint point = getPoint(i);

            TripPoint prevPoint = null;
            TripPoint nextPoint = null;

            prevPoint = getPoint(i - 1);
            point.setPrevPoint(prevPoint);

            nextPoint = getPoint(i + 1);
            point.setNextPoint(nextPoint);

            if (prevPoint != null) {
                prevPoint.setNextPoint(point);
                prevPoint.setIndex(i - 1);
            }

            if (nextPoint != null) {
                nextPoint.setPrevPoint(point);
                nextPoint.setIndex(i + 1);
            }
        }
    }

    /**
     * This functions calculates the distances from one location to another.
     *
     * *Note:* these are fake distances and aren't reliable in any way
     */
    private void calculateDistances() {
        TripPoint prevPoint = null;

        for (int i = 0; i < travelPoints.size(); i++) {
            TripPoint currentPoint = travelPoints.get(i);
            currentPoint.setTimeToTravel(null);

            if (prevPoint != null) {
                Duration duration = Duration.ofMinutes((long)(50 * 0.9));
                prevPoint.setTimeToTravel(duration);
                prevPoint.setCostToLocation(50);

                currentPoint.setArrivalDate(prevPoint.getDepartDate().plusMinutes(duration.toMinutes()));
            }

            prevPoint = currentPoint;
            prevPoint.setDepartDate(prevPoint.getArrivalDate());
        }
    }


    ///////// GETTERS AND SETTERS /////////


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;

        for (TripPoint p : travelPoints) {
            p.travelId = id;
        }
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public int getNumOfAdults() {
        return numOfAdults;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
    }

    public List<TripPoint> getTripPoints() {
        return travelPoints;
    }
}
