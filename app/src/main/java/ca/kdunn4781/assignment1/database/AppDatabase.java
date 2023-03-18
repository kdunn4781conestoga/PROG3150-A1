package ca.kdunn4781.assignment1.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ca.kdunn4781.assignment1.location.Location;
import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripPoint;

/**
 * This class is for the App's Room database
 */
@Database(entities = {Location.class, Trip.class, TripPoint.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase _singleton;

    /**
     * This function gets the instance of this class
     *
     * @param context the app context
     * @return the singleton
     */
    public static AppDatabase getAppDatabase(Context context) {
        if (_singleton == null)
        {
            _singleton = Room.databaseBuilder(context, AppDatabase.class, "trip-planner").build();
        }

        return _singleton;
    }

    public abstract LocationDAO locationDAO();
    public abstract TripDAO tripDAO();
}
