package ca.kdunn4781.assignment1.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.databinding.ActivityTripWidgetConfigurationBinding;
import ca.kdunn4781.assignment1.trip.NewTripViewModel;
import ca.kdunn4781.assignment1.trip.SavedTripsListAdapter;
import ca.kdunn4781.assignment1.trip.SavedTripsViewModel;
import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripRepository;

public class TripWidgetConfigurationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ActivityTripWidgetConfigurationBinding binding;

    SavedTripsViewModel savedTripsViewModel;

    private SavedTripsListAdapter adapter;

    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTripWidgetConfigurationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(Activity.RESULT_CANCELED, resultValue);

        // creates new adapter for listview
        adapter = new SavedTripsListAdapter(
                this,
                getLayoutInflater()
        );
        adapter.setOnItemClickListener(this);

        binding.savedTrips.listTrips.setAdapter(adapter);

        savedTripsViewModel = new ViewModelProvider(this).get(SavedTripsViewModel.class);
        savedTripsViewModel.loadTrips().observe(this, trips -> {
            if (trips == null || trips.isEmpty()) {
                new AlertDialog.Builder(TripWidgetConfigurationActivity.this)
                        .setMessage("No existing trips")
                        .setNeutralButton(android.R.string.ok, (dialogInterface, i) -> {
                            Intent resultValue1 = new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                            setResult(RESULT_CANCELED, resultValue1);
                            finish();
                        }).show();
            } else {
                adapter.setList(trips);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Trip trip = adapter.getItem(i);

        Log.d(getLocalClassName(), "Selected trip " + trip.getId());

        SharedPreferences prefs = getSharedPreferences(TripWidgetProvider.CONFIG, 0);
        prefs.edit().putInt(TripWidgetProvider.TRIP_KEY + appWidgetId, trip.getId()).apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        TripWidgetProvider.updateWidget(this, new TripRepository(this), appWidgetManager, appWidgetId);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_trip_points);

        Intent resultValue = new Intent()
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
