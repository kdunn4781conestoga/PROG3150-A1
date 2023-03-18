package ca.kdunn4781.assignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.kdunn4781.assignment1.databinding.FragmentWelcomeBinding;
import ca.kdunn4781.assignment1.trip.NewTripFragment;
import ca.kdunn4781.assignment1.trip.SavedTripsFragment;

public class WelcomeFragment extends Fragment {
    FragmentWelcomeBinding binding = null;

    public WelcomeFragment() {
        // Required empty public constructor
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
                ((MainActivity) requireActivity()).switchToScreen(NewTripFragment.class, savedInstanceState);
            }
        });
        //load saved trips
        binding.btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).switchToScreen(SavedTripsFragment.class, savedInstanceState);
            }
        });
    }
}