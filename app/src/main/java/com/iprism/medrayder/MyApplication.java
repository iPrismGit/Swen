package com.iprism.medrayder;

import android.app.Application;
import android.util.Log;

import com.onesignal.OneSignal;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize OneSignal
        OneSignal.initWithContext(this);

        // Replace with your OneSignal App ID
        OneSignal.setAppId("cebaa375-de95-4fb8-9403-71089f304ffe");
        Log.d("OneSignal", "Device is subscribed: " + OneSignal.getDeviceState().isSubscribed());

        // Set the custom notification handler
        /*OneSignal.setNotificationWillShowInForegroundHandler(
                (OneSignal.OSNotificationWillShowInForegroundHandler) new OneSignalServiceNotifications()
        );*/
        OneSignal.setNotificationWillShowInForegroundHandler(notificationReceivedEvent -> {
            Log.d("OneSignalNotification", "Title: " + notificationReceivedEvent.getNotification().getTitle());
            Log.d("OneSignalNotification", "Body: " + notificationReceivedEvent.getNotification().getBody());

            // Complete processing to display the notification
            notificationReceivedEvent.complete(notificationReceivedEvent.getNotification());
        });

        /*// Set the remote notification handler
        OneSignal.setRemoteNotificationReceivedHandler(new OneSignalServiceNotifications());*/
    }
}
