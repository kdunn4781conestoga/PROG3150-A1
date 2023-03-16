package ca.kdunn4781.assignment1.location;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

    /**
     * This constructor creates an instance with the location name and arrival date
     * @param name the location name
     */
    public Location(String name) {
        this.id = 0;
        this.name = name;
    }

    ///////// GETTERS AND SETTERS /////////

    public String getName() {
        return name;
    }


    ///////// OVERRIDES //////////


    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
