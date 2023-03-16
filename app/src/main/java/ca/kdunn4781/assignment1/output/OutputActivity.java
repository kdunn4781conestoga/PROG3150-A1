package ca.kdunn4781.assignment1.output;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.kdunn4781.assignment1.Connection;
import ca.kdunn4781.assignment1.Network;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.Shop;
import ca.kdunn4781.assignment1.databinding.ActivityOutputBinding;
import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.location.Location;

/**
 * This activity shows the output of the calculations
 */

public class OutputActivity extends AppCompatActivity {
    Connection connectionReceiver = new Connection();
    IntentFilter myFilter = new IntentFilter(Network.MY_BROADCAST);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.output_activity);

        ActivityOutputBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_output);
        Button shop = binding.btnShop;
        // Network Service Intent
        final Intent serviceIntent  = new Intent(this, Network.class);
        // Network Service Starts on Create stage
        startService(serviceIntent);


        // Button click event handling
        shop.setOnClickListener(new View.OnClickListener() {
            // METHOD      : onClick
            // PARAMETER   : View view
            // RETURN      : void
            // DESCRIPTION : Click handler
            @Override
            public void onClick(View view) {
                // if the boolean value of connection from custom broadcast (ConnectionReceiver)
                // is true which the phone has wireless(3G/4G/5G + WIFI) connection
               if (connectionReceiver.connection) {
                    // Start another activity here when the button is clicked.
                    Intent shop=new Intent(OutputActivity.this, Shop.class);
                    startActivity(shop);
              }
                // if connection is false, toast a message about no internet
                else {
                    Toast.makeText(getApplicationContext(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();

        // A3: Register connectionReceiver
        registerReceiver(connectionReceiver, myFilter);
        Log.d("Receiver", "connectionReceiver is registered");
    }

    @Override
    protected void onPause() {

        // A3: Unregister connectionReceiver
        unregisterReceiver(connectionReceiver);
        Log.d("Receiver", "connectionReceiver is unregistered");

        super.onPause();
    }

}

