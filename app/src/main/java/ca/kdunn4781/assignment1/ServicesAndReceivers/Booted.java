package ca.kdunn4781.assignment1.ServicesAndReceivers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;



import androidx.annotation.Nullable;
import android.widget.Toast;


public class Booted extends Service {
    @Override
    public void onCreate() {
        Log.d("AfterBootService", "Phone booted successfully. Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        Log.d("AfterBootService", "Service Started with id=" + startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();

        Log.d("Booted", "Service Destroyed");
    }
}
