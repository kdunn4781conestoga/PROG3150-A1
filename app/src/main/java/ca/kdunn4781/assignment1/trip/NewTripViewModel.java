package ca.kdunn4781.assignment1.trip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ca.kdunn4781.assignment1.location.Location;
import ca.kdunn4781.assignment1.location.LocationRepository;
import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripRepository;

public class NewTripViewModel extends AndroidViewModel {

    private final LocationRepository locationRepository;
    private final TripRepository tripRepository;

    public NewTripViewModel(@NonNull Application application)
    {
        super(application);
        this.locationRepository = new LocationRepository(application);
        this.tripRepository = new TripRepository(application);
    }

    public LiveData<Trip> createTrip(String name, @Nullable String description, int numOfAdults, int numOfChildren, Location startLocation, Location endLocation)
    {
        return tripRepository.createTrip(name, description, numOfAdults, numOfChildren, startLocation, endLocation);
    }

    public LiveData<List<Location>> loadLocations() {
        return locationRepository.loadLocations();
    }

    public LiveData<Trip> getTripById(int id) { return tripRepository.getTripById(id); }

    public LiveData<Trip> updateTrip(Trip trip) { return tripRepository.updateTrip(trip); }
}
