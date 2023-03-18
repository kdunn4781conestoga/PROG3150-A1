package ca.kdunn4781.assignment1.trip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Duration;
import java.time.LocalDateTime;

import ca.kdunn4781.assignment1.location.Location;

@Entity(
        tableName = "tripPoints",
        foreignKeys = {
                @ForeignKey(
                    entity = Trip.class,
                    parentColumns = "id",
                    childColumns = "travelId",
                    onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                    entity = Location.class,
                    parentColumns = "id",
                    childColumns = "locationId"
                )
        }
)
public class TripPoint {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(index = true)
    public int travelId;
    @ColumnInfo(index = true)
    public int locationId;

    private int index;

    /** The previous location */
    @Nullable
    private Integer prevPointIndex;

    /** The next location */
    @Nullable
    private Integer nextPointIndex;

    @NonNull
    @Ignore
    private Location location;

    /** The distance to travel to the next location */
    private float distanceToTravel;

    /** The arrival date */
    private LocalDateTime arrivalDate;

    /** The departure date */
    private LocalDateTime departDate;

    /** The cost to travel to the destination */
    private double costToLocation;

    /** The time to travel to the next location */
    private Duration timeToTravel;

    public TripPoint() {
        this(-1, new Location("Invalid"), 0);
    }

    public TripPoint(int index, @NonNull Location location, float distanceToTravel) {
        this(index, null, null, location, distanceToTravel, null, null);
    }

    public TripPoint(int index, @Nullable TripPoint prevPoint, @Nullable TripPoint nextPoint, @NonNull Location location, float distanceToTravel, LocalDateTime arrivalDate, LocalDateTime departDate) {
        this.id = 0;
        this.travelId = 0;
        this.locationId = location.id;
        this.index = index;
        this.prevPointIndex = prevPoint == null ? null : prevPoint.index;
        this.nextPointIndex = nextPoint == null ? null : nextPoint.index;
        this.location = location;
        this.distanceToTravel = distanceToTravel;
        this.arrivalDate = arrivalDate;
        this.departDate = departDate;
        this.costToLocation = 0;
        this.timeToTravel = Duration.ZERO;
    }

    ///////// GETTERS AND SETTERS /////////


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Nullable
    public Integer getPrevPointIndex() {
        return prevPointIndex;
    }

    public void setPrevPointIndex(@Nullable Integer prevPointIndex) {
        this.prevPointIndex = prevPointIndex;
    }

    public void setPrevPoint(@Nullable TripPoint prevPoint) {
        if (prevPoint != null)
            setPrevPointIndex(prevPoint.index);
        else
            setPrevPointIndex(null);
    }

    @Nullable
    public Integer getNextPointIndex() {
        return nextPointIndex;
    }

    public void setNextPointIndex(@Nullable Integer nextPointIndex) {
        this.nextPointIndex = nextPointIndex;
    }

    public void setNextPoint(@Nullable TripPoint nextPoint) {
        if (nextPoint != null)
            setNextPointIndex(nextPoint.index);
        else
            setNextPointIndex(null);
    }

    @NonNull
    public Location getLocation() {
        return location;
    }

    public void setLocation(@NonNull Location location) {
        this.location = location;
    }

    public double getCostToLocation() {
        return costToLocation;
    }

    public void setCostToLocation(double costToLocation) {
        this.costToLocation = costToLocation;
    }

    public float getDistanceToTravel() {
        return distanceToTravel;
    }

    public void setDistanceToTravel(float distanceToTravel) {
        this.distanceToTravel = distanceToTravel;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime calendar) {
        this.arrivalDate = calendar;
    }

    public LocalDateTime getDepartDate() {
        return departDate;
    }

    public void setDepartDate(LocalDateTime calendar) {
        this.departDate = calendar;
    }

    public Duration getTimeToTravel() {
        return timeToTravel;
    }

    public void setTimeToTravel(Duration timeToTravel) {
        this.timeToTravel = timeToTravel;
    }
}
