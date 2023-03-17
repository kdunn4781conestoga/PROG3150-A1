package ca.kdunn4781.assignment1.trip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ca.kdunn4781.assignment1.location.Location;

public class TripViewModel extends AndroidViewModel {

    private TripRepository tripRepository;

    public TripViewModel(@NonNull Application application)
    {
        super(application);
        this.tripRepository = new TripRepository(application);
    }

    public LiveData<Trip> createTrip(String name, @Nullable String description, int numOfAdults, int numOfChildren, Location startLocation, Location endLocation)
    {
        return tripRepository.createTrip(name, description, numOfAdults, numOfChildren, startLocation, endLocation);
    }

    public LiveData<Trip> getTripById(int id) { return tripRepository.getTripById(id); }

    public void addTripPoint(int index, Location location) {
        tripRepository.addTripPoint(index, location);
    }

    public void removeTripPoint(int index) {
        tripRepository.removeTripPoint(index);
    }
}
