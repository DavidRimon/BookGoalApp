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

        //TODO:: fix this service!!!!
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name3)
                .setContentTitle(getResources().getString(R.string.learn_today))
                .setContentText(getString(R.string.did_you_learn_today))
                .setContentIntent(resultPendingIntent)
                .build();
        mNM.notify(NOtIFICATION,notification);

        Toast.makeText(this.getApplicationContext(),"startupt thingy",Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onDestroy() {

        // TODO:: decide if this is needed. without it, there is a stronger reminder
        // mNM.cancel(NOtIFICATION);
        Toast.makeText(this.getApplicationContext(),"destroy thingy",Toast.LENGTH_LONG).show();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
