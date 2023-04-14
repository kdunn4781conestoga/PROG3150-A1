package ca.kdunn4781.assignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.kdunn4781.assignment1.ServicesAndReceivers.Network;
import ca.kdunn4781.assignment1.databinding.FragmentWelcomeBinding;
import ca.kdunn4781.assignment1.trip.NewTripFragment;
import ca.kdunn4781.assignment1.trip.SavedTripsFragment;
import android.content.Intent;


@RequiresApi(api = Build.VERSION_CODES.N)
public class WelcomeFragment extends Fragment {
    FragmentWelcomeBinding binding = null;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public void startService(View view) {
        Intent intent = new Intent(getActivity().getBaseContext(), Network.class);
        getActivity().startService(intent);

    }

    // Method to stop the service
    public void stopService(View view) {
        Intent intent = new Intent(getActivity().getBaseContext(), Network.class);
        getActivity().stopService(intent);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create new trip
        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if Permission is not granted, ask user for permission
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                        && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                        && getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//                    // Ask user permission for Fine and Coarse Location
//                    getActivity().locationPermissionRequest.launch(new String[] {
//                            Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.ACCESS_COARSE_LOCATION
//                    });
       //         }
                ((MainActivity) requireActivity()).switchToScreen(NewTripFragment.class, savedInstanceState);
                Log.d("Create", "switch to create saved trips");
            }
        });
        //load saved trips
        binding.btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).switchToScreen(SavedTripsFragment.class, savedInstanceState);
                Log.d("Load", "switch to loading saved trips");
            }
        });
    }
}