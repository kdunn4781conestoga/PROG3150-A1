package ca.kdunn4781.assignment1.ServicesAndReceivers;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootReceiver", "ACTION_BOOT_COMPLETED");
        Intent serviceIntent = new Intent(context, Booted.class);
        context.startService(serviceIntent);
    }
}