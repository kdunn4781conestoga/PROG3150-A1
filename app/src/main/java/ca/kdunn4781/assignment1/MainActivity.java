package ca.kdunn4781.assignment1;import androidx.appcompat.app.AppCompatActivity;import androidx.fragment.app.Fragment;import androidx.fragment.app.FragmentManager;import androidx.lifecycle.ViewModelProvider;import android.os.Bundle;import android.util.Log;import ca.kdunn4781.assignment1.location.Location;import ca.kdunn4781.assignment1.location.LocationRepository;import ca.kdunn4781.assignment1.location.ModifyLocationViewModel;import ca.kdunn4781.assignment1.trip.SavedTripsViewModel;/** * This class displays the main activity with switching fragments */public class MainActivity extends AppCompatActivity {    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        setSupportActionBar(findViewById(R.id.appBar));        //starting with the welcome screen        switchToScreen(WelcomeFragment.class, savedInstanceState);        Log.d("Start", "MAIN activity loaded successfully, switch to welcome screen");    }    public void switchToScreen(Class<? extends Fragment> screen, Bundle bundle) {        FragmentManager manager = getSupportFragmentManager();        manager.beginTransaction().replace(R.id.content_frame, screen, bundle).commit();        setTitle(screen.getSimpleName()); // TODO change later    }}