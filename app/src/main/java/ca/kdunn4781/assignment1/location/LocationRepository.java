package ca.kdunn4781.assignment1.location;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.api.ApiService;
import ca.kdunn4781.assignment1.database.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This repository contains functions for accessing data pertaining to locations
 */
public class LocationRepository {
    private AppDatabase appDatabase;
    private final LiveData<List<Location>> locationsLiveData;

    public LocationRepository(@NonNull Context context) {
        this.appDatabase = AppDatabase.getAppDatabase(context);
        this.locationsLiveData = appDatabase.locationDAO().loadAllLocations();
    }

    /**
     * This function creates the retrofit api service
     *
     * @return the api service
     */
    public ApiService invoke() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ApiService.class);
    }

    /**
     * This function gets the list of locations
     * @return the livedata
     */
    public LiveData<List<Location>> getLocations() { return locationsLiveData; }

    /**
     * This function loads the locations from the server or default values
     *
     * @return the livedata
     */
    public LiveData<List<Location>> loadLocations() {
        MutableLiveData<List<Location>> liveData = new MutableLiveData<>();

        ApiService service = invoke();
        Call<List<Location>> call = service.getLocations();
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(@NonNull Call<List<Location>> call, @NonNull Response<List<Location>> response) {
                // populates the database with locations
                List<Location> locations = null;
                if (response.isSuccessful() && response.body() != null) {
                    locations = response.body();

                    locationsLiveData.observeForever(new Observer<List<Location>>() {
                        @Override
                        public void onChanged(List<Location> locations) {
                            liveData.postValue(locations);

                            locationsLiveData.removeObserver(this);
                        }
                    });

                    populateLocations(locations.toArray(new Location[0]));
                } else {
                    locationsLiveData.observeForever(new Observer<List<Location>>() {
                        @Override
                        public void onChanged(List<Location> locations) {
                            liveData.postValue(locations);

                            locationsLiveData.removeObserver(this);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Location>> call, @NonNull Throwable t) {
                Log.d("LocationRepository", t.getLocalizedMessage());

                locationsLiveData.observeForever(new Observer<List<Location>>() {
                    @Override
                    public void onChanged(List<Location> locations) {
                        liveData.postValue(locations);

                        locationsLiveData.removeObserver(this);
                    }
                });
            }
        });

        return liveData;
    }

    /**
     * This function populates the database with the provided locations
     *
     * @param locations the locations to insert
     */
    public void populateLocations(Location... locations) {
        AsyncTask.execute(() -> {
            appDatabase.locationDAO().insertLocations(locations);
        });
    }
}
