package ca.kdunn4781.assignment1.travel;

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

@Entity(tableName = "trips")
public class Trip {
    @PrimaryKey
    public int id;

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

        addTravelPoint(0, startPoint);
        addTravelPoint(1, endPoint);

        calculateDistances();
    }

    private Trip(int id, @NonNull String name, @Nullable String description, int numOfAdults, int numOfChildren, @NonNull List<TripPoint> travelPoints) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numOfAdults = numOfAdults;
        this.numOfChildren = numOfChildren;
        this.travelPoints = travelPoints;
    }

    public void addTravelPoint(int index, TripPoint point) {
        point.setIndex(index);

        travelPoints.add(index, point);

        revalidate();

        calculateDistances();
    }

    public void removeTripPoint(int index) {
        travelPoints.remove(index);

        revalidate();

        calculateDistances();
    }

    public TripPoint getPoint(int index) {
        if (index < 0 || index >= travelPoints.size())
            return null;

        return travelPoints.get(index);
    }

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
