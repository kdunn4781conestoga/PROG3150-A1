package ca.kdunn4781.assignment1.location;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {
    private final LocationRepository locationRepository;

    public LocationViewModel(@NonNull Application application)
    {
        super(application);

        this.locationRepository = new LocationRepository(application);
    }

    public LiveData<List<Location>> loadLocations() {
        return locationRepository.loadLocations();
    }

    public LiveData<List<Location>> getLocations() {
        return locationRepository.getLocations();
    }
}
