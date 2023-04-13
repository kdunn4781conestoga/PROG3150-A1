package ca.kdunn4781.assignment1.location;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * This class stores information pertaining to a location
 */
@Entity(
        tableName = "locations",
        indices = {
                @Index(value = "name", unique = true)
        }
)
public class Location {
    @PrimaryKey(autoGenerate = true)
    public int id;

    /** The location name */
    @ColumnInfo()
    private String name;

    private double latCoord;
    private double longCoord;

    /**
     * This constructor creates an instance with the location name and arrival date
     * @param name the location name
     */
    public Location(String name, double latCoord, double longCoord) {
        this.id = 0;
        this.name = name;
        this.latCoord = latCoord;
        this.longCoord = longCoord;
    }

    ///////// GETTERS AND SETTERS /////////

    public String getName() {
        return name;
    }

    public double getLatCoord() {
        return latCoord;
    }

    public double getLongCoord() {
        return longCoord;
    }

    public LatLng getCoord() { return new LatLng(latCoord, longCoord); }


    ///////// OVERRIDES //////////


    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
