package com.synapse.social.studioasinc;

import androidx.annotation.Keep;
import com.onesignal.notifications.INotificationServiceExtension;
import com.onesignal.notifications.INotificationReceivedEvent;
import com.onesignal.notifications.IDisplayableMutableNotification;

@Keep
public class NotificationService implements INotificationServiceExtension {
    @Override
    public void onNotificationReceived(INotificationReceivedEvent event) {
        // Example: prevent the notification from showing
        // event.preventDefault();

        // Or access/modify the notification before it's displayed:
        IDisplayableMutableNotification n = event.getDisplayableNotification();
        // n.setSmallIcon("ic_stat_onesignal_default");
        // ... etc.
    }
}
