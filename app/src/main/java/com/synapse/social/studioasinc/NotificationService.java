package com.synapse.social.studioasinc;

import android.util.Log;
import com.onesignal.OSNotification;
import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal.OSRemoteNotificationReceivedHandler;
import androidx.core.app.NotificationCompat;
import com.onesignal.NotificationServiceExtension;

public class NotificationService extends NotificationServiceExtension {

    @Override
    protected void onNotificationReceived(OSNotificationReceivedEvent event) {
        OSNotification notification = event.getNotification();
        OSMutableNotification mutableNotification = notification.mutableCopy();

        mutableNotification.setExtender(new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                return builder.setColor(0xFF00FF00);
            }
        });

        Log.d("OneSignal", "Notification received with content: " + notification.getBody());

        // Complete with a modified notification
        event.complete(mutableNotification);
    }
}
