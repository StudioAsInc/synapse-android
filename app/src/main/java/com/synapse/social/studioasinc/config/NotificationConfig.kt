package com.synapse.social.studioasinc.config

/**
 * Configuration class for notification settings.
 * This class centralizes notification-related configuration values.
 */
object NotificationConfig {
    
    // OneSignal Configuration
    const val ONESIGNAL_APP_ID = "044e1911-6911-4871-95f9-d60003002fe2"
    
    // OneSignal REST API Key - Replace this with your actual REST API key
    // For production, consider storing this in BuildConfig or encrypted preferences
    const val ONESIGNAL_REST_API_KEY = "os_v2_app_arhbseljcfehdfpz2yaagabp4j7fcvm6rqluzmeowmqsz7jztqdpdrc2jvmuijfzurukbzkhanucy2woctycxs4bv563rlpizdn5whq"
    
    // OneSignal REST API URL
    const val ONESIGNAL_REST_API_URL = "https://onesignal.com/api/v1/notifications"
    
    // Notification Channel ID for Android - dedicated channel for OneSignal notifications
    const val CHAT_MESSAGES_CHANNEL_ID = "chat_messages"
    
    // Notification Icon Resource Name
    const val NOTIFICATION_ICON = "ic_notification"
    
    /**
     * Checks if the OneSignal REST API key is properly configured.
     * @return true if the key is configured, false otherwise
     */
    fun isApiKeyConfigured(): Boolean {
        return ONESIGNAL_REST_API_KEY != "os_v2_app_arhbseljcfehdfpz2yaagabp4j7fcvm6rqluzmeowmqsz7jztqdpdrc2jvmuijfzurukbzkhanucy2woctycxs4bjvmuijfzurukbzkhanucy2woctycxs4bv563rlpizdn5whq" && 
               ONESIGNAL_REST_API_KEY.isNotBlank()
    }
}