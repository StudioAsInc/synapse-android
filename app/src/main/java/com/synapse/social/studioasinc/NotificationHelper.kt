package com.synapse.social.studioasinc

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.onesignal.OneSignal
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
        // Validate inputs
        if (senderUid.isBlank() || recipientUid.isBlank() || recipientOneSignalPlayerId.isBlank()) {
            Log.e(TAG, "Invalid parameters: senderUid=$senderUid, recipientUid=$recipientUid, playerId=$recipientOneSignalPlayerId")
            return
        }

        Log.d(TAG, "Checking notification status for message from $senderUid to $recipientUid")
        
        val recipientStatusRef = FirebaseDatabase.getInstance().getReference("/status/$recipientUid")

        recipientStatusRef.get().addOnSuccessListener { dataSnapshot ->
            val recipientStatus = dataSnapshot.getValue(String::class.java)
            val suppressStatus = "chatting_with_$senderUid"

            // Don't send if recipient is in this chat.
            if (suppressStatus == recipientStatus) {
                Log.i(TAG, "Recipient is in chat. Suppressing notification.")
            } else {
                // Send in all other cases (offline, online, null).
                Log.i(TAG, "Recipient not in chat. Sending notification. Status: $recipientStatus")
                sendClientSideNotification(recipientOneSignalPlayerId, message, senderUid)
            }
        }.addOnFailureListener { e ->
            // On error, default to sending notification.
            Log.e(TAG, "Status check failed. Defaulting to send notification.", e)
            sendClientSideNotification(recipientOneSignalPlayerId, message, senderUid)
        }

        // TODO: Save the actual message to your DB here.
    }

    /**
     * Sends a push notification directly from the client using OneSignal's REST API.
     */
    @JvmStatic
    fun sendClientSideNotification(recipientPlayerId: String, message: String, senderUid: String) {
        // Check if API key is configured
        if (!NotificationConfig.isApiKeyConfigured()) {
            Log.e(TAG, "OneSignal REST API key not configured. Please set the API key in NotificationConfig.")
            return
        }

        // Validate inputs
        if (recipientPlayerId.isBlank() || message.isBlank()) {
            Log.e(TAG, "Invalid notification parameters: playerId=$recipientPlayerId, message=$message")
            return
        }

        try {
            Log.d(TAG, "Preparing client-side notification for player: $recipientPlayerId")
            
            // Create notification content for OneSignal REST API
            val notificationContent = JSONObject().apply {
                put("app_id", NotificationConfig.ONESIGNAL_APP_ID)
                put("include_player_ids", arrayOf(recipientPlayerId))
                put("contents", JSONObject().put("en", message))
                put("headings", JSONObject().put("en", "New Message"))
                put("data", JSONObject().put("senderUid", senderUid))
                put("android_channel_id", NotificationConfig.CHAT_MESSAGES_CHANNEL_ID)
                put("small_icon", NotificationConfig.NOTIFICATION_ICON)
                put("priority", 10) // High priority for chat messages
            }

            Log.d(TAG, "Notification payload: ${notificationContent.toString()}")

            // Send notification using HTTP client to OneSignal REST API
            sendNotificationToOneSignal(notificationContent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to prepare client-side notification", e)
        }
    }

    /**
     * Sends the notification to OneSignal using their REST API.
     */
    private fun sendNotificationToOneSignal(notificationContent: JSONObject) {
        val client = OkHttpClient()
        
        val body = notificationContent.toString().toRequestBody(JSON)
        val request = Request.Builder()
            .url(NotificationConfig.ONESIGNAL_REST_API_URL)
            .post(body)
            .addHeader("Authorization", "Basic ${NotificationConfig.ONESIGNAL_REST_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .build()

        Log.d(TAG, "Sending notification to OneSignal REST API...")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to send notification to OneSignal", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        val responseBody = it.body?.string() ?: "No response body"
                        Log.i(TAG, "Client-side notification sent successfully via OneSignal REST API")
                        Log.d(TAG, "OneSignal response: $responseBody")
                    } else {
                        val responseBody = it.body?.string() ?: "No response body"
                        Log.e(TAG, "Failed to send notification via OneSignal REST API: ${it.code}")
                        Log.e(TAG, "Response body: $responseBody")
                        Log.e(TAG, "Request URL: ${it.request.url}")
                        Log.e(TAG, "Request headers: ${it.request.headers}")
                    }
                }
            }
        })
    }

    /**
     * Legacy method - kept for backward compatibility but now redirects to client-side
     */
    @JvmStatic
    fun triggerPushNotification(recipientId: String, message: String) {
        Log.w(TAG, "triggerPushNotification called - redirecting to client-side implementation")
        // Redirect to client-side notification
        sendClientSideNotification(recipientId, message, "")
    }

    /**
     * Test method to verify notification configuration
     */
    @JvmStatic
    fun testNotificationConfiguration() {
        Log.i(TAG, "=== Notification Configuration Test ===")
        Log.i(TAG, "OneSignal App ID: ${NotificationConfig.ONESIGNAL_APP_ID}")
        Log.i(TAG, "OneSignal REST API URL: ${NotificationConfig.ONESIGNAL_REST_API_URL}")
        Log.i(TAG, "Notification Channel ID: ${NotificationConfig.CHAT_MESSAGES_CHANNEL_ID}")
        Log.i(TAG, "API Key Configured: ${NotificationConfig.isApiKeyConfigured()}")
        Log.i(TAG, "========================================")
    }
}
