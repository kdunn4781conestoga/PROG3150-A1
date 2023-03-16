package ca.kdunn4781.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Connection extends BroadcastReceiver {
    public boolean connection = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Receive custom broadcast sender code
        connection = intent.getExtras().getBoolean("Connection");
        Log.d("Custom Broadcast Rev", String.valueOf(connection));
        context.stopService(new Intent(context, Network.class));
    }
}