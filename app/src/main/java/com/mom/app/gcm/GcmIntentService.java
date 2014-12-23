package com.mom.app.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mom.app.R;
import com.mom.app.activity.BaseActivity;
import com.mom.app.utils.AppConstants;

/**
 * Created by vaibhavsinha on 9/23/14.
 */
public class GcmIntentService extends IntentService {
    String _LOG = AppConstants.LOG_PREFIX + "GCM INTENT";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(_LOG, "GcmIntentService::onHandleIntent");

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                Log.i(_LOG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
//                sendNotification("Received: " + extras.toString());
                handleGcmMessage(extras);
                Log.i(_LOG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void handleGcmMessage(Bundle extras){
        Log.d(_LOG, "GcmIntentService::handleGcmMessage: " + extras);

        if(extras == null){
            Log.i(_LOG, "null message received from GCM");
            return;
        }

        String jsonReceived = extras.getString(AppConstants.PARAM_GCM_PAYLOAD);

        if(TextUtils.isEmpty(jsonReceived)){
            Log.w(_LOG, "No payload received. Can't do anything!");
            return;
        }

        Intent intent = new Intent(AppConstants.GCM_INTENT);
        intent.putExtra(AppConstants.PARAM_GCM_PAYLOAD, jsonReceived);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        Log.d(_LOG, "sendNotification");
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, BaseActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.momicon)
                        .setContentTitle("MoM Alert")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}