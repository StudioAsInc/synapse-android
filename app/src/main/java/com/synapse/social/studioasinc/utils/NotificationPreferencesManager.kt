package com.synapse.social.studioasinc.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Manages notification preferences using SharedPreferences.
 * Provides easy access to user notification settings.
 */
class NotificationPreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    
    companion object {
        // Preference keys
        private const val KEY_PUSH_NOTIFICATIONS = "push_notifications"
        private const val KEY_CHAT_MESSAGES = "chat_messages"
        private const val KEY_GROUP_UPDATES = "group_updates"
        private const val KEY_FRIEND_REQUESTS = "friend_requests"
        private const val KEY_MENTIONS = "mentions"
        private const val KEY_NOTIFICATION_SOUND = "notification_sound"
        private const val KEY_NOTIFICATION_VIBRATION = "notification_vibration"
        private const val KEY_NOTIFICATION_LED = "notification_led"
        private const val KEY_QUIET_HOURS_ENABLED = "quiet_hours_enabled"
        private const val KEY_QUIET_HOURS_START = "quiet_hours_start"
        private const val KEY_QUIET_HOURS_END = "quiet_hours_end"
    }
    
    /**
     * Check if push notifications are enabled
     */
    fun arePushNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_PUSH_NOTIFICATIONS, true)
    }
    
    /**
     * Check if chat message notifications are enabled
     */
    fun areChatNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_CHAT_MESSAGES, true) && arePushNotificationsEnabled()
    }
    
    /**
     * Check if group update notifications are enabled
     */
    fun areGroupNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_GROUP_UPDATES, true) && arePushNotificationsEnabled()
    }
    
    /**
     * Check if friend request notifications are enabled
     */
    fun areFriendRequestNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_FRIEND_REQUESTS, true) && arePushNotificationsEnabled()
    }
    
    /**
     * Check if mention notifications are enabled
     */
    fun areMentionNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_MENTIONS, true) && arePushNotificationsEnabled()
    }
    
    /**
     * Check if notification sound is enabled
     */
    fun isNotificationSoundEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATION_SOUND, true) && arePushNotificationsEnabled()
    }
    
    /**
     * Check if notification vibration is enabled
     */
    fun isNotificationVibrationEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATION_VIBRATION, true) && arePushNotificationsEnabled()
    }
    
    /**
     * Check if notification LED is enabled
     */
    fun isNotificationLedEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATION_LED, false) && arePushNotificationsEnabled()
    }
    
    /**
     * Check if quiet hours are enabled
     */
    fun areQuietHoursEnabled(): Boolean {
        return prefs.getBoolean(KEY_QUIET_HOURS_ENABLED, false)
    }
    
    /**
     * Get quiet hours start time (24-hour format)
     */
    fun getQuietHoursStart(): Int {
        return prefs.getInt(KEY_QUIET_HOURS_START, 22) // Default: 10 PM
    }
    
    /**
     * Get quiet hours end time (24-hour format)
     */
    fun getQuietHoursEnd(): Int {
        return prefs.getInt(KEY_QUIET_HOURS_END, 8) // Default: 8 AM
    }
    
    /**
     * Check if current time is within quiet hours
     */
    fun isCurrentlyQuietHours(): Boolean {
        if (!areQuietHoursEnabled()) return false
        
        val currentHour = java.time.LocalTime.now().hour
        val startHour = getQuietHoursStart()
        val endHour = getQuietHoursEnd()
        
        return if (startHour <= endHour) {
            // Same day (e.g., 8 AM to 10 PM)
            currentHour in startHour..endHour
        } else {
            // Overnight (e.g., 10 PM to 8 AM)
            currentHour >= startHour || currentHour <= endHour
        }
    }
    
    /**
     * Check if notifications should be shown based on current settings
     */
    fun shouldShowNotification(notificationType: NotificationType): Boolean {
        if (!arePushNotificationsEnabled()) return false
        if (isCurrentlyQuietHours()) return false
        
        return when (notificationType) {
            NotificationType.CHAT_MESSAGE -> areChatNotificationsEnabled()
            NotificationType.GROUP_UPDATE -> areGroupNotificationsEnabled()
            NotificationType.FRIEND_REQUEST -> areFriendRequestNotificationsEnabled()
            NotificationType.MENTION -> areMentionNotificationsEnabled()
        }
    }
    
    /**
     * Get notification priority based on type and user preferences
     */
    fun getNotificationPriority(notificationType: NotificationType): Int {
        return when (notificationType) {
            NotificationType.CHAT_MESSAGE -> android.app.NotificationManager.IMPORTANCE_HIGH
            NotificationType.MENTION -> android.app.NotificationManager.IMPORTANCE_HIGH
            NotificationType.GROUP_UPDATE -> android.app.NotificationManager.IMPORTANCE_DEFAULT
            NotificationType.FRIEND_REQUEST -> android.app.NotificationManager.IMPORTANCE_DEFAULT
        }
    }
    
    /**
     * Get notification settings summary for display
     */
    fun getNotificationSettingsSummary(): String {
        val enabledCount = listOf(
            areChatNotificationsEnabled(),
            areGroupNotificationsEnabled(),
            areFriendRequestNotificationsEnabled(),
            areMentionNotificationsEnabled()
        ).count { it }
        
        val totalCount = 4
        return "$enabledCount of $totalCount notification types enabled"
    }
    
    /**
     * Enum for different notification types
     */
    enum class NotificationType {
        CHAT_MESSAGE,
        GROUP_UPDATE,
        FRIEND_REQUEST,
        MENTION
    }
}