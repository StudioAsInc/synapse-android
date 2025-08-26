package com.synapse.social.studioasinc

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * Enhanced notification system supporting both server-side and client-side OneSignal notifications.
 * 
 * Features:
 * - Toggle between server-side (Cloudflare Workers) and client-side (OneSignal REST API) notification sending
 * - Smart notification suppression when both users are actively chatting
 * - Fallback mechanisms for reliability
 * - Configurable notification settings
 */
object NotificationHelper {

    private const val TAG = "NotificationHelper"
    
    private val JSON = "application/json; charset=utf-8".toMediaType()
    private const val ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications"
    
    /**
     * Enhanced notification sending with smart presence checking and dual system support.
     * 
     * @param senderUid The UID of the message sender
     * @param recipientUid The UID of the message recipient
     * @param recipientOneSignalPlayerId The OneSignal Player ID of the recipient
     * @param message The message content to send in the notification
     * @param chatId Optional chat ID for deep linking (can be null)
     */
    @JvmStatic
    fun sendMessageAndNotifyIfNeeded(
        senderUid: String,
        recipientUid: String,
        recipientOneSignalPlayerId: String,
        message: String,
        chatId: String? = null
    ) {
        if (recipientOneSignalPlayerId.isBlank()) {
            Log.w(TAG, "Recipient OneSignal Player ID is blank. Cannot send notification.")
            return
        }

        val recipientStatusRef = FirebaseDatabase.getInstance().getReference("/skyline/users/$recipientUid/status")

        recipientStatusRef.get().addOnSuccessListener { dataSnapshot ->
            val recipientStatus = dataSnapshot.getValue(String::class.java)
            val suppressStatus = "chatting_with_$senderUid"

            // Smart notification suppression based on user status
            if (NotificationConfig.ENABLE_SMART_SUPPRESSION) {
                // Don't send notification if recipient is actively chatting with the sender
                if (suppressStatus == recipientStatus) {
                    if (NotificationConfig.ENABLE_DEBUG_LOGGING) {
                        Log.i(TAG, "Recipient is actively chatting with sender. Suppressing notification.")
                    }
                    return@addOnSuccessListener
                }
                
                // Don't send notification if recipient is online (they'll see the message in real-time)
                if (recipientStatus == "online") {
                    if (NotificationConfig.ENABLE_DEBUG_LOGGING) {
                        Log.i(TAG, "Recipient is online. Suppressing notification for real-time message visibility.")
                    }
                    return@addOnSuccessListener
                }
            }

            // Send notification in all other cases (offline, null status, or other statuses)
            if (NotificationConfig.ENABLE_DEBUG_LOGGING) {
                Log.i(TAG, "Recipient not in chat and not online. Sending notification.")
            }
            
            if (NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                sendClientSideNotification(recipientOneSignalPlayerId, message, senderUid, chatId)
            } else {
                sendServerSideNotification(recipientOneSignalPlayerId, message)
            }
        }.addOnFailureListener { e ->
            // On error, default to sending notification via the configured system
            Log.e(TAG, "Status check failed. Defaulting to send notification.", e)
            if (NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                sendClientSideNotification(recipientOneSignalPlayerId, message, senderUid, chatId)
            } else {
                sendServerSideNotification(recipientOneSignalPlayerId, message)
            }
        }
    }

    /**
     * Sends notification via the existing Cloudflare Worker (server-side).
     */
    @JvmStatic
    fun sendServerSideNotification(recipientId: String, message: String) {
        val client = OkHttpClient()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("recipientUserId", recipientId)
            jsonBody.put("notificationMessage", message)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create JSON for server-side notification", e)
            return
        }

        val body = jsonBody.toString().toRequestBody(JSON)
        val request = Request.Builder()
            .url(NotificationConfig.WORKER_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to send server-side notification", e)
                // Fallback to client-side if server fails
                if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && !NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                    Log.i(TAG, "Falling back to client-side notification due to server failure")
                    sendClientSideNotification(recipientId, message, null, null)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        Log.i(TAG, "Server-side notification sent successfully.")
                    } else {
                        Log.e(TAG, "Failed to send server-side notification: ${it.code}")
                        // Fallback to client-side if server returns error
                        if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && !NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                            Log.i(TAG, "Falling back to client-side notification due to server error")
                            sendClientSideNotification(recipientId, message, null, null)
                        }
                    }
                }
            }
        })
    }

    /**
     * Sends notification directly via OneSignal REST API (client-side).
     */
    @JvmStatic
    fun sendClientSideNotification(
        recipientPlayerId: String, 
        message: String, 
        senderUid: String? = null,
        chatId: String? = null
    ) {
        val client = OkHttpClient()
        val jsonBody = JSONObject()
        
        try {
            // Required OneSignal fields
            jsonBody.put("app_id", NotificationConfig.ONESIGNAL_APP_ID)
            jsonBody.put("include_player_ids", JSONObject().put("0", recipientPlayerId))
            jsonBody.put("contents", JSONObject().put("en", message))
            jsonBody.put("headings", JSONObject().put("en", NotificationConfig.NOTIFICATION_TITLE))
            jsonBody.put("subtitle", JSONObject().put("en", NotificationConfig.NOTIFICATION_SUBTITLE))
            
            // Optional deep linking data
            if (NotificationConfig.ENABLE_DEEP_LINKING) {
                val data = JSONObject()
                if (senderUid != null) {
                    data.put("sender_uid", senderUid)
                }
                if (chatId != null) {
                    data.put("chat_id", chatId)
                }
                data.put("type", "chat_message")
                jsonBody.put("data", data)
            }
            
            // Notification settings
            jsonBody.put("priority", NotificationConfig.NOTIFICATION_PRIORITY)
            jsonBody.put("android_channel_id", NotificationConfig.NOTIFICATION_CHANNEL_ID)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create JSON for client-side notification", e)
            return
        }

        val body = jsonBody.toString().toRequestBody(JSON)
        val request = Request.Builder()
            .url(ONESIGNAL_API_URL)
            .addHeader("Authorization", "Basic ${NotificationConfig.ONESIGNAL_REST_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to send client-side notification", e)
                // Fallback to server-side if client-side fails
                if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                    Log.i(TAG, "Falling back to server-side notification due to client-side failure")
                    sendServerSideNotification(recipientPlayerId, message)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        Log.i(TAG, "Client-side notification sent successfully.")
                    } else {
                        Log.e(TAG, "Failed to send client-side notification: ${it.code} - ${it.body?.string()}")
                        // Fallback to server-side if client-side returns error
                        if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                            Log.i(TAG, "Falling back to server-side notification due to client-side error")
                            sendServerSideNotification(recipientPlayerId, message)
                        }
                    }
                }
            }
        })
    }

    /**
     * Legacy method for backward compatibility.
     * @deprecated Use sendMessageAndNotifyIfNeeded with chatId parameter instead
     */
    @JvmStatic
    @Deprecated("Use sendMessageAndNotifyIfNeeded with chatId parameter for better deep linking")
    fun triggerPushNotification(recipientId: String, message: String) {
        sendMessageAndNotifyIfNeeded("", "", recipientId, message)
    }

    /**
     * Gets the current notification system being used.
     * @return true if using client-side notifications, false if using server-side
     */
    @JvmStatic
    fun isUsingClientSideNotifications(): Boolean {
        return NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS
    }

    /**
     * Checks if the notification system is properly configured.
     * @return true if configuration is valid, false otherwise
     */
    @JvmStatic
    fun isNotificationSystemConfigured(): Boolean {
        return NotificationConfig.isConfigurationValid()
    }
}
