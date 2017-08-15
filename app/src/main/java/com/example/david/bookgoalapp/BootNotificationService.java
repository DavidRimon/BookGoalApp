package com.example.david.bookgoalapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by David on 15-Aug-17.
 */

public class BootNotificationService extends Service {

    private NotificationManager  mNM;
    private final int NOtIFICATION = 1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(){

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this,MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,
                resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //fix this service? if will show the today to learn will require to launch the app
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_calendar_book)
                .setContentTitle(getResources().getString(R.string.learn_today))
                .setContentText(getString(R.string.did_you_learn_today))
                .setColor(getResources().getColor(R.color.caldroid_holo_blue_light))
                .setContentIntent(resultPendingIntent)
                .build();
        mNM.notify(NOtIFICATION,notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onDestroy() {

        // decide if this is needed. without it, there is a stronger reminder
        //it is needed cus otherwise the service notify will replace the app notify
        //after the app was launched
        mNM.cancel(NOtIFICATION);
       // Toast.makeText(this.getApplicationContext(),"destroy thingy",Toast.LENGTH_LONG).show();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
