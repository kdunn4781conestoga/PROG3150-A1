package ca.kdunn4781.assignment1.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import java.time.LocalDateTime;

import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.ActivityLocationBinding;

/**
 * This activity shows a list of locations that the user can add or remove
 */
public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLocationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_location);

        // binds adapter to list
        LocationListAdapter locationListAdapter = new LocationListAdapter(this, getLayoutInflater());
        binding.listLocations.setAdapter(locationListAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();

            // add locations that were selected in the first activity
            Location startLocation = new Location(extras.getString("startLocation", ""), LocalDateTime.now());
            Location endLocation = new Location(extras.getString("endLocation", ""), null);

            locationListAdapter.addLocation(0, startLocation);
            locationListAdapter.addLocation(1, endLocation);
        }
    }
}