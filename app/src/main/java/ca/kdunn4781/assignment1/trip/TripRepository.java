package ca.kdunn4781.assignment1.trip;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import ca.kdunn4781.assignment1.database.AppDatabase;
import ca.kdunn4781.assignment1.database.TripDAO;
import ca.kdunn4781.assignment1.location.Location;

/**
 * This repository contains functions for accessing data pertaining to trips
 */
public class TripRepository {
    private AppDatabase appDatabase;
    private TripDAO tripDAO;
    private final MutableLiveData<Trip> tripLiveData = new MutableLiveData<>();

    public TripRepository(Context context) {
        this.appDatabase = AppDatabase.getAppDatabase(context);
        this.tripDAO = appDatabase.tripDAO();
    }

    public LiveData<List<Trip>> loadTrips() {
        Log.d("TripRepository", "Loading trips...");
        return tripDAO.getAllTrips();
    }

    /**
     * This function creates a new trip
     *
     * @param name the trip name
     * @param description the trip description
     * @param numOfAdults the number of adults for the trip
     * @param numOfChildren the number of children for the trip
     * @param startLocation the start location
     * @param endLocation the end location
     * @return the livedata
     */
    public LiveData<Trip> createTrip(String name, @Nullable String description, int numOfAdults, int numOfChildren, Location startLocation, Location endLocation) {
        AsyncTask.execute(() -> {
            Trip trip = new Trip(name, description, startLocation, endLocation);
            trip.setNumOfAdults(numOfAdults);
            trip.setNumOfChildren(numOfChildren);

            Log.d("TripRepository", "Creating a new trip...");
            int id = (int) tripDAO.insertTrip(trip);
            trip.setId(id);

            Log.d("TripRepository", "Inserting trip points...");
            tripDAO.insertPoints(trip.getTripPoints().toArray(new TripPoint[0]));

            tripLiveData.postValue(trip);
        });

        return tripLiveData;
    }

    /**
     * This function finds a trip by it's id
     *
     * @param id the trip's id
     * @return the livedata
     */
    public LiveData<Trip> getTripById(int id) {
        AsyncTask.execute(() -> {
            Trip trip = tripDAO.findByTripId(id);

            Log.d("TripRepository", "Finding trip...");
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

    /**
     * This function updates the trip
     * @param trip the trip to update
     * @return the livedata
     */
    public LiveData<Trip> updateTrip(Trip trip) {
        AsyncTask.execute(() -> {
            Log.d("TripRepository", "Updating trip...");
            int result = tripDAO.updateTrip(trip);

            if (result > 0) {
                tripLiveData.postValue(trip);
            } else {
                tripLiveData.postValue(null);
            }
        });

        return tripLiveData;
    }

    /**
     * This function adds a new trip point
     *
     * @param index the index to insert it as
     * @param location the location the of the point
     */
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

    /**
     * This function removes a trip point
     *
     * @param index the index to remove the trip point
     */
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

    /**
     * This function deletes the trip
     *
     * @param trip the trip to delete
     * @return the livedata
     */
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
