package com.synapse.social.studioasinc

/**
 * Configuration class for notification system settings.
 * 
 * This class centralizes all notification-related configuration to make it easy
 * to switch between different notification systems and manage credentials.
 */
object NotificationConfig {
    
    // ===== NOTIFICATION SYSTEM TOGGLE =====
    // Set to true to use client-side OneSignal notifications
    // Set to false to use server-side Cloudflare Worker notifications
    const val USE_CLIENT_SIDE_NOTIFICATIONS = false
    
    // ===== ONESIGNAL CONFIGURATION =====
    // Replace these placeholder values with your actual OneSignal credentials
    const val ONESIGNAL_APP_ID = "YOUR_ONESIGNAL_APP_ID_HERE"
    const val ONESIGNAL_REST_API_KEY = "YOUR_ONESIGNAL_REST_API_KEY_HERE"
    
    // ===== NOTIFICATION SETTINGS =====
    const val NOTIFICATION_TITLE = "New Message"
    const val NOTIFICATION_SUBTITLE = "Synapse Social"
    const val NOTIFICATION_CHANNEL_ID = "chat_messages"
    const val NOTIFICATION_PRIORITY = 10 // High priority
    
    // ===== SERVER-SIDE CONFIGURATION =====
    const val WORKER_URL = "https://my-app-notification-sender.mashikahamed0.workers.dev"
    
    // ===== PRESENCE SETTINGS =====
    // Time threshold (in milliseconds) to consider a user "recently active"
    // Users who were active within this time will not receive notifications
    const val RECENT_ACTIVITY_THRESHOLD = 5 * 60 * 1000L // 5 minutes
    
    // ===== FEATURE FLAGS =====
    // Enable/disable smart notification suppression
    const val ENABLE_SMART_SUPPRESSION = true
    
    // Enable/disable fallback mechanisms
    const val ENABLE_FALLBACK_MECHANISMS = true
    
    // Enable/disable deep linking in notifications
    const val ENABLE_DEEP_LINKING = true
    
    // ===== DEBUG SETTINGS =====
    // Enable detailed logging for notification system
    const val ENABLE_DEBUG_LOGGING = true
    
    /**
     * Validates the current notification configuration.
     * @return true if configuration is valid, false otherwise
     */
    fun isConfigurationValid(): Boolean {
        return if (USE_CLIENT_SIDE_NOTIFICATIONS) {
            ONESIGNAL_APP_ID != "YOUR_ONESIGNAL_APP_ID_HERE" && 
            ONESIGNAL_REST_API_KEY != "YOUR_ONESIGNAL_REST_API_KEY_HERE"
        } else {
            true // Server-side is always considered valid
        }
    }
    
    /**
     * Gets a human-readable description of the current notification system.
     * @return Description of the active notification system
     */
    fun getNotificationSystemDescription(): String {
        return if (USE_CLIENT_SIDE_NOTIFICATIONS) {
            "Client-side OneSignal REST API"
        } else {
            "Server-side Cloudflare Worker"
        }
    }
    
    /**
     * Gets configuration status for debugging purposes.
     * @return Map containing configuration status
     */
    fun getConfigurationStatus(): Map<String, Any> {
        return mapOf(
            "useClientSideNotifications" to USE_CLIENT_SIDE_NOTIFICATIONS,
            "systemDescription" to getNotificationSystemDescription(),
            "isConfigurationValid" to isConfigurationValid(),
            "enableSmartSuppression" to ENABLE_SMART_SUPPRESSION,
            "enableFallbackMechanisms" to ENABLE_FALLBACK_MECHANISMS,
            "enableDeepLinking" to ENABLE_DEEP_LINKING,
            "enableDebugLogging" to ENABLE_DEBUG_LOGGING,
            "recentActivityThreshold" to RECENT_ACTIVITY_THRESHOLD
        )
    }
}