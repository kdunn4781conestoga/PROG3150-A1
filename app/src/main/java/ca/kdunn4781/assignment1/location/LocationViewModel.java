package ca.kdunn4781.assignment1.location;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ca.kdunn4781.assignment1.database.AppDatabase;

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
}
