package ca.kdunn4781.assignment1.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripPoint;

/**
 * This is for the trips and tripPoints table in the Room database
 */
@Dao
public interface TripDAO {

    @Query("SELECT * FROM trips")
    LiveData<List<Trip>> loadAllTrips();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTrip(Trip travel);


    @Query(
            "SELECT * FROM trips " +
            "WHERE trips.id = :id"
    )
    Trip findByTripId(int id);


    @Update
    int updateTrip(Trip travel);

    @Delete
    int deleteTrip(Trip trip);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertPoints(TripPoint... travelPoints);

    @Query("SELECT * FROM tripPoints WHERE travelId = :travelId ORDER BY `index` ASC")
    List<TripPoint> getPointsById(int travelId);

    @Query("SELECT * FROM tripPoints WHERE travelId = :travelId ORDER BY `index` ASC")
    LiveData<List<TripPoint>> findPointsById(int travelId);

    @Update
    int updatePoints(TripPoint... points);

    @Delete
    int deletePoint(TripPoint point);
}
