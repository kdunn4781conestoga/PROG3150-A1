package ca.kdunn4781.assignment1.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.FragmentModifyLocationsBinding;
import ca.kdunn4781.assignment1.output.OutputFragment;
import ca.kdunn4781.assignment1.travel.OnTripPointClickListener;
import ca.kdunn4781.assignment1.travel.Trip;
import ca.kdunn4781.assignment1.travel.TripPointListAdapter;
import ca.kdunn4781.assignment1.travel.TripViewModel;

/**
 * This activity shows a list of locations that the user can add or remove
 */
public class ModifyLocationFragment extends Fragment {
    private FragmentModifyLocationsBinding binding = null;

    TripViewModel tripViewModel;
    LocationViewModel locationViewModel;

    TripPointListAdapter locationListAdapter;

    public ModifyLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_locations, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        locationViewModel.getLocationListLiveData().observe(requireActivity(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                if (locations != null && !locations.isEmpty()) {
                    // binds adapter to list
                    locationListAdapter = new TripPointListAdapter(
                            requireContext(),
                            getLayoutInflater(),
                            new OnTripPointClickListener() {
                                @Override
                                public void onAddPointClickListener(View view, int position) {
                                    // shows a dialog for selecting a list of locations
                                    AlertDialog.Builder b = new AlertDialog.Builder(requireContext());
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

        tripViewModel.getTripLiveData().observe(requireActivity(), new Observer<Trip>() {
            @Override
            public void onChanged(Trip travel) {
                if (travel != null && travel.getTripPoints() != null)
                    locationListAdapter.setList(travel.getTripPoints());
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).switchToScreen(OutputFragment.class, getArguments());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null && getArguments().containsKey("tripId"))
        {
            tripViewModel.getTripById(getArguments().getInt("tripId"));
        }
    }
}