package ca.kdunn4781.assignment1.trip;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.WelcomeFragment;
import ca.kdunn4781.assignment1.databinding.FragmentSavedTripsBinding;

public class SavedTripsFragment extends Fragment implements AdapterView.OnItemClickListener {
    private FragmentSavedTripsBinding binding = null;

    private SavedTripsViewModel savedTripsViewModel;

    private SavedTripsListAdapter adapter;

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

        adapter = new SavedTripsListAdapter(
                requireActivity(),
                getLayoutInflater()
        );
        adapter.setOnItemClickListener(this);

        binding.listTrips.setAdapter(adapter);

        savedTripsViewModel = new ViewModelProvider(requireActivity()).get(SavedTripsViewModel.class);
        savedTripsViewModel.loadTrips().observe(requireActivity(), trips -> {
            if (trips != null) {
                adapter.setList(trips);
            }
        });

        binding.btbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).switchToScreen(WelcomeFragment.class, savedInstanceState);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Trip trip = adapter.getItem(i);

        new AlertDialog.Builder(requireActivity())
                .setTitle("Trip " + trip.getId())
                .setMessage("Do you want to load or delete the trip?")
                .setCancelable(true)
                .setPositiveButton(R.string.load, (dialogInterface, i12) -> {
                    dialogInterface.dismiss();

                    Bundle bundle = new Bundle();
                    bundle.putInt("tripId", trip.getId());

                    ((MainActivity) requireActivity()).switchToScreen(NewTripFragment.class, bundle);
                })
                .setNegativeButton(R.string.btnDelete, (dialogInterface, i1) -> {
                    dialogInterface.dismiss();

                    new AlertDialog.Builder(requireActivity())
                            .setTitle("Confirm")
                            .setMessage("Are you sure you want to delete the trip?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

                                    savedTripsViewModel.deleteTrip(trip).observe(requireActivity(), (dTrip) -> {
                                        if (dTrip == null) {
                                            Toast.makeText(requireActivity(), "Successfully deleted trip!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(requireActivity(), "Failed to delete trip!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            })
                            .show();
                })
                .show();
    }
}