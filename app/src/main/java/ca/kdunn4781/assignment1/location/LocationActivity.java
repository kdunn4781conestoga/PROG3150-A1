package ca.kdunn4781.assignment1.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.time.LocalDateTime;

import ca.kdunn4781.assignment1.output.OutputActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.ActivityLocationBinding;

/**
 * This activity shows a list of locations that the user can add or remove
 */
public class LocationActivity extends AppCompatActivity {

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.location_activity);

        ActivityLocationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_location);

        // binds adapter to list
        LocationListAdapter locationListAdapter = new LocationListAdapter(this, getLayoutInflater());
        binding.listLocations.setAdapter(locationListAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            extras = intent.getExtras();

            // add locations that were selected in the first activity
            Location startLocation = new Location(extras.getString("startLocation", ""), LocalDateTime.now());
            Location endLocation = new Location(extras.getString("endLocation", ""), null);

            locationListAdapter.addLocation(0, startLocation);
            locationListAdapter.addLocation(1, endLocation);
        }

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(LocationActivity.this, OutputActivity.class); // change MainActivity to last activity
                if (extras != null)
                {
                    intent.putExtras(extras);
                }

                // add all locations
                intent.putExtra("locations", locationListAdapter.getLocations().toArray());

                startActivity(intent);
            }
        });
    }
}