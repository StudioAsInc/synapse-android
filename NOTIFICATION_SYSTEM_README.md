# Notification System Documentation

## Overview

This project includes a comprehensive notification system built with OneSignal integration, featuring a beautiful Material Design settings UI and robust notification management capabilities.

## Features

### 🎯 Core Notification System
- **OneSignal Integration**: Push notifications via OneSignal REST API
- **Local Notifications**: Android system notifications with custom channels
- **Smart Filtering**: User preference-based notification delivery
- **Quiet Hours**: Configurable time-based notification silencing

### 🎨 Beautiful Settings UI
- **Material Design 3**: Modern, accessible interface
- **Preference Management**: Comprehensive notification preferences
- **Real-time Status**: Live notification system status
- **Test Interface**: Built-in notification testing tools

### 🔧 Technical Features
- **Notification Channels**: Android 8.0+ compatible
- **Preference Persistence**: SharedPreferences-based settings
- **Service Architecture**: Clean separation of concerns
- **Error Handling**: Robust error handling and logging

## Architecture

### Components

#### 1. NotificationConfig
```kotlin
object NotificationConfig {
    const val ONESIGNAL_APP_ID = "044e1911-6911-4871-95f9-d60003002fe2"
    const val ONESIGNAL_REST_API_KEY = "os_v2_app_arhbseljcfehdfpz2yaagabp4j7fcvm6rqluzmeowmqsz7jztqdpdrc2jvmuijfzurukbzkhanucy2woctycxs4bv563rlpizdn5whq"
    const val ONESIGNAL_REST_API_URL = "https://onesignal.com/api/v1/notifications"
}
```

#### 2. NotificationManagerService
Handles all notification operations:
- Local notification display
- OneSignal API integration
- Notification channel management
- Notification lifecycle

#### 3. NotificationPreferencesManager
Manages user preferences:
- Notification type toggles
- Sound/vibration settings
- Quiet hours configuration
- Preference validation

#### 4. Settings UI
- `SettingsActivity`: Main settings navigation
- `NotificationSettingsActivity`: Notification-specific settings
- `NotificationTestActivity`: Testing interface

## Usage

### Basic Setup

1. **Initialize Notification Manager**:
```kotlin
val notificationManager = NotificationManagerService(context)
```

2. **Check Preferences**:
```kotlin
val preferencesManager = NotificationPreferencesManager(context)
if (preferencesManager.shouldShowNotification(NotificationType.CHAT_MESSAGE)) {
    // Show notification
}
```

3. **Send Notifications**:
```kotlin
// Local notification
notificationManager.showChatNotification("John Doe", "Hello!", "chat_123")

// OneSignal push notification
notificationManager.sendOneSignalNotification(
    "Title", "Message", 
    listOf("player_id"), 
    mapOf("type" to "chat")
)
```

### Notification Types

#### Chat Messages
```kotlin
notificationManager.showChatNotification(
    senderName = "John Doe",
    message = "Hey! How are you?",
    chatId = "chat_123"
)
```

#### Group Updates
```kotlin
notificationManager.showGroupNotification(
    groupName = "Tech Enthusiasts",
    updateType = "New member joined"
)
```

#### Friend Requests
```kotlin
notificationManager.showFriendRequestNotification(
    senderName = "Jane Smith"
)
```

#### Mentions
```kotlin
notificationManager.showMentionNotification(
    senderName = "Alex Johnson",
    content = "Hey @you, check this out!"
)
```

### OneSignal Integration

#### Send Push Notification
```kotlin
notificationManager.sendOneSignalNotification(
    title = "New Message",
    message = "You have a new message",
    playerIds = listOf("user_player_id"),
    data = mapOf(
        "type" to "chat",
        "chat_id" to "123",
        "action" to "open_chat"
    )
)
```

#### API Configuration
The system automatically uses the configured OneSignal credentials:
- App ID: `044e1911-6911-4871-95f9-d60003002fe2`
- REST API Key: `os_v2_app_arhbseljcfehdfpz2yaagabp4j7fcvm6rqluzmeowmqsz7jztqdpdrc2jvmuijfzurukbzkhanucy2woctycxs4bv563rlpizdn5whq`

## Settings Configuration

### Notification Preferences

Users can configure:
- **Push Notifications**: Master toggle for all notifications
- **Chat Messages**: Incoming chat notifications
- **Group Updates**: Group activity notifications
- **Friend Requests**: New friend request notifications
- **Mentions**: @mention notifications
- **Sound**: Notification audio
- **Vibration**: Haptic feedback
- **LED**: Notification light

### Quiet Hours

Configure time periods when notifications are silenced:
- **Start Time**: When quiet hours begin (24-hour format)
- **End Time**: When quiet hours end (24-hour format)
- **Smart Detection**: Automatic quiet hours detection

## UI Components

### Material Design Cards
- Rounded corners (12dp radius)
- Subtle shadows (4dp elevation)
- Clean white backgrounds
- Proper spacing and padding

### Buttons
- **Primary**: Filled buttons for main actions
- **Outlined**: Secondary actions
- **Text**: Minimal text buttons
- Consistent sizing (48dp height)

### Color Scheme
- **Primary**: Material Blue (#2196F3)
- **Accent**: Material Pink (#FF4081)
- **Success**: Material Green (#4CAF50)
- **Warning**: Material Orange (#FF9800)
- **Error**: Material Red (#F44336)

## Testing

### Test Interface
The `NotificationTestActivity` provides:
- Individual notification type testing
- OneSignal API testing
- Real-time status display
- Preference validation

### Test Notifications
1. Navigate to Settings → Notifications → Test
2. Use test buttons for each notification type
3. Verify preferences are respected
4. Check OneSignal API integration

## Error Handling

### Common Issues

#### OneSignal API Errors
- Check API key configuration
- Verify network connectivity
- Validate player IDs
- Monitor API response codes

#### Local Notification Issues
- Ensure notification channels exist
- Check Android version compatibility
- Verify permission settings
- Monitor notification delivery

### Debugging
```kotlin
// Enable debug logging
android.util.Log.d("NotificationManager", "Debug message")

// Check configuration status
if (NotificationConfig.isApiKeyConfigured()) {
    // API is properly configured
}
```

## Security Considerations

### API Key Management
- Store API keys securely
- Use BuildConfig for production
- Implement key rotation
- Monitor API usage

### User Privacy
- Respect user preferences
- Implement quiet hours
- Provide granular control
- Log minimal data

## Performance Optimization

### Notification Batching
- Group similar notifications
- Implement rate limiting
- Use notification channels
- Optimize payload size

### Memory Management
- Reuse notification builders
- Clear old notifications
- Monitor memory usage
- Implement cleanup routines

## Future Enhancements

### Planned Features
- **Rich Notifications**: Media attachments
- **Action Buttons**: Quick response options
- **Notification History**: Persistent storage
- **Advanced Scheduling**: Custom timing rules
- **Analytics**: Notification performance metrics

### Integration Opportunities
- **Firebase Cloud Messaging**: Alternative push service
- **WebSocket**: Real-time updates
- **Machine Learning**: Smart notification timing
- **A/B Testing**: Notification optimization

## Support

### Documentation
- This README
- Code comments
- Inline documentation
- API references

### Troubleshooting
1. Check notification permissions
2. Verify OneSignal configuration
3. Test with sample data
4. Review system logs
5. Validate user preferences

### Contact
For technical support or questions about the notification system, please refer to the project documentation or create an issue in the project repository.

---

**Note**: This notification system is designed to be robust, user-friendly, and maintainable. It follows Android best practices and Material Design guidelines for optimal user experience.