package ca.kdunn4781.assignment1.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.location.Location;
import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripPoint;
import ca.kdunn4781.assignment1.trip.TripRepository;

public class TripWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TripRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class TripRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appWidgetId, tripId;

    private TripRepository tripRepository;

    private LiveData<List<TripPoint>> allTripPoints;
    private List<TripPoint> tripPoints = new ArrayList<>();

    public TripRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        this.tripRepository = new TripRepository(context);

        onDataSetChanged();
    }

    @Override
    public void onCreate() {
        Log.d("TripWidgetService", "Created with trip ");

        SharedPreferences sharedPreferences = context.getSharedPreferences(TripWidgetProvider.CONFIG, 0);
        tripId = sharedPreferences.getInt(TripWidgetProvider.TRIP_KEY + appWidgetId, -1);

        allTripPoints = tripRepository.getTripPoints(tripId);
        allTripPoints.observeForever((list) -> {
            AsyncTask.execute(() -> {
                if (list != null) {
                    for (TripPoint p : list) {
                        Location location = tripRepository.getLocationById(p.locationId);
                        p.setLocation(location);
                    }
                }

                new Handler(Looper.getMainLooper()).post(() -> {
                    updateList(list);
                });
            });
        });
    }

    @Override
    public void onDataSetChanged() {
        allTripPoints = tripRepository.getTripPoints(tripId);
        if (!tripPoints.isEmpty()) {
            getViewAt(0);
        }
    }

    @Override
    public void onDestroy() {
        Log.d("TripWidgetService", "Destroyed");
    }

    @Override
    public int getCount() {
        return tripPoints.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        TripPoint tripPoint = tripPoints.get(i);

        Log.d("TripWidgetService", String.format(Locale.getDefault(), "Got view at pos %d, with %s at %s", i + 1, tripPoint.getLocation().getName(), tripPoint.locationId));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_trip_point_item);

        views.setTextViewText(R.id.txt_location, String.format(Locale.getDefault(),"%02d - %s", i + 1, tripPoint.getLocation().getName()));

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return tripPoints.get(i).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void updateList(List<TripPoint> points) {
        tripPoints.clear();
        tripPoints.addAll(points);

        onDataSetChanged();
    }
}
