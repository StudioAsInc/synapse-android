package com.synapse.social.studioasinc;

/**
 * Configuration class for notification settings.
 * Change these values to switch between different notification methods.
 */
public class NotificationConfig {
    
    /**
     * Set to true to use server-side notifications (primary method)
     * Set to false to use client-side notifications (fallback method)
     */
    public static final boolean USE_SERVER_NOTIFICATIONS = true;
    
    /**
     * Set to true to enable automatic fallback to client-side when server fails
     * Set to false to disable fallback (will fail silently if server is down)
     */
    public static final boolean ENABLE_AUTOMATIC_FALLBACK = true;
    
    /**
     * Set to true to enable detailed logging for debugging
     * Set to false to reduce log output
     */
    public static final boolean ENABLE_DEBUG_LOGGING = true;
    
    /**
     * Timeout for server notification requests in milliseconds
     */
    public static final int SERVER_REQUEST_TIMEOUT_MS = 10000; // 10 seconds
    
    /**
     * Maximum retry attempts for server notifications
     */
    public static final int MAX_SERVER_RETRY_ATTEMPTS = 3;
    
    /**
     * Delay between retry attempts in milliseconds
     */
    public static final int RETRY_DELAY_MS = 2000; // 2 seconds
}