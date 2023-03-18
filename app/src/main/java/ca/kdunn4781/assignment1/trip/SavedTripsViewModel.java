package ca.kdunn4781.assignment1.trip;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ca.kdunn4781.assignment1.location.LocationRepository;

/**
 * ViewModel for SavedTripsFragment
 */
public class SavedTripsViewModel extends AndroidViewModel {
    private final TripRepository tripRepository;

    public SavedTripsViewModel(@NonNull Application applicationContext) {
        super(applicationContext);
        this.tripRepository = new TripRepository(applicationContext);
    }

    public LiveData<List<Trip>> loadTrips() {
        return tripRepository.loadTrips();
    }

    public LiveData<Trip> deleteTrip(Trip trip) { return tripRepository.deleteTrip(trip); }
}
