package com.synapse.social.studioasinc

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.io.IOException

/**
 * Checks the recipient's status and sends a push notification if they are not
 * actively in the chat with the sender.
 *
 * This is the core logic for the smart notification system.
 * It should be called from a coroutine scope (e.g., `viewModelScope.launch`).
 *
 * @param senderUid The Firebase UID of the message sender.
 * @param recipientUid The Firebase UID of the message recipient.
 * @param recipientOneSignalPlayerId The OneSignal Player ID of the recipient.
 * @param message The message content for the notification.
 */
suspend fun sendMessageAndNotifyIfNeeded(
    senderUid: String,
    recipientUid: String,
    recipientOneSignalPlayerId: String,
    message: String
) {
    val recipientStatusRef = FirebaseDatabase.getInstance().getReference("/status/$recipientUid")

    try {
        val snapshot = recipientStatusRef.get().await()
        val recipientStatus = snapshot.getValue(String::class.java)

        // The condition to NOT send a notification.
        // We only withhold the notification if we are certain the recipient is
        // in this specific chat screen.
        val shouldSuppressNotification = recipientStatus == "chatting_with_$senderUid"

        if (shouldSuppressNotification) {
            Log.i("NotificationSender", "Recipient is already in chat. Suppressing notification.")
        } else {
            // In all other cases (offline, online but not in this chat, status doesn't exist, or read failed),
            // we send the notification.
            Log.i("NotificationSender", "Recipient is not in chat or status is unavailable. Sending notification.")
            triggerPushNotification(recipientOneSignalPlayerId, message)
        }

    } catch (e: Exception) {
        // As per requirements, if the status check fails, default to sending the notification.
        Log.e("NotificationSender", "Error checking recipient status. Defaulting to send notification.", e)
        triggerPushNotification(recipientOneSignalPlayerId, message)
    }

    // TODO: After checking for notification, save the actual message to your chat database here.
    // For example: saveMessageToFirestore(senderUid, recipientUid, message)
}


/**
 * A helper function to trigger a push notification via the Cloudflare Worker.
 *
 * @param recipientId The OneSignal Player ID of the recipient.
 * @param message The content of the notification message.
 */
fun triggerPushNotification(recipientId: String, message: String) {
    val client = OkHttpClient()

    val workerUrl = "https://my-app-notification-sender.mashikahamed0.workers.dev"
    val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    // 1. Create the JSON payload
    val jsonObject = JSONObject()
    jsonObject.put("recipientUserId", recipientId)
    jsonObject.put("notificationMessage", message)
    val requestBody = jsonObject.toString().toRequestBody(jsonMediaType)

    // 2. Build the request
    val request = Request.Builder()
        .url(workerUrl)
        .post(requestBody)
        .build()

    // 3. Execute the request asynchronously
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("NotificationSender", "Failed to send notification", e)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.i("NotificationSender", "Notification sent successfully. Response: ${response.body?.string()}")
            } else {
                Log.e("NotificationSender", "Failed to send notification. Code: ${response.code}, Message: ${response.message}")
            }
            // Ensure the response body is closed to avoid resource leaks
            response.close()
        }
    })
}
