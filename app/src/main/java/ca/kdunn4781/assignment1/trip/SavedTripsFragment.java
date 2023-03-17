package ca.kdunn4781.assignment1.trip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.WelcomeFragment;
import ca.kdunn4781.assignment1.databinding.FragmentSavedTripsBinding;

public class SavedTripsFragment extends Fragment {
    private FragmentSavedTripsBinding binding = null;

    public SavedTripsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_trips, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SavedTripsViewModel savedTripsViewModel = new ViewModelProvider(requireActivity()).get(SavedTripsViewModel.class);
        savedTripsViewModel.loadTrips().observe(requireActivity(), trips -> {
            if (trips != null) {

            }
        });

        binding.btbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).switchToScreen(WelcomeFragment.class, savedInstanceState);
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to delete from DB
            }
        });
    }
}