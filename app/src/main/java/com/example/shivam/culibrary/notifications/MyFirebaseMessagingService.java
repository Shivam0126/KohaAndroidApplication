package com.example.shivam.culibrary.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.shivam.culibrary.MainActivity;
import com.example.shivam.culibrary.R;
import com.example.shivam.culibrary.books;
import com.example.shivam.culibrary.home;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String ADMIN_CHANNEL_ID ="admin_channel";
    private NotificationManager notificationManager;
    Intent notificationIntent;
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

      /*  Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job  true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        // [END receive_message]

        if(MainActivity.isAppRunning){
            notificationIntent = new Intent(this, books.class);
        }else{
            notificationIntent = new Intent(this, home.class);
        }

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        //You should use an actual ID instead
        int notificationId = new Random().nextInt(60000);


        //    Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image-url"));


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                        .setSmallIcon(R.drawable.culogo)
                        .setContentTitle(remoteMessage.getData().get("title"))

                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId, notificationBuilder.build());

    }





    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    /**
     * Schedule a job using FirebaseJobDispatcher.

     private void scheduleJob() {
     // [START dispatch_job]
     FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
     Job myJob = dispatcher.newJobBuilder()
     .setService(MyJobService.class)
     .setTag("my-job-tag")
     .build();
     dispatcher.schedule(myJob);
     // [END dispatch_job]
     }

     /**
     * Handle time allotted to BroadcastReceivers.

     private void handleNow() {
     Log.d(TAG, "Short lived task is done.");
     }*/

}
