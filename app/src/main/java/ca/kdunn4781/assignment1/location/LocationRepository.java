package ca.kdunn4781.assignment1.location;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

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

public class LocationRepository {
    private AppDatabase appDatabase;
    private final LiveData<List<Location>> locationsLiveData;
    private final Location[] defaultLocations;

    public LocationRepository(@NonNull Context context) {
        this.appDatabase = AppDatabase.getAppDatabase(context);
        this.locationsLiveData = appDatabase.locationDAO().loadAllLocations();

        // loads default locations (in-case the api service failed to grab locations)
        String[] locations = context.getResources().getStringArray(R.array.locations);
        defaultLocations = new Location[locations.length];
        for (int i = 0; i < locations.length; i++) {
            Location l = new Location(locations[i]);
            l.id = i + 1;

            defaultLocations[i] = l;
        }
    }

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

    public LiveData<List<Location>> getLocations() { return locationsLiveData; }

    public LiveData<List<Location>> loadLocations() {
        ApiService service = invoke();
        Call<List<Location>> call = service.getLocations();
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(@NonNull Call<List<Location>> call, @NonNull Response<List<Location>> response) {
                List<Location> locations = null;
                if (response.isSuccessful() && response.body() != null) {
                    locations = response.body();
                    populateLocations(locations.toArray(new Location[0]));
                } else {
                    populateLocations(defaultLocations);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Location>> call, @NonNull Throwable t) {
                Log.d("LocationRepository", t.getLocalizedMessage());
                populateLocations(defaultLocations);
            }
        });

        return locationsLiveData;
    }

    private void populateLocations(Location... locations) {
        AsyncTask.execute(() -> {
            appDatabase.locationDAO().insertLocations(locations);
        });
    }
}