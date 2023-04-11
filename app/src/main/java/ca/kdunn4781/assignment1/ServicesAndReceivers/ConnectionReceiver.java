package ca.kdunn4781.assignment1.ServicesAndReceivers;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ConnectionReceiver extends BroadcastReceiver {
    public boolean connection = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Receive custom broadcast sender code
        connection = intent.getExtras().getBoolean("Connection");
        Log.d("Custom Broadcast Rev", String.valueOf(connection));
        context.stopService(new Intent(context, NetworkService.class));
    }
}
