package ca.kdunn4781.assignment1.trip;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.WelcomeFragment;
import ca.kdunn4781.assignment1.databinding.FragmentNewTripBinding;
import ca.kdunn4781.assignment1.databinding.PeopleCountBinding;
import ca.kdunn4781.assignment1.location.Location;
import ca.kdunn4781.assignment1.location.ModifyLocationFragment;

public class NewTripFragment extends Fragment {
    private FragmentNewTripBinding binding;

    private NewTripViewModel newTripViewModel;

    private ArrayAdapter<Location> adapter;

    public NewTripFragment() {
        // Required empty public constructor
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

        newTripViewModel = new ViewModelProvider(this).get(NewTripViewModel.class);
        newTripViewModel.loadLocations().observe(requireActivity(), locations -> {
            if (locations != null && !locations.isEmpty()) { // database items
                adapter.clear();
                adapter.addAll(locations);
            }
        });

        // better implementation of adding click events for the counters
        assignCounters(binding.adultCount);
        assignCounters(binding.childrenCount);

        binding.startDatePicker.btnPickDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setDate(binding.startDatePicker.lblDate);
            }
        });

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fromLocationSpinner.setAdapter(adapter);
        binding.toLocationSpinner.setAdapter(adapter);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTripViewModel.createTrip(
                        "My Travel",
                        null,
                        Integer.parseInt(binding.adultCount.howManyTv.getText().toString()),
                        Integer.parseInt(binding.childrenCount.howManyTv.getText().toString()),
                        adapter.getItem(binding.fromLocationSpinner.getSelectedItemPosition()),
                        adapter.getItem(binding.toLocationSpinner.getSelectedItemPosition())
                ).observe(requireActivity(), travel -> {
                    if (travel != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("tripId", travel.getId());

                        ((MainActivity) requireActivity()).switchToScreen(ModifyLocationFragment.class, bundle);
                    }
                });
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).switchToScreen(WelcomeFragment.class, new Bundle());
            }
        });


    }

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