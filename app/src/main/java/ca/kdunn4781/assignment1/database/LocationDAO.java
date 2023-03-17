package ca.kdunn4781.assignment1.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ca.kdunn4781.assignment1.location.Location;

@Dao
public interface LocationDAO {
    @Query("SELECT * FROM locations")
    LiveData<List<Location>> loadAllLocations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocations(Location... locations);

    @Query("SELECT * FROM locations WHERE id = :id")
    Location findLocationById(int id);
}
