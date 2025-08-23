package com.synapse.social.studioasinc

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * A utility object for handling notification logic.
 * Methods are Java-friendly using @JvmStatic.
 */
object NotificationHelper {

    private const val TAG = "NotificationHelper"
    private const val WORKER_URL = "https://my-app-notification-sender.mashikahamed0.workers.dev"
    private val JSON = "application/json; charset=utf-8".toMediaType()

    /**
     * Checks recipient status and notifies if needed.
     * This is non-suspending and uses the Task API for Java interop.
     */
    @JvmStatic
    fun sendMessageAndNotifyIfNeeded(
        senderUid: String,
        recipientUid: String,
        recipientOneSignalPlayerId: String,
        message: String
    ) {
        val recipientStatusRef = FirebaseDatabase.getInstance().getReference("/status/$recipientUid")

        recipientStatusRef.get().addOnSuccessListener { dataSnapshot ->
            val recipientStatus = dataSnapshot.getValue(String::class.java)
            val suppressStatus = "chatting_with_$senderUid"

            // Don't send if recipient is in this chat.
            if (suppressStatus == recipientStatus) {
                Log.i(TAG, "Recipient is in chat. Suppressing notification.")
            } else {
                // Send in all other cases (offline, online, null).
                Log.i(TAG, "Recipient not in chat. Sending notification.")
                triggerPushNotification(recipientOneSignalPlayerId, message)
            }
        }.addOnFailureListener { e ->
            // On error, default to sending notification.
            Log.e(TAG, "Status check failed. Defaulting to send notification.", e)
            triggerPushNotification(recipientOneSignalPlayerId, message)
        }

        // TODO: Save the actual message to your DB here.
    }

    /**
     * Triggers a push notification via the Cloudflare Worker.
     */
    @JvmStatic
    fun triggerPushNotification(recipientId: String, message: String) {
        val client = OkHttpClient()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("recipientUserId", recipientId)
            jsonBody.put("notificationMessage", message)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create JSON for notification", e)
            return
        }

        val body = jsonBody.toString().toRequestBody(JSON)
        val request = Request.Builder()
            .url(WORKER_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to send notification", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        Log.i(TAG, "Notification sent successfully.")
                    } else {
                        Log.e(TAG, "Failed to send notification: ${it.code}")
                    }
                }
            }
        })
    }
}
