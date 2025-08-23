package com.synapse.social.studioasinc;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A utility class for handling notification logic.
 * This class uses static methods and is not meant to be instantiated.
 */
public final class NotificationHelper {

    private static final String TAG = "NotificationHelper";
    private static final String WORKER_URL = "https://my-app-notification-sender.mashikahamed0.workers.dev";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private NotificationHelper() {}

    /**
     * Checks recipient status and notifies if needed using Firebase Tasks.
     * @param senderUid The UID of the message sender.
     * @param recipientUid The UID of the message recipient.
     * @param recipientOneSignalPlayerId The OneSignal Player ID of the recipient.
     * @param message The message content for the notification.
     */
    public static void sendMessageAndNotifyIfNeeded(
            String senderUid,
            String recipientUid,
            String recipientOneSignalPlayerId,
            String message) {

        DatabaseReference recipientStatusRef = FirebaseDatabase.getInstance().getReference("/status/" + recipientUid);

        recipientStatusRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String recipientStatus = dataSnapshot.getValue(String.class);
                String suppressStatus = "chatting_with_" + senderUid;

                // Don't send if recipient is in this chat.
                if (suppressStatus.equals(recipientStatus)) {
                    Log.i(TAG, "Recipient is in chat. Suppressing notification.");
                } else {
                    // Send in all other cases (offline, online, null).
                    Log.i(TAG, "Recipient not in chat. Sending notification.");
                    triggerPushNotification(recipientOneSignalPlayerId, message);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // On error, default to sending notification.
                Log.e(TAG, "Status check failed. Defaulting to send notification.", e);
                triggerPushNotification(recipientOneSignalPlayerId, message);
            }
        });

        // TODO: Save the actual message to your DB here.
    }

    /**
     * Triggers a push notification via the Cloudflare Worker.
     * @param recipientId The OneSignal Player ID of the recipient.
     * @param message The content of the notification message.
     */
    public static void triggerPushNotification(String recipientId, String message) {
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("recipientUserId", recipientId);
            jsonBody.put("notificationMessage", message);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create JSON for notification", e);
            return;
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(WORKER_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Failed to send notification", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Notification sent successfully.");
                } else {
                    Log.e(TAG, "Failed to send notification: " + response.code());
                }
                response.close();
            }
        });
    }
}
