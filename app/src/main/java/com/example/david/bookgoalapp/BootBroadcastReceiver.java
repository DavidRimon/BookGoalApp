package com.example.david.bookgoalapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by David on 14-Aug-17.
 * for more reference go to : http://androidgps.blogspot.co.il/2008/09/starting-android-service-at-boot-time.html
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // just make sure we are getting the right intent (better safe than sorry)

            ComponentName componentName = new ComponentName(context.getPackageName(),BootNotificationService.class.getName());
            Toast.makeText(context,"startup text",Toast.LENGTH_LONG).show();
            ComponentName service = context.startService(new Intent().setComponent(componentName));
            if( null ==  service) {
                //something really wrong here
                Log.e(TAG,"Received unexpected intent " + intent.toString());
            }

    }
}
