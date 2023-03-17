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
    private AppDatabase appDatabase;
    private final LiveData<List<Location>> locationListLiveData;

    public LocationViewModel(@NonNull Application application)
    {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(application);
        locationListLiveData = appDatabase.locationDAO().loadAllLocations();
    }

    public LiveData<List<Location>> getLocationListLiveData() {
        return locationListLiveData;
    }

    public void populateInitialLocations(String... locations) {
        AsyncTask.execute(() -> {
            for (String location : locations) {
                appDatabase.locationDAO().insertLocations(new Location(location));
            }
        });
    }
}
