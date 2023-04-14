package ca.kdunn4781.assignment1.trip;


import android.Manifest;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.WelcomeFragment;
import ca.kdunn4781.assignment1.databinding.DialogTripDetailsBinding;
import ca.kdunn4781.assignment1.databinding.FragmentNewTripBinding;
import ca.kdunn4781.assignment1.databinding.PeopleCountBinding;
import ca.kdunn4781.assignment1.location.Location;
import ca.kdunn4781.assignment1.location.ModifyLocationFragment;

/**
 * Fragment for displaying new or existing trips
 */
public class NewTripFragment extends Fragment {
    private FragmentNewTripBinding binding;

    private NewTripViewModel newTripViewModel;

    private ArrayAdapter<Location> adapter;

    private Trip trip = null;
    private String name = null;
    private String description = null;

    public NewTripFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_trip, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AlertDialog loadDialog = new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.loading_locations))
                .setCancelable(false)
                .show();


        // create viewmodel for this fragment
        newTripViewModel = new ViewModelProvider(this).get(NewTripViewModel.class);
        newTripViewModel.loadLocations().observe(getViewLifecycleOwner(), locations -> {
            // update adapter with locations
            if (locations != null && !locations.isEmpty()) {
                adapter.clear();
                adapter.addAll(locations);

                if (trip != null) {
                    for (int i = 0; i < locations.size(); i++)
                    {
                        if (locations.get(i).id == trip.getTripPoints().get(0).locationId) {
                            binding.fromLocationSpinner.setSelection(i);
                        } else if (locations.get(i).id == trip.getTripPoints().get(trip.getTripPoints().size() - 1).locationId) {
                            binding.toLocationSpinner.setSelection(i);
                        }
                    }
                } else {
                    binding.fromLocationSpinner.setSelection(0);
                    binding.toLocationSpinner.setSelection(locations.size() - 1);
                }
            }

            loadDialog.dismiss();
        });

        // better implementation of adding click events for the counters
        assignCounters(binding.adultCount);
        assignCounters(binding.childrenCount);

        // date picker
        binding.startDatePicker.btnPickDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setDate(binding.startDatePicker.lblDate);
            }
        });

        // adapter for the spinners
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fromLocationSpinner.setAdapter(adapter);
        binding.toLocationSpinner.setAdapter(adapter);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveData<Trip> liveData;
                if (trip != null) {
                    // existing trip existing so loading views
                    trip.setName(name);
                    trip.setDescription(description);
                    trip.setNumOfAdults(Integer.parseInt(binding.adultCount.howManyTv.getText().toString()));
                    trip.setNumOfChildren(Integer.parseInt(binding.childrenCount.howManyTv.getText().toString()));
                    trip.setTravelPoints(
                            adapter.getItem(binding.fromLocationSpinner.getSelectedItemPosition()),
                            adapter.getItem(binding.toLocationSpinner.getSelectedItemPosition())
                    );

                    liveData = newTripViewModel.updateTrip(trip);
                } else {
                    // create new trip
                    liveData = newTripViewModel.createTrip(
                            name,
                            description,
                            Integer.parseInt(binding.adultCount.howManyTv.getText().toString()),
                            Integer.parseInt(binding.childrenCount.howManyTv.getText().toString()),
                            adapter.getItem(binding.fromLocationSpinner.getSelectedItemPosition()),
                            adapter.getItem(binding.toLocationSpinner.getSelectedItemPosition())
                    );
                }

                liveData.observe(getViewLifecycleOwner(), new Observer<Trip>() {
                    @Override
                    public void onChanged(Trip trip) {
                        // switch to fragment after loading/creating trip
                        if (trip != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("tripId", trip.getId());

                            ((MainActivity) requireActivity()).switchToScreen(ModifyLocationFragment.class, "Modify Locations", bundle);

                            liveData.removeObserver(this);
                        }
                    }
                });
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).switchToScreen(WelcomeFragment.class, "Trip Planner", new Bundle());
            }
        });

        // checks if there are arguments and the tripId exists
        if (getArguments() != null && getArguments().containsKey("tripId")) {
            newTripViewModel.getTripById(getArguments().getInt("tripId")).observe(getViewLifecycleOwner(), trip -> {
                if (trip != null) {
                    // loads trip in views
                    NewTripFragment.this.trip = trip;
                    NewTripFragment.this.name = trip.getName();
                    NewTripFragment.this.description = trip.getDescription();

                    binding.adultCount.howManyTv.setText(String.valueOf(trip.getNumOfAdults()));
                    binding.childrenCount.howManyTv.setText(String.valueOf(trip.getNumOfChildren()));
                } else {
                    trip = new Trip();
                }

                requireActivity().setTitle(trip.getName());
            });
        } else {
            editDetails(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    ((MainActivity) requireActivity()).switchToScreen(WelcomeFragment.class, "Trip Planner", null);
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_trip, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_edit_details) {
            editDetails(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editDetails(DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder detailDialog = new AlertDialog.Builder(requireActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_trip_details, null);
        DialogTripDetailsBinding binding = (DialogTripDetailsBinding) DataBindingUtil.bind(dialogView);

        if (binding != null) {
            binding.textName.setText(name);
            binding.textDescription.setText(description);
        }

        detailDialog
                .setTitle(R.string.trip_details)
                .setView(dialogView)
                .setPositiveButton(R.string.btnSave, (dialogInterface, i) -> {

                    if (binding != null && !binding.textName.getText().toString().isEmpty()) {
                        name = binding.textName.getText().toString();

                        if (binding.textDescription.getText().toString().isEmpty())
                            description = null;
                        else
                            description = binding.textDescription.getText().toString();

                        requireActivity().setTitle(name + "\n" + description);

                        return;
                    }

                    negativeListener.onClick(dialogInterface, i);
                })
                .setNegativeButton(android.R.string.cancel, negativeListener)
                .show();
    }

    /**
     * This function sets the date
     * @param x the textview
     */
    private void setDate(TextView x) {
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int date = calender.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {
                String str = year + "/" + month + "/" + date;
                x.setText(str);
            }
        }, year, month, date);

        datePickerDialog.show();
    }

    /**
     * This function setups the people counts
     * @param peopleCountBinding the binding for people counts
     */
    public void assignCounters(PeopleCountBinding peopleCountBinding) {
        peopleCountBinding.plusBtn.setOnClickListener(v -> {
            String s = peopleCountBinding.howManyTv.getText().toString();
            int  num = Integer.valueOf(s);
            num = num + 1;
            String s1= String.valueOf(num);
            peopleCountBinding.howManyTv.setText(s1);

        });

        peopleCountBinding.minusBtn.setOnClickListener(v -> {

            String s = peopleCountBinding.howManyTv.getText().toString();
            int  num = Integer.valueOf(s);
            if(num > 0) {
                num = num - 1;
            }
            String s1= String.valueOf(num);
            peopleCountBinding.howManyTv.setText(s1);

        });
    }
}