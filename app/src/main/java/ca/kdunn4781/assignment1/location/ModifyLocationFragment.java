package ca.kdunn4781.assignment1.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.NewTripFragment;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.FragmentModifyLocationsBinding;
import ca.kdunn4781.assignment1.output.OutputFragment;
import ca.kdunn4781.assignment1.trip.OnTripPointClickListener;
import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripPointListAdapter;
import ca.kdunn4781.assignment1.trip.TripViewModel;

/**
 * This activity shows a list of locations that the user can add or remove
 */
public class ModifyLocationFragment extends Fragment implements OnTripPointClickListener {
    private FragmentModifyLocationsBinding binding = null;

    TripViewModel tripViewModel;
    LocationViewModel locationViewModel;

    TripPointListAdapter locationListAdapter;

    List<String> locationStrs;

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

        // binds adapter to list
        locationListAdapter = new TripPointListAdapter(
                requireActivity(),
                getLayoutInflater(),
                this
        );
        binding.listLocations.setAdapter(locationListAdapter);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).switchToScreen(OutputFragment.class, getArguments());
            }
        });

        if (getArguments() != null && getArguments().containsKey("tripId")) {
            tripViewModel.getTripById(getArguments().getInt("tripId")).observe(requireActivity(), new Observer<Trip>() {
                @Override
                public void onChanged(Trip trip) {
                    if (trip != null && trip.getTripPoints() != null) {
                        locationListAdapter.setList(trip.getTripPoints());
                    }
                }
            });
        } else {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Error")
                    .setMessage("Failed to load locations. Returning to previous screen.")
                    .setOnDismissListener((dialog) -> {
                        dialog.dismiss();

                        ((MainActivity) requireActivity()).switchToScreen(NewTripFragment.class, new Bundle());
                    })
                    .show();
        }
    }
    @Override
    public void onAddPointClickListener(View view1, int position) {
        // shows a dialog for loading locations
        AlertDialog loadingLocationsDialog =
                new AlertDialog.Builder(requireActivity())
                        .setMessage("Loading locations...")
                        .create();

        loadingLocationsDialog.show();

        locationViewModel.loadLocations().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                loadingLocationsDialog.dismiss();

                // shows a dialog for selecting a list of locations
                AlertDialog.Builder addLocationAlert = new AlertDialog.Builder(requireActivity());
                addLocationAlert.setTitle("Add Location");
                addLocationAlert.setItems(locations.stream().map(Location::getName).toArray(CharSequence[]::new), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        tripViewModel.addTripPoint(position + 1, locations.get(which));
                    }
                });
                addLocationAlert.show();

                locationViewModel.getLocations().removeObserver(this);
            }
        });
    }

    @Override
    public void onRemovePointClickListener(View view1, int position) {
        tripViewModel.removeTripPoint(position);
    }

    @Override
    public void onDetailPointClickListener(View view1, int position) {
        // TODO for showing details about location
    }
}