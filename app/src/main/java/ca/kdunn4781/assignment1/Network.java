//package ca.kdunn4781.assignment1;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
//
//public class Network extends Service {
//
//    // Declare variable to send for a custom broadcast
//    private boolean connection = false;
//    public final static String MY_BROADCAST = "com.example.group9_assignment2";
//
//    private static String Channel_ID = "My Channel";
//
//    @Override
//    public void onCreate() {
//        Log.d("NetworkService", "Service Created");
//
//        // Call isConnected function and store the boolean value
//        connection = isConnected();
//
//        // If connection is false
//        if (!connection) {
//            Intent notificationIntent = new Intent(this, Wifi.class);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//
//            // Setup notification content
//            CharSequence contentTitle = "Network is not available";
//            CharSequence contentText = "Please check you wireless setting";
//
//            // Build notification
//            NotificationCompat.Builder mBuilder
//                    = new NotificationCompat.Builder(this, Channel_ID)
//
//                    .setContentTitle(contentTitle)
//                    .setContentText(contentText)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    // Tab fires activity_wifiSetting.class which leads to Wifi Setting screen
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true);
//
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//            notificationManager.notify(1,mBuilder.build());
//        }
//
//
//        // Create broadcast intent and ready for connection value
//        Intent broadcastIntent = new Intent(MY_BROADCAST);
//        broadcastIntent.putExtra("Connection", connection);
//
//        Log.d("Custom broadcast sends", String.valueOf(connection));
//
//        // Send broadcast
//        this.sendBroadcast(broadcastIntent);
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i("NetworkService", "Service Started with id=" + startId);
//        return START_STICKY;
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d("NetworkService", "Service Destroyed");
//    }
//
//
//
//
//
//    // METHOD      : isConnected
//    // PARAMETER   : None
//    // RETURN      : void
//    // DESCRIPTION : This function checks wireless connection of
//
//    public boolean isConnected() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//
//        if(networkInfo != null) {
//            if (networkInfo.isConnected()) {
//                Log.d("Network", "Wireless is connected");
//                return true;
//            }
//            else {
//                Log.d("Network", "Wireless is NOT connected");
//                return false;
//            }
//        } else {
//            Log.d("Network", "NetworkInfo is NULL");
//            return false;
//        }
//
//    }
//}
