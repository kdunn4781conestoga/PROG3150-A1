package ca.kdunn4781.assignment1.output;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.ActivityOutputBinding;
import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.location.Location;
import ca.kdunn4781.assignment1.travel.Trip;
import ca.kdunn4781.assignment1.travel.TripViewModel;

/**
 * This activity shows the output of the calculations
 */

public class OutputActivity extends AppCompatActivity {

    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.output_activity);

        ActivityOutputBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_output);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getTripLiveData().observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(Trip trip) {
                if (trip != null) {
                    int tripCount = trip.getTripPoints().size();
                    int cost = (tripCount - 1) * 50;
                    int totalCount = trip.getNumOfAdults() + trip.getNumOfChildren();
                    int totalTravelTime = (tripCount - 1) * 45;
                    int totalDistanceTraveled = (tripCount - 1) * 50;

                    binding.pricePerPerson.setText("Cost Per Person: $" + String.valueOf(cost/totalCount));
                    binding.totalCost.setText("Total Cost: $" + String.valueOf(cost));
                    binding.totalDistanceTraveled.setText("Total Distance Traveled: " + String.valueOf(totalDistanceTraveled) + " km");

                    binding.totalTimeTraveled.setText("Total Time Traveled: " + String.valueOf(totalTravelTime) + " minutes");
                }
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("tripId")) {
            tripViewModel.getTripById(extras.getInt("tripId"));
        }

        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OutputActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}