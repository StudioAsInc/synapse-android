# Enhanced Notification System Guide

## Overview

The Synapse Social app now supports a dual notification system that can send push notifications through either:
1. **Server-side (Cloudflare Workers)** - The existing system
2. **Client-side (OneSignal REST API)** - New direct integration

This system includes smart notification suppression to prevent notifications when both users are actively chatting.

## Features

### 🎯 Smart Notification Suppression
- **No notifications when both users are chatting**: If the recipient is actively chatting with the sender, no notification is sent
- **No notifications for online users**: If the recipient is online, they'll see messages in real-time, so no notification is needed
- **Notifications only for offline users**: Notifications are only sent when the recipient is offline or has an unknown status

### 🔄 Dual System Support
- **Server-side notifications**: Uses existing Cloudflare Worker
- **Client-side notifications**: Direct OneSignal REST API integration
- **Easy switching**: Single configuration flag to switch between systems
- **Fallback mechanisms**: Automatic fallback if primary system fails

### ⚙️ Configurable Settings
- **Feature flags**: Enable/disable smart suppression, fallback mechanisms, deep linking
- **Debug logging**: Detailed logging for troubleshooting
- **Customizable notification content**: Title, subtitle, channel ID, priority

## Configuration

### 1. Notification System Toggle

Edit `NotificationConfig.kt` to switch between notification systems:

```kotlin
// Set to true for client-side OneSignal notifications
// Set to false for server-side Cloudflare Worker notifications
const val USE_CLIENT_SIDE_NOTIFICATIONS = false
```

### 2. OneSignal Configuration (Client-side)

If using client-side notifications, replace the placeholder values in `NotificationConfig.kt`:

```kotlin
// Replace with your actual OneSignal credentials
const val ONESIGNAL_APP_ID = "YOUR_ONESIGNAL_APP_ID_HERE"
const val ONESIGNAL_REST_API_KEY = "YOUR_ONESIGNAL_REST_API_KEY_HERE"
```

#### How to get OneSignal credentials:

1. **App ID**: Found in your OneSignal dashboard under Settings > Keys & IDs
2. **REST API Key**: Found in your OneSignal dashboard under Settings > Keys & IDs

### 3. Feature Flags

Configure additional features in `NotificationConfig.kt`:

```kotlin
// Enable/disable smart notification suppression
const val ENABLE_SMART_SUPPRESSION = true

// Enable/disable fallback mechanisms
const val ENABLE_FALLBACK_MECHANISMS = true

// Enable/disable deep linking in notifications
const val ENABLE_DEEP_LINKING = true

// Enable detailed logging for notification system
const val ENABLE_DEBUG_LOGGING = true
```

## Usage

### Basic Usage

The notification system is automatically used when sending messages in `ChatActivity`. The system:

1. Checks the recipient's presence status
2. Determines if a notification should be sent
3. Sends notification via the configured system
4. Falls back to the other system if the primary fails

### Manual Notification Sending

```kotlin
// Send notification with smart presence checking
NotificationHelper.sendMessageAndNotifyIfNeeded(
    senderUid = "user123",
    recipientUid = "user456", 
    recipientOneSignalPlayerId = "onesignal_player_id",
    message = "Hello!",
    chatId = "user123_user456" // Optional for deep linking
)
```

### Checking System Status

```kotlin
// Check which notification system is active
val isClientSide = NotificationHelper.isUsingClientSideNotifications()

// Check if configuration is valid
val isConfigured = NotificationHelper.isNotificationSystemConfigured()

// Get configuration status for debugging
val status = NotificationConfig.getConfigurationStatus()
```

## Presence System Integration

The notification system integrates with the existing presence system:

### Presence Statuses
- `"online"` - User is online and active
- `"chatting_with_<uid>"` - User is actively chatting with another user
- `"<timestamp>"` - User is offline (last seen timestamp)

### Smart Suppression Logic
1. **Chatting suppression**: If recipient status is `"chatting_with_<sender_uid>"`, no notification
2. **Online suppression**: If recipient status is `"online"`, no notification
3. **Notification sent**: For all other statuses (offline, null, etc.)

## Deep Linking

When `ENABLE_DEEP_LINKING` is enabled, notifications include data for deep linking:

```json
{
  "data": {
    "sender_uid": "user123",
    "chat_id": "user123_user456", 
    "type": "chat_message"
  }
}
```

This allows the app to open directly to the chat when the notification is tapped.

## Fallback Mechanisms

The system includes automatic fallback to ensure notifications are delivered:

### Server-side → Client-side Fallback
- Triggered when Cloudflare Worker is unavailable
- Triggered when server returns an error response

### Client-side → Server-side Fallback  
- Triggered when OneSignal API is unavailable
- Triggered when OneSignal returns an error response

## Debugging

