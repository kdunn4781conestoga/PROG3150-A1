package ca.kdunn4781.assignment1.output;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripRepository;

/**
 * ViewModel for OutputFragment
 */
public class OutputViewModel extends AndroidViewModel {
    private final TripRepository tripRepository;

    public OutputViewModel(@NonNull Application application)
    {
        super(application);
        this.tripRepository = new TripRepository(application);
    }

    public LiveData<Trip> getTripById(int id) { return tripRepository.loadTripById(id); }
}
