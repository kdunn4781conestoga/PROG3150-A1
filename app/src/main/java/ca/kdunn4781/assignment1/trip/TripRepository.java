package ca.kdunn4781.assignment1.trip;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import ca.kdunn4781.assignment1.database.AppDatabase;
import ca.kdunn4781.assignment1.database.TripDAO;
import ca.kdunn4781.assignment1.location.Location;

public class TripRepository {
    private AppDatabase appDatabase;
    private TripDAO tripDAO;
    private final MutableLiveData<Trip> tripLiveData = new MutableLiveData<>();

    public TripRepository(Context context) {
        this.appDatabase = AppDatabase.getAppDatabase(context);
        this.tripDAO = appDatabase.tripDAO();
    }

    public LiveData<List<Trip>> loadTrips() {
        return tripDAO.getAllTrips();
    }

    public LiveData<Trip> createTrip(String name, @Nullable String description, int numOfAdults, int numOfChildren, Location startLocation, Location endLocation) {
        AsyncTask.execute(() -> {
            Trip trip = new Trip(name, description, startLocation, endLocation);
            trip.setNumOfAdults(numOfAdults);
            trip.setNumOfChildren(numOfChildren);

            int id = (int) tripDAO.insertTrip(trip);
            trip.setId(id);

            tripDAO.insertPoints(trip.getTripPoints().toArray(new TripPoint[0]));

            tripLiveData.postValue(trip);
        });

        return tripLiveData;
    }

    public LiveData<Trip> getTripById(int id) {
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

            tripLiveData.postValue(trip);
        });

        return tripLiveData;
    }

    public LiveData<Trip> updateTrip(Trip trip) {
        AsyncTask.execute(() -> {
            int result = tripDAO.updateTrip(trip);

            if (result > 0) {
                tripLiveData.postValue(trip);
            } else {
                tripLiveData.postValue(null);
            }
        });

        return tripLiveData;
    }

    public void addTripPoint(int index, Location location) {
        AsyncTask.execute(() -> {
            Trip travel = tripLiveData.getValue();
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

            tripLiveData.postValue(travel);
        });
    }

    public void removeTripPoint(int index) {
        AsyncTask.execute(() -> {
            Trip trip = tripLiveData.getValue();
            assert trip != null;

            TripPoint removedPoint = trip.getPoint(index);

            trip.removeTripPoint(index);

            tripDAO.deletePoint(removedPoint);

            tripDAO.updatePoints(trip.getTripPoints().toArray(new TripPoint[0]));

            tripDAO.updateTrip(trip);

            tripLiveData.postValue(trip);
        });
    }

    public LiveData<Trip> deleteTrip(Trip trip) {
        AsyncTask.execute(() -> {
            int d = tripDAO.deleteTrip(trip);

            if (d > 0) {
                tripLiveData.postValue(null);
            } else {
                tripLiveData.postValue(trip);
            }
        });

        return tripLiveData;
    }
}