### Enable Debug Logging

Set `ENABLE_DEBUG_LOGGING = true` in `NotificationConfig.kt` to see detailed logs:

```
NotificationHelper: Recipient is actively chatting with sender. Suppressing notification.
NotificationHelper: Recipient is online. Suppressing notification for real-time message visibility.
NotificationHelper: Recipient not in chat and not online. Sending notification.
NotificationHelper: Server-side notification sent successfully.
```

### Configuration Validation

```kotlin
// Check if OneSignal credentials are properly configured
if (!NotificationHelper.isNotificationSystemConfigured()) {
    Log.w("App", "Notification system not properly configured!")
}
```

### Configuration Status

```kotlin
val status = NotificationConfig.getConfigurationStatus()
Log.d("App", "Notification config: $status")
```

## Security Considerations

### OneSignal REST API Key
- **Never commit the actual API key to version control**
- **Use environment variables or secure storage in production**
- **Consider using Firebase Remote Config for dynamic configuration**

### Server-side Security
- **The Cloudflare Worker should validate requests**
- **Implement rate limiting to prevent abuse**
- **Use proper authentication for the worker endpoint**

## Migration Guide

### From Server-side to Client-side

1. **Update configuration**:
   ```kotlin
   const val USE_CLIENT_SIDE_NOTIFICATIONS = true
   ```

2. **Add OneSignal credentials**:
   ```kotlin
   const val ONESIGNAL_APP_ID = "your_app_id"
   const val ONESIGNAL_REST_API_KEY = "your_rest_api_key"
   ```

3. **Test the system**:
   - Send a message to an offline user
   - Verify notification is received
   - Check logs for any errors

### From Client-side to Server-side

1. **Update configuration**:
   ```kotlin
   const val USE_CLIENT_SIDE_NOTIFICATIONS = false
   ```

2. **Verify server endpoint**:
   - Ensure Cloudflare Worker is running
   - Test the worker endpoint directly

## Troubleshooting

### Common Issues

#### "Notification system not properly configured"
- **Cause**: OneSignal credentials are placeholder values
- **Solution**: Replace placeholder values with actual OneSignal credentials

#### "Failed to send client-side notification"
- **Cause**: Invalid OneSignal credentials or network issues
- **Solution**: Verify credentials and check network connectivity

#### "Failed to send server-side notification"  
- **Cause**: Cloudflare Worker is down or returning errors
- **Solution**: Check worker status and logs

#### Notifications not being suppressed
- **Cause**: Presence system not working or smart suppression disabled
- **Solution**: Check presence system integration and enable smart suppression

### Testing

1. **Test with offline user**:
   - Set recipient status to offline timestamp
   - Send message
   - Verify notification is received

2. **Test with online user**:
   - Set recipient status to "online"
   - Send message  
   - Verify no notification is sent

3. **Test with chatting user**:
   - Set recipient status to "chatting_with_<sender_uid>"
   - Send message
   - Verify no notification is sent

## API Reference

### NotificationHelper

#### `sendMessageAndNotifyIfNeeded()`
Sends notification with smart presence checking.

**Parameters:**
- `senderUid`: String - The UID of the message sender
- `recipientUid`: String - The UID of the message recipient  
- `recipientOneSignalPlayerId`: String - The OneSignal Player ID of the recipient
- `message`: String - The message content to send in the notification
- `chatId`: String? - Optional chat ID for deep linking

#### `isUsingClientSideNotifications()`
Returns true if using client-side notifications.

#### `isNotificationSystemConfigured()`
Returns true if the notification system is properly configured.

### NotificationConfig

#### `USE_CLIENT_SIDE_NOTIFICATIONS`
Boolean flag to switch between notification systems.

#### `ONESIGNAL_APP_ID`
OneSignal App ID for client-side notifications.

#### `ONESIGNAL_REST_API_KEY`
OneSignal REST API Key for client-side notifications.

#### `ENABLE_SMART_SUPPRESSION`
Boolean flag to enable/disable smart notification suppression.

#### `ENABLE_FALLBACK_MECHANISMS`
Boolean flag to enable/disable fallback mechanisms.

#### `ENABLE_DEEP_LINKING`
Boolean flag to enable/disable deep linking in notifications.

#### `ENABLE_DEBUG_LOGGING`
Boolean flag to enable/disable detailed logging.

#### `getConfigurationStatus()`
Returns a map containing the current configuration status.

#### `isConfigurationValid()`
Returns true if the current configuration is valid.

## Support

For issues or questions about the notification system:

1. Check the debug logs with `ENABLE_DEBUG_LOGGING = true`
2. Verify configuration with `NotificationConfig.getConfigurationStatus()`
3. Test with different user presence states
4. Check OneSignal dashboard for delivery status (client-side)
5. Check Cloudflare Worker logs (server-side)