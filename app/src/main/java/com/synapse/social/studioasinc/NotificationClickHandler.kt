package com.synapse.social.studioasinc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.notifications.INotificationClickListener
import com.onesignal.notifications.INotificationClickEvent
import org.json.JSONObject

/**
 * Handles OneSignal notification clicks and navigates users to appropriate content.
 * 
 * This class implements deep linking functionality to direct users to:
 * - Chat messages: Opens ChatActivity with the specific chat
 * - New followers: Opens ProfileActivity of the follower
 * - Post-related notifications: Opens HomeActivity and navigates to the post
 * - Profile likes: Opens ProfileActivity of the user who liked
 * 
 * Features:
 * - Robust error handling and logging
 * - Fallback to HomeActivity when navigation fails
 * - Support for all notification types defined in NotificationConfig
 */
class NotificationClickHandler : INotificationClickListener {
    
    companion object {
        private const val TAG = "NotificationClickHandler"
        
        /**
         * Registers the notification click handler with OneSignal.
         * Should be called during app initialization.
         */
        @JvmStatic
        fun register() {
            try {
                com.onesignal.OneSignal.getNotifications().addClickListener(NotificationClickHandler())
                Log.i(TAG, "OneSignal notification click handler registered successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to register OneSignal notification click handler", e)
            }
        }
    }
    
    override fun onClick(event: INotificationClickEvent) {
        try {
            Log.d(TAG, "Notification clicked: ${event.notification.notificationId}")
            
            val context = SynapseApp.getCurrentActivity()?.get()
            if (context == null) {
                Log.e(TAG, "No current activity available for navigation")
                return
            }
            
            // Get notification data
            val additionalData = event.notification.additionalData
            if (additionalData == null || additionalData.length() == 0) {
                Log.w(TAG, "No additional data in notification, opening HomeActivity")
                openHomeActivity(context)
                return
            }
            
            Log.d(TAG, "Notification additional data: $additionalData")
            
            // Parse notification type and handle accordingly
            val notificationType = additionalData.optString("type", "")
            when (notificationType) {
                NotificationConfig.NOTIFICATION_TYPE_CHAT_MESSAGE -> handleChatMessage(context, additionalData)
                NotificationConfig.NOTIFICATION_TYPE_NEW_FOLLOWER -> handleNewFollower(context, additionalData)
                NotificationConfig.NOTIFICATION_TYPE_PROFILE_LIKE -> handleProfileLike(context, additionalData)
                NotificationConfig.NOTIFICATION_TYPE_NEW_LIKE_POST -> handlePostNotification(context, additionalData)
                NotificationConfig.NOTIFICATION_TYPE_NEW_COMMENT -> handlePostNotification(context, additionalData)
                NotificationConfig.NOTIFICATION_TYPE_NEW_REPLY -> handlePostNotification(context, additionalData)
                NotificationConfig.NOTIFICATION_TYPE_NEW_POST -> handlePostNotification(context, additionalData)
                NotificationConfig.NOTIFICATION_TYPE_MENTION_POST -> handlePostNotification(context, additionalData)
                else -> {
                    Log.w(TAG, "Unknown notification type: $notificationType, opening HomeActivity")
                    openHomeActivity(context)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error handling notification click", e)
            // Fallback to home activity
            val context = SynapseApp.getCurrentActivity()?.get()
            if (context != null) {
                openHomeActivity(context)
            }
        }
    }
    
    /**
     * Handles chat message notifications by opening ChatActivity with the specific chat.
     */
    private fun handleChatMessage(context: Context, data: JSONObject) {
        try {
            val chatId = data.optString("chatId", "")
            val senderUid = data.optString("sender_uid", "")
            
            if (chatId.isBlank() && senderUid.isBlank()) {
                Log.e(TAG, "Chat notification missing chatId and sender_uid")
                openHomeActivity(context)
                return
            }
            
            // Determine recipient UID (the other person in the chat)
            val currentUid = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUid == null) {
                Log.e(TAG, "User not authenticated, cannot open chat")
                openHomeActivity(context)
                return
            }
            
            val recipientUid = if (senderUid.isNotBlank()) {
                senderUid
            } else {
                // Parse chatId to extract the other user's UID
                val parts = chatId.split("_")
                if (parts.size == 2) {
                    if (parts[0] == currentUid) parts[1] else parts[0]
                } else {
                    Log.e(TAG, "Invalid chatId format: $chatId")
                    openHomeActivity(context)
                    return
                }
            }
            
            Log.d(TAG, "Opening ChatActivity for recipient: $recipientUid")
            
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra("uid", recipientUid)
                putExtra("origin", "NotificationClick")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            
            context.startActivity(intent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error handling chat message notification", e)
            openHomeActivity(context)
        }
    }
    
    /**
     * Handles new follower notifications by opening the follower's ProfileActivity.
     */
    private fun handleNewFollower(context: Context, data: JSONObject) {
        try {
            val senderUid = data.optString("sender_uid", "")
            
            if (senderUid.isBlank()) {
                Log.e(TAG, "New follower notification missing sender_uid")
                openHomeActivity(context)
                return
            }
            
            Log.d(TAG, "Opening ProfileActivity for new follower: $senderUid")
            
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra("uid", senderUid)
                putExtra("origin", "NotificationClick")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            
            context.startActivity(intent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error handling new follower notification", e)
            openHomeActivity(context)
        }
    }
    
    /**
     * Handles profile like notifications by opening the liker's ProfileActivity.
     */
    private fun handleProfileLike(context: Context, data: JSONObject) {
        try {
            val senderUid = data.optString("sender_uid", "")
            
            if (senderUid.isBlank()) {
                Log.e(TAG, "Profile like notification missing sender_uid")
                openHomeActivity(context)
                return
            }
            
            Log.d(TAG, "Opening ProfileActivity for profile liker: $senderUid")
            
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra("uid", senderUid)
                putExtra("origin", "NotificationClick")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            
            context.startActivity(intent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error handling profile like notification", e)
            openHomeActivity(context)
        }
    }
    
    /**
     * Handles post-related notifications (likes, comments, replies, mentions, new posts)
     * by opening HomeActivity. The post will be visible in the home feed.
     * 
     * Note: Since there's no dedicated PostActivity, we navigate to HomeActivity
     * where posts are displayed in the HomeFragment.
     */
    private fun handlePostNotification(context: Context, data: JSONObject) {
        try {
            val postId = data.optString("postId", "")
            val senderUid = data.optString("sender_uid", "")
            
            Log.d(TAG, "Opening HomeActivity for post notification. PostId: $postId, SenderUid: $senderUid")
            
            // For now, we'll open HomeActivity where the user can see their posts
            // In the future, we could implement a specific post viewer or scroll to the post
            val intent = Intent(context, HomeActivity::class.java).apply {
                if (postId.isNotBlank()) {
                    putExtra("postId", postId)
                }
                if (senderUid.isNotBlank()) {
                    putExtra("senderUid", senderUid)
                }
                putExtra("origin", "NotificationClick")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            
            context.startActivity(intent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error handling post notification", e)
            openHomeActivity(context)
        }
    }
    
    /**
     * Fallback method to open HomeActivity when navigation fails or data is insufficient.
     */
    private fun openHomeActivity(context: Context) {
        try {
            Log.d(TAG, "Opening HomeActivity as fallback")
            
            val intent = Intent(context, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            
            context.startActivity(intent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open HomeActivity as fallback", e)
        }
    }
}