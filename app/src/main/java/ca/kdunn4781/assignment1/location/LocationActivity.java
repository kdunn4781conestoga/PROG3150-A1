package ca.kdunn4781.assignment1.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.time.LocalDateTime;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.ActivityLocationBinding;
import ca.kdunn4781.assignment1.output.OutputActivity;
import ca.kdunn4781.assignment1.travel.OnTripPointClickListener;
import ca.kdunn4781.assignment1.travel.Trip;
import ca.kdunn4781.assignment1.travel.TripPointListAdapter;
import ca.kdunn4781.assignment1.travel.TripViewModel;

/**
 * This activity shows a list of locations that the user can add or remove
 */
public class LocationActivity extends AppCompatActivity {

    Bundle extras;
    TripViewModel tripViewModel;
    LocationViewModel locationViewModel;

    TripPointListAdapter locationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.location_activity);

        ActivityLocationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_location);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel.getLocationListLiveData().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                if (locations != null && !locations.isEmpty()) {
                    // binds adapter to list
                    locationListAdapter = new TripPointListAdapter(
                            LocationActivity.this,
                            getLayoutInflater(),
                            new OnTripPointClickListener() {
                                @Override
                                public void onAddPointClickListener(View view, int position) {
                                    // shows a dialog for selecting a list of locations
                                    AlertDialog.Builder b = new AlertDialog.Builder(LocationActivity.this);
                                    b.setTitle("Add Location");
                                    b.setItems(locations.stream().map(Location::getName).toArray(CharSequence[]::new), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                            tripViewModel.addTripPoint(position + 1, locations.get(which));
                                        }
                                    });
                                    b.show();
                                }

                                @Override
                                public void onRemovePointClickListener(View view, int position) {
                                    tripViewModel.removeTripPoint(position);
                                }

                                @Override
                                public void onDetailPointClickListener(View view, int position) {
                                    // TODO for showing details about location
                                }
                            }
                    );
                    binding.listLocations.setAdapter(locationListAdapter);
                }
            }
        });

        tripViewModel.getTripLiveData().observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(Trip travel) {
                if (travel != null && travel.getTripPoints() != null)
                    locationListAdapter.setList(travel.getTripPoints());
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            extras = intent.getExtras();

            if (extras.containsKey("tripId"))
            {
                tripViewModel.getTripById(extras.getInt("tripId"));
            }
        }

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(LocationActivity.this, OutputActivity.class); // change MainActivity to last activity
                if (extras != null)
                {
                    intent.putExtras(extras);
                }

                startActivity(intent);
            }
        });
    }
}