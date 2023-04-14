package ca.kdunn4781.assignment1.location;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.FragmentModifyLocationsBinding;
import ca.kdunn4781.assignment1.output.OutputFragment;
import ca.kdunn4781.assignment1.trip.NewTripFragment;
import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripPointListAdapter;

/**
 * This activity shows a list of locations that the user can add or remove
 */
public class ModifyLocationFragment extends Fragment implements TripPointListAdapter.OnTripPointClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private FragmentModifyLocationsBinding binding = null;

    ModifyLocationViewModel modifyLocationViewModel;

    TripPointListAdapter locationListAdapter;

    List<String> locationStrs;

    ActivityResultLauncher<String[]> locationPermissionRequest;

    int tripPosition;
    private GoogleMap googleMap;

    public ModifyLocationFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                googleMap.setMyLocationEnabled(true);
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                googleMap.setMyLocationEnabled(true);
                            } else {
                                // No location access granted.
                                googleMap.setMyLocationEnabled(false);
                            }
                        }
                );
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

        modifyLocationViewModel = new ViewModelProvider(this).get(ModifyLocationViewModel.class);

        // binds adapter to list
        locationListAdapter = new TripPointListAdapter(
                requireActivity(),
                getLayoutInflater(),
                this
        );
        binding.listLocations.setAdapter(locationListAdapter);

        binding.nextBtn.setOnClickListener(v -> ((MainActivity) requireActivity()).switchToScreen(OutputFragment.class, "Trip Results", getArguments()));

        if (getArguments() != null && getArguments().containsKey("tripId")) {
            modifyLocationViewModel.getTripById(getArguments().getInt("tripId")).observe(getViewLifecycleOwner(), new Observer<Trip>() {
                @Override
                public void onChanged(Trip trip) {
                    if (trip != null && trip.getTripPoints() != null) {
                        locationListAdapter.setList(trip.getTripPoints());
                    }
                }
            });
        } else {
            // dialog to notify the user that it failed
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Error")
                    .setMessage("Failed to load locations. Returning to previous screen.")
                    .setOnDismissListener((dialog) -> {
                        dialog.dismiss();

                        ((MainActivity) requireActivity()).switchToScreen(NewTripFragment.class, "Modify Trip", new Bundle());
                    })
                    .show();
        }
    }

    @Override
    public void onAddPointClickListener(View view1, int position) {
        // shows a dialog for selecting a list of locations
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(ModifyLocationFragment.this);
        tripPosition = position;

        getParentFragmentManager()
                .beginTransaction()
                .add(R.id.content_frame, mapFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRemovePointClickListener(View view1, int position) {
        modifyLocationViewModel.removeTripPoint(position);
    }

    @Override
    public void onDetailPointClickListener(View view1, int position) {
        // TODO for showing details about location
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(43.4822111, -80.5880046), 8f);
        googleMap.moveCamera(update);

        googleMap.setOnMarkerClickListener(this);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager()
                        .popBackStackImmediate();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

        // shows a dialog for loading locations
        AlertDialog loadingLocationsDialog =
                new AlertDialog.Builder(requireActivity())
                        .setMessage("Loading locations...")
                        .create();

        loadingLocationsDialog.show();

        modifyLocationViewModel.loadLocations().observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                loadingLocationsDialog.dismiss();

                for (int i = 0; i < locations.size(); i++) {
                    Location location = locations.get(i);

                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(location.getCoord())
                            .title(location.getName()));
                    marker.setTag(location);
                }

//                AlertDialog.Builder addLocationAlert = new AlertDialog.Builder(requireActivity());
//                addLocationAlert.setTitle("Add Location");
//                addLocationAlert.setItems(locations.stream().map(Location::getName).toArray(CharSequence[]::new), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//
//                        modifyLocationViewModel.addTripPoint(position + 1, locations.get(which));
//                    }
//                });
//                addLocationAlert.show();

                modifyLocationViewModel.getLocations().removeObserver(this);
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Location location = (Location) marker.getTag();

        if (location == null) {
            return false;
        }

        // shows a dialog for accepting a location
        AlertDialog loadingLocationsDialog =
                new AlertDialog.Builder(requireActivity())
                        .setMessage(Html.fromHtml(String.format("Do you want <b>%s</b> to the trip?", location.getName()), 0))
                        .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                            modifyLocationViewModel.addTripPoint(tripPosition + 1, location);

                            getParentFragmentManager().popBackStackImmediate();
                        })
                        .setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss())
                        .create();

        loadingLocationsDialog.show();

        return true;
    }
}