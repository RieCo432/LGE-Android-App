package com.lumi_dos.lge;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GCMService extends IntentService{

    private String TAG = "Broadcast receiver";

    public GCMService() {
        super("Listener-Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GCMBroadCastReceiver.completeWakefulIntent(intent);

        Log.i(TAG, "Notification receive " + intent.getExtras().toString());

        Bundle payload = intent.getExtras();

        String title = payload.getString("gcm.notification.title");
        String body = payload.getString("gcm.notification.body");

        /*Log.i(TAG, "Title: "+title);
        Log.i(TAG, "Body: "+body);*/

        sendNotification(title, body);
    }

    public void sendNotification(String title, String body) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, MainActivity.class), 0);

        long vibration[] = {1000};

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL);

        mBuilder.setContentIntent(contentIntent);
        int NOTIFICATION_ID = 1234;
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
