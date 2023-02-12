package ca.kdunn4781.assignment1.output;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.ActivityOutputBinding;
import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.location.Location;

public class OutputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.output_activity);

        ActivityOutputBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_output);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Object[] locationsArray = (Object[]) extras.get("locations");
            List<Location> locations = new ArrayList<>();
            for (Object location : locationsArray) {
                locations.add((Location) location);
            }
            int cost = (locations.size() - 1) * 50;
            String adultCountString = extras.getString("adultCount");
            String childrenCountString = extras.getString("childrenCount");
            int adultCount = Integer.parseInt(adultCountString);
            int childrenCount = Integer.parseInt(childrenCountString);
            int totalCount = adultCount + childrenCount;
            int totalTravelTime = (locations.size() - 1) * 45;
            int totalDistanceTraveled = (locations.size() - 1) * 50;

            binding.pricePerPerson.setText("Cost Per Person: $" + String.valueOf(cost/totalCount));
            binding.totalCost.setText("Total Cost: $" + String.valueOf(cost));
            binding.totalDistanceTraveled.setText("Total Distance Traveled: " + String.valueOf(totalDistanceTraveled) + " km");

            binding.totalTimeTraveled.setText("Total Time Traveled: " + String.valueOf(totalTravelTime) + " minutes");
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