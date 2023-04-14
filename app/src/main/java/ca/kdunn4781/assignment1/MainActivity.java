package ca.kdunn4781.assignment1;import androidx.appcompat.app.AppCompatActivity;import androidx.fragment.app.Fragment;import androidx.fragment.app.FragmentManager;import androidx.lifecycle.ViewModelProvider;import android.appwidget.AppWidgetManager;import android.content.ComponentName;import android.content.Intent;import android.content.SharedPreferences;import android.os.Build;import android.os.Bundle;import android.util.Log;import android.widget.EditText;import ca.kdunn4781.assignment1.location.Location;import ca.kdunn4781.assignment1.location.LocationRepository;import ca.kdunn4781.assignment1.location.ModifyLocationViewModel;import ca.kdunn4781.assignment1.trip.NewTripFragment;import ca.kdunn4781.assignment1.trip.SavedTripsViewModel;import ca.kdunn4781.assignment1.trip.TripRepository;import ca.kdunn4781.assignment1.widget.TripWidgetProvider;/** * This class displays the main activity with switching fragments */public class MainActivity extends AppCompatActivity {    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        setSupportActionBar(findViewById(R.id.appBar));        //starting with the welcome screen        Log.d("Start", "MAIN activity loaded successfully, switch to welcome screen");        switchToScreen(WelcomeFragment.class, "Trip Planner", null);    }    public void switchToScreen(Class<? extends Fragment> screen, String title, Bundle bundle) {        FragmentManager manager = getSupportFragmentManager();        manager.beginTransaction().replace(R.id.content_frame, screen, bundle).commit();        setTitle(title);    }    public void updateWidgets() {        Intent intent = new Intent(this, TripWidgetProvider.class);        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(this, TripWidgetProvider.class));        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);        sendBroadcast(intent);    }    @Override    protected void onNewIntent(Intent intent) {        super.onNewIntent(intent);        if (intent != null) {            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);            int tripId = TripWidgetProvider.getWidgetTripId(this, widgetId);            if (tripId != -1) {                Log.d("Start", "MAIN activity loaded successfully, switch to edit trip screen");                Bundle bundle = new Bundle();                bundle.putInt("tripId", tripId);                switchToScreen(NewTripFragment.class, "Edit Trip", bundle);            }        }    }    @Override    protected void onResume() {        super.onResume();    }    @Override    protected void onPause() {        super.onPause();        updateWidgets();    }}