package com.synapse.social.studioasinc;

/**
 * Enhanced notification system supporting both server-side and client-side OneSignal notifications.
 *
 * Features:
 * - Toggle between server-side (Cloudflare Workers) and client-side (OneSignal REST API) notification sending
 * - Smart notification suppression when both users are actively chatting
 * - Fallback mechanisms for reliability
 * - Configurable notification settings
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J@\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00052\u0016\b\u0002\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0010H\u0007J4\u0010\u0011\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0005H\u0007J8\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00052\u0016\b\u0002\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0010H\u0007JD\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000e\u001a\u00020\u00052\u0016\b\u0002\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0010H\u0007J\u0018\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u0005H\u0007J\b\u0010\u0019\u001a\u00020\u001aH\u0007J\b\u0010\u001b\u001a\u00020\u001aH\u0007J>\u0010\u001c\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00052\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0010H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/synapse/social/studioasinc/NotificationHelper;", "", "<init>", "()V", "TAG", "", "JSON", "Lokhttp3/MediaType;", "ONESIGNAL_API_URL", "sendNotification", "", "recipientUid", "senderUid", "message", "notificationType", "data", "", "sendMessageAndNotifyIfNeeded", "recipientOneSignalPlayerId", "chatId", "sendServerSideNotification", "recipientId", "sendClientSideNotification", "recipientPlayerId", "triggerPushNotification", "isUsingClientSideNotifications", "", "isNotificationSystemConfigured", "saveNotificationToDatabase", "app_release"})
public final class NotificationHelper {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "NotificationHelper";
    @org.jetbrains.annotations.NotNull()
    private static final okhttp3.MediaType JSON = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications";
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.NotificationHelper INSTANCE = null;
    
    private NotificationHelper() {
        super();
    }
    
    /**
     * Sends a notification to a user.
     *
     * @param recipientUid The UID of the user to send the notification to.
     * @param senderUid The UID of the user sending the notification.
     * @param message The message to send in the notification.
     * @param notificationType The type of notification to send.
     * @param data A map of additional data to send with the notification.
     */
    @kotlin.jvm.JvmStatic()
    public static final void sendNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String recipientUid, @org.jetbrains.annotations.NotNull()
    java.lang.String senderUid, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    java.lang.String notificationType, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    /**
     * Enhanced notification sending with smart presence checking and dual system support.
     *
     * @param senderUid The UID of the message sender
     * @param recipientUid The UID of the message recipient
     * @param recipientOneSignalPlayerId The OneSignal Player ID of the recipient
     * @param message The message content to send in the notification
     * @param chatId Optional chat ID for deep linking (can be null)
     * @deprecated Use sendNotification instead.
     */
    @kotlin.jvm.JvmStatic()
    @java.lang.Deprecated()
    public static final void sendMessageAndNotifyIfNeeded(@org.jetbrains.annotations.NotNull()
    java.lang.String senderUid, @org.jetbrains.annotations.NotNull()
    java.lang.String recipientUid, @org.jetbrains.annotations.NotNull()
    java.lang.String recipientOneSignalPlayerId, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String chatId) {
    }
    
    /**
     * Sends notification via the existing Cloudflare Worker (server-side).
     */
    @kotlin.jvm.JvmStatic()
    public static final void sendServerSideNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String recipientId, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.NotNull()
    java.lang.String notificationType, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    /**
     * Sends notification directly via OneSignal REST API (client-side).
     */
    @kotlin.jvm.JvmStatic()
    public static final void sendClientSideNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String recipientPlayerId, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String senderUid, @org.jetbrains.annotations.NotNull()
    java.lang.String notificationType, @org.jetbrains.annotations.Nullable()
    java.util.Map<java.lang.String, java.lang.String> data) {
    }
    
    /**
     * Legacy method for backward compatibility.
     * @deprecated Use sendMessageAndNotifyIfNeeded with chatId parameter instead
     */
    @kotlin.jvm.JvmStatic()
    @java.lang.Deprecated()
    public static final void triggerPushNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String recipientId, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    /**
     * Gets the current notification system being used.
     * @return true if using client-side notifications, false if using server-side
     */
    @kotlin.jvm.JvmStatic()
    public static final boolean isUsingClientSideNotifications() {
        return false;
    }
    
    /**
     * Checks if the notification system is properly configured.
     * @return true if configuration is valid, false otherwise
     */
    @kotlin.jvm.JvmStatic()
    public static final boolean isNotificationSystemConfigured() {
        return false;
    }
    
    private final void saveNotificationToDatabase(java.lang.String recipientUid, java.lang.String senderUid, java.lang.String message, java.lang.String notificationType, java.util.Map<java.lang.String, java.lang.String> data) {
    }
}