package ca.kdunn4781.assignment1.location;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripRepository;

/**
 * ViewModel for ModifyLocationFragment
 */
public class ModifyLocationViewModel extends AndroidViewModel {
    private final LocationRepository locationRepository;
    private final TripRepository tripRepository;

    public ModifyLocationViewModel(@NonNull Application application)
    {
        super(application);
        this.locationRepository = new LocationRepository(application);
        this.tripRepository = new TripRepository(application);
    }

    public LiveData<Trip> getTripById(int id) { return tripRepository.loadTripById(id); }

    public void addTripPoint(int index, Location location) {
        tripRepository.addTripPoint(index, location);
    }

    public void removeTripPoint(int index) {
        tripRepository.removeTripPoint(index);
    }

    public LiveData<List<Location>> loadLocations() {
        return locationRepository.loadLocations();
    }

    public LiveData<List<Location>> getLocations() {
        return locationRepository.getLocations();
    }
}
