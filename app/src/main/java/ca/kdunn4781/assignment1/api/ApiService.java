package ca.kdunn4781.assignment1.api;

import java.util.List;

import ca.kdunn4781.assignment1.location.Location;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("locations")
    Call<List<Location>> getLocations();
}
