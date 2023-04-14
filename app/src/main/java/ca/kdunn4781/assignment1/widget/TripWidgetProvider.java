package ca.kdunn4781.assignment1.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import ca.kdunn4781.assignment1.MainActivity;
import ca.kdunn4781.assignment1.R;
import ca.kdunn4781.assignment1.trip.Trip;
import ca.kdunn4781.assignment1.trip.TripRepository;

public class TripWidgetProvider extends AppWidgetProvider {

    public static String CONFIG = "ca.kdunn4781.assignment1.widgets";
    public static String TRIP_KEY = "Widget_TripId:";
    private TripRepository tripRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TripWidgetProvider", "Received Widget with action " + intent.getAction());

        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Bundle extras = intent.getExtras();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            if (extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_IDS)) {
                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);

                for (int appWidgetId : appWidgetIds) {
                    updateWidget(context, tripRepository, appWidgetManager, appWidgetId);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_trip_points);
                }
            } else {
                int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

                if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    updateWidget(context, tripRepository, appWidgetManager, appWidgetId);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_trip_points);
                }
            }
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("TripWidgetProvider", "Updating Widgets");

        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, tripRepository, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static int getWidgetTripId(Context context, int appWidgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG, 0);
        String key = TRIP_KEY + appWidgetId;
        return sharedPreferences.getInt(key, -1);
    }

    public static void updateWidget(Context context, TripRepository tripRepository, AppWidgetManager appWidgetManager, int appWidgetId) {
        int tripId = getWidgetTripId(context, appWidgetId);

        Log.d("TripWidgetProvider", "Updating widget " + appWidgetId + " with trip " + tripId);

        if (tripId != -1) {
            if (tripRepository == null)
            {
                tripRepository = new TripRepository(context);
            }

            LiveData<Trip> tripLiveData = tripRepository.loadTripById(tripId);
            tripLiveData.observeForever(new Observer<Trip>() {
                @Override
                public void onChanged(Trip trip) {
                    if (trip != null) {
                        Intent mainIntent = new Intent(context, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mainIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, appWidgetId + 100, mainIntent, PendingIntent.FLAG_ONE_SHOT);

                        Intent serviceIntent = new Intent(context, TripWidgetService.class);
                        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

                        Intent refreshIntent = new Intent(context, TripWidgetProvider.class);
                        refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                        PendingIntent pendingSync = PendingIntent.getBroadcast(context, appWidgetId + 102, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_trip_planner);
                        views.setTextViewText(R.id.txt_name, trip.getName());
                        views.setTextViewText(R.id.txt_description, trip.getDescription());
                        views.setOnClickPendingIntent(R.id.btn_edit, mainPendingIntent);
                        views.setOnClickPendingIntent(R.id.btn_refresh, pendingSync);
                        views.setRemoteAdapter(R.id.list_trip_points, serviceIntent);
                        views.setEmptyView(R.id.list_trip_points, android.R.id.empty);

                        appWidgetManager.updateAppWidget(appWidgetId, views);

                        tripLiveData.removeObserver(this);
                    } else {
                        displayEmpty(context, appWidgetManager, appWidgetId, tripId);
                    }
                }
            });
        } else {
            displayEmpty(context, appWidgetManager, appWidgetId, tripId);
        }
    }

    private static void displayEmpty(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int tripId) {
        Intent intent = new Intent(context, TripWidgetConfigurationActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId + 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_trip_planner_empty);
        views.setTextViewText(R.id.txt_error, context.getString(R.string.no_trip, tripId));
        views.setOnClickPendingIntent(R.id.btn_configure, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG, 0);
            sharedPreferences.edit().remove(TRIP_KEY + appWidgetId).apply();
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Log.d("TripWidgetProvider", "Widget Enabling...");

        tripRepository = new TripRepository(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        Log.d("TripWidgetProvider", "Widget Restores");

        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.d("TripWidgetProvider", "Widget Changed");

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}
