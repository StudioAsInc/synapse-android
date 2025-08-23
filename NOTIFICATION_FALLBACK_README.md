# Notification Fallback System

## Overview

This implementation provides a robust notification system with automatic fallback capabilities. It allows you to switch between server-side and client-side notifications by changing a single configuration line, following DRY principles.

## Key Features

- **Configurable Notification Method**: Switch between server and client notifications with one line change
- **Automatic Fallback**: Server failures automatically trigger client-side notifications
- **Retry Logic**: Configurable retry attempts for server notifications
- **Smart Suppression**: Prevents notifications when recipients are already in chat
- **Detailed Logging**: Comprehensive logging for debugging and monitoring
- **DRY Implementation**: Reusable methods with clear separation of concerns

## Configuration

### Quick Switch

To change notification methods, edit `NotificationConfig.java`:

```java
// For server-side notifications (default)
public static final boolean USE_SERVER_NOTIFICATIONS = true;

// For client-side notifications
public static final boolean USE_SERVER_NOTIFICATIONS = false;
```

### Advanced Configuration

```java
public class NotificationConfig {
    // Primary method selection
    public static final boolean USE_SERVER_NOTIFICATIONS = true;
    
    // Fallback behavior
    public static final boolean ENABLE_AUTOMATIC_FALLBACK = true;
    
    // Debugging
    public static final boolean ENABLE_DEBUG_LOGGING = true;
    
    // Retry settings
    public static final int SERVER_REQUEST_TIMEOUT_MS = 10000;
    public static final int MAX_SERVER_RETRY_ATTEMPTS = 3;
    public static final int RETRY_DELAY_MS = 2000;
}
```

## Architecture

### Server-Side Notifications (Primary)

1. **NotificationHelper.kt**: Handles Firebase status checks and OneSignal integration
2. **Cloudflare Worker**: Processes notifications and forwards to OneSignal
3. **Retry Logic**: Automatic retries with configurable attempts and delays
4. **Fallback**: Automatic switch to client-side if server fails

### Client-Side Notifications (Fallback)

1. **Firebase Presence**: Checks recipient status using Firebase Realtime Database
2. **Smart Suppression**: Prevents notifications when recipients are in chat
3. **Local Notifications**: Placeholder implementations for various notification types
4. **Extensible**: Easy to add custom notification methods

## Usage Examples

### Basic Usage

The system automatically handles notification method selection:

```java
// This automatically uses server or client based on configuration
_sendNotification(senderUid, recipientUid, recipientOneSignalPlayerId, 
                 message, messageType, attachments, senderName);
```

### Custom Notification Methods

Uncomment and customize the example methods in `ChatActivity.java`:

```java
// Local Android notifications
private void _createAndroidNotification(String message, String messageType, 
                                      List<String> attachments, String senderName) {
    // Use NotificationManager and NotificationCompat.Builder
}

// In-app notifications
private void _showInAppNotification(String message, String messageType, 
                                   List<String> attachments, String senderName) {
    // Show banners, update notification center, etc.
}

// Alternative push services
private void _sendAlternativePushNotification(String message, String messageType, 
                                            List<String> attachments, String senderName) {
    // Use Firebase Cloud Messaging, OneSignal direct API, etc.
}
```

## Implementation Details

### Message Sending Flow

1. **Message Creation**: Text or attachment messages are prepared
2. **Notification Decision**: Based on `USE_SERVER_NOTIFICATIONS` flag
3. **Server Path**: NotificationHelper → Cloudflare Worker → OneSignal
4. **Client Path**: Firebase status check → Local notification creation
5. **Fallback**: Automatic switch if server fails

### Error Handling

- **Server Failures**: Automatic retries with exponential backoff
- **Network Issues**: Graceful degradation to client-side
- **Status Check Failures**: Conservative approach (send notification anyway)
- **Upload Failures**: Partial message sending with available attachments

### Performance Optimizations

- **Async Operations**: Non-blocking notification delivery
- **Status Caching**: Firebase presence checks for suppression
- **Batch Processing**: Efficient handling of multiple attachments
- **Memory Management**: Proper cleanup of resources and handlers

## Monitoring and Debugging

### Logging

Enable debug logging in `NotificationConfig.java`:

```java
public static final boolean ENABLE_DEBUG_LOGGING = true;
```

### Log Categories

- **Server Notifications**: Attempts, successes, failures, retries
- **Client Notifications**: Status checks, suppression, delivery
- **Fallback Logic**: Automatic switches and error conditions
- **Performance**: Timing and resource usage

### Common Issues

1. **Server Down**: Automatically falls back to client-side
2. **Network Issues**: Retry logic handles temporary failures
3. **Firebase Errors**: Graceful degradation with logging
4. **Configuration Errors**: Clear error messages and fallbacks

## Migration Guide

### From Server-Only to Hybrid

1. Set `USE_SERVER_NOTIFICATIONS = true`
2. Set `ENABLE_AUTOMATIC_FALLBACK = true`
3. Test with server failures to verify fallback

### From Hybrid to Client-Only

1. Set `USE_SERVER_NOTIFICATIONS = false`
2. Customize `_createLocalNotification` method
3. Test client-side notification delivery

### Custom Notification Services

1. Implement custom notification method
2. Update `_createLocalNotification` to call your method
3. Configure service-specific settings

## Best Practices

1. **Always Enable Fallback**: Prevents notification loss during server issues
2. **Monitor Logs**: Use debug logging to track notification delivery
3. **Test Failures**: Verify fallback behavior with simulated server failures
4. **Customize Client Notifications**: Implement meaningful local notifications
5. **Handle Edge Cases**: Consider offline scenarios and network changes

## Troubleshooting

### Notifications Not Sending

1. Check `USE_SERVER_NOTIFICATIONS` setting
2. Verify Firebase connectivity
3. Check OneSignal configuration
4. Review debug logs for errors

### Fallback Not Working

1. Ensure `ENABLE_AUTOMATIC_FALLBACK = true`
2. Check retry configuration
3. Verify client notification implementation
4. Review error logs

### Performance Issues

1. Adjust retry delays and attempts
2. Optimize Firebase queries
3. Implement notification batching
4. Monitor memory usage

## Future Enhancements

- **Notification Queuing**: Persistent storage for failed notifications
- **Smart Retry**: Adaptive retry intervals based on failure patterns
- **Analytics**: Track notification delivery success rates
- **Multi-Service**: Support for multiple notification providers
- **Offline Support**: Queue notifications when device is offline

## Support

For issues or questions:
1. Check debug logs for detailed error information
2. Verify configuration settings
3. Test with different notification methods
4. Review Firebase and OneSignal status