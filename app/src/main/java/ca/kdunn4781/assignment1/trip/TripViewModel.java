package ca.kdunn4781.assignment1.trip;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import ca.kdunn4781.assignment1.database.AppDatabase;
import ca.kdunn4781.assignment1.database.TripDAO;
import ca.kdunn4781.assignment1.location.Location;

public class TripViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private TripDAO tripDAO;
    private MutableLiveData<Trip> tripMutableLiveData;

    public TripViewModel(@NonNull Application application)
    {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(application);
        tripDAO = appDatabase.tripDAO();
        tripMutableLiveData = new MutableLiveData<>(null);
    }

    public LiveData<Trip> getTripLiveData() {
        return tripMutableLiveData;
    }

    public void createTravel(String name, @Nullable String description, int numOfAdults, int numOfChildren, Location startLocation, Location endLocation) {
        AsyncTask.execute(() -> {
            Trip trip = new Trip(name, description, startLocation, endLocation);
            trip.setNumOfAdults(numOfAdults);
            trip.setNumOfChildren(numOfChildren);

            int id = (int) tripDAO.insertTrip(trip);
            trip.setId(id);

            tripDAO.insertPoints(trip.getTripPoints().toArray(new TripPoint[0]));

            tripMutableLiveData.postValue(trip);
        });
    }

    public void getTripById(int id) {
        AsyncTask.execute(() -> {
            Trip trip = tripDAO.findByTripId(id);

            List<TripPoint> points = tripDAO.findPointsByTravelId(id);
            IntStream.range(0, points.size()).forEach(i -> trip.addTravelPoint(i, points.get(i)));

            trip.getTripPoints().sort(Comparator.comparingInt(TripPoint::getIndex));

            for (TripPoint p : trip.getTripPoints()) {
                Location location = appDatabase.locationDAO().findLocationById(p.locationId);
                if (location != null)
                    p.setLocation(location);
            }

            tripMutableLiveData.postValue(trip);
        });
    }

    public void addTripPoint(int index, Location location) {
        AsyncTask.execute(() -> {
            Trip travel = tripMutableLiveData.getValue();
            assert travel != null;
            TripPoint start = new TripPoint(
                    index, location, 50
            );
            start.travelId = travel.getId();

            travel.addTravelPoint(index, start);

            tripDAO.updatePoints(travel.getTripPoints().toArray(new TripPoint[0]));

            start.id = (int)tripDAO.insertPoints(start)[0];
            if (start.id == -1) return;

            tripDAO.updateTrip(travel);

            tripMutableLiveData.postValue(travel);
        });
    }

    public void removeTripPoint(int index) {
        AsyncTask.execute(() -> {
            Trip trip = tripMutableLiveData.getValue();
            assert trip != null;

            TripPoint removedPoint = trip.getPoint(index);

            trip.removeTripPoint(index);

            tripDAO.deletePoint(removedPoint);

            tripDAO.updatePoints(trip.getTripPoints().toArray(new TripPoint[0]));

            tripDAO.updateTrip(trip);

            tripMutableLiveData.postValue(trip);
        });
    }
}
