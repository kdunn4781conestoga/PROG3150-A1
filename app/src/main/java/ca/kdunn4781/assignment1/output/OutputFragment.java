package ca.kdunn4781.assignment1.output;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.kdunn4781.assignment1.Shop;
import ca.kdunn4781.assignment1.trip.NewTripFragment;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.databinding.FragmentOutputBinding;
import ca.kdunn4781.assignment1.trip.NewTripViewModel;

/**
 * This activity shows the output of the calculations
 */

public class OutputFragment extends Fragment {
    private FragmentOutputBinding binding = null;

    public OutputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_output, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OutputViewModel outputViewModel = new ViewModelProvider(this).get(OutputViewModel.class);

        // start over
        binding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).switchToScreen(NewTripFragment.class, new Bundle());
            }
        });

        // go to Amazon.ca
        binding.btnShop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
                Intent shop = new Intent(requireActivity(), Shop.class);
                startActivity(shop);
                Log.d("Shop", "connect to internet and visit Amazon.ca");
            }
        });

        //display details
        if (getArguments() != null && getArguments().containsKey("tripId")) {
            outputViewModel.getTripById(getArguments().getInt("tripId")).observe(requireActivity(), trip -> {
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
            });
        }
    }
}