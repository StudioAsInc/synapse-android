# Firebase Presence System for Synapse Social Media App

## Overview

This document provides comprehensive documentation for the Firebase Presence System implemented for the Synapse social media app. The system provides real-time user presence tracking, automatic online/offline status management, and seamless integration with both web and mobile platforms.

## Features

### ✅ Core Functionality
- **Real-time Connection Monitoring**: Uses Firebase `.info/connected` for accurate connection detection
- **Automatic Status Management**: Automatically sets users online/offline based on connection state
- **Graceful Disconnection Handling**: Uses Firebase `onDisconnect()` for reliable offline status
- **Activity Tracking**: Tracks user activities and last seen timestamps
- **Heartbeat System**: Maintains presence with periodic activity updates
- **Multi-User Monitoring**: Listen to multiple users' status changes simultaneously

### ✅ Safety & Reliability
- **Connection State Validation**: Ensures operations only occur when connected
- **Error Handling**: Comprehensive error handling and logging
- **Memory Management**: Proper cleanup of listeners and intervals
- **Platform Agnostic**: Works with web, Android, and other platforms
- **Configurable**: Flexible configuration for different deployment scenarios

## Architecture

### Database Structure
```
firebase-database/
├── presence/
│   └── {userId}/
│       ├── user_id: string
│       ├── connected_at: timestamp
│       ├── last_activity: timestamp
│       ├── current_activity: string
│       └── platform: string
└── user_status/
    └── {userId}/
        ├── status: "online" | "offline" | "away" | "busy"
        ├── last_changed: timestamp
        ├── last_seen: timestamp
        ├── user_id: string
        ├── display_name: string
        ├── avatar_url: string
        └── platform: string
```

### Key Components

1. **Connection Monitor**: Monitors Firebase connection state
2. **Presence Manager**: Handles user presence data
3. **Status Tracker**: Manages user status updates
4. **Activity Logger**: Tracks user activities
5. **Listener Manager**: Manages real-time listeners
6. **Heartbeat System**: Maintains active presence

## Installation & Setup

### 1. Include Firebase SDK

```html
<!-- Firebase App (the core Firebase SDK) -->
<script src="https://www.gstatic.com/firebasejs/9.x.x/firebase-app.js"></script>

<!-- Firebase Realtime Database -->
<script src="https://www.gstatic.com/firebasejs/9.x.x/firebase-database.js"></script>

<!-- Include the presence system -->
<script src="firebase-presence.js"></script>
```

### 2. Initialize the System

```javascript
// Firebase configuration
const firebaseConfig = {
    databaseURL: 'https://your-project.firebaseio.com',
    presencePath: 'presence',
    userStatusPath: 'user_status',
    connectionTimeout: 30000,    // 30 seconds
    heartbeatInterval: 25000     // 25 seconds
};

// Initialize presence system
const presenceSystem = new FirebasePresenceSystem(firebaseConfig);

// Initialize for current user
const currentUser = {
    id: 'user123',
    displayName: 'John Doe',
    username: 'johndoe',
    avatarUrl: 'https://example.com/avatar.jpg',
    platform: 'web' // or 'android', 'ios'
};

presenceSystem.initializeUserPresence(currentUser.id, currentUser);
```

## API Reference

### Core Methods

#### `initializeUserPresence(userId, userData)`
Initializes presence tracking for a user.

```javascript
presenceSystem.initializeUserPresence('user123', {
    displayName: 'John Doe',
    username: 'johndoe',
    avatarUrl: 'https://example.com/avatar.jpg',
    platform: 'web'
});
```

#### `updateActivity(activity)`
Updates the user's current activity.

```javascript
presenceSystem.updateActivity('typing');
presenceSystem.updateActivity('viewing_profile');
presenceSystem.updateActivity('in_chat');
```

#### `setUserStatus(status)`
Manually sets user status.

```javascript
presenceSystem.setUserStatus('online');
presenceSystem.setUserStatus('away');
presenceSystem.setUserStatus('busy');
presenceSystem.setUserStatus('offline');
```

### Monitoring Methods

#### `listenToUserStatus(userId, callback)`
Listens to a specific user's status changes.

```javascript
const removeListener = presenceSystem.listenToUserStatus('user456', (status, userData) => {
    console.log(`User 456 is now ${status}`);
    if (userData) {
        console.log('User data:', userData);
    }
});

// Remove listener when done
removeListener();
```

#### `listenToMultipleUsers(userIds, callback)`
Listens to multiple users' status changes.

```javascript
const userIds = ['user123', 'user456', 'user789'];
const removeListeners = presenceSystem.listenToMultipleUsers(userIds, (userId, status, userData) => {
    console.log(`User ${userId} is now ${status}`);
});

// Remove all listeners when done
removeListeners();
```

#### `getOnlineUsers(callback)`
Gets all currently online users.

```javascript
presenceSystem.getOnlineUsers((onlineUsers) => {
    console.log('Online users:', onlineUsers);
    onlineUsers.forEach(user => {
        console.log(`${user.display_name} is online`);
    });
});
```

### Connection Management

#### `onConnectionChange(callback)`
Listens to connection state changes.

```javascript
presenceSystem.onConnectionChange((isConnected) => {
    if (isConnected) {
        console.log('Connected to Firebase');
    } else {
        console.log('Disconnected from Firebase');
    }
});
```

#### `isUserConnected()`
Checks if the user is currently connected.

```javascript
if (presenceSystem.isUserConnected()) {
    console.log('User is connected');
} else {
    console.log('User is disconnected');
}
```

### Utility Methods

#### `getCurrentUser()`
Gets current user data.

```javascript
const currentUser = presenceSystem.getCurrentUser();
console.log('Current user:', currentUser);
```

#### `cleanup()`
Cleans up all listeners and references.

```javascript
// Call this when the user logs out or the app is closing
presenceSystem.cleanup();
```

## Integration Examples

### Web Application Integration

```javascript
class SynapseWebApp {
    constructor() {
        this.presenceSystem = null;
        this.currentUser = null;
    }

    async initialize(userData) {
        // Initialize Firebase presence system
        this.presenceSystem = new FirebasePresenceSystem({
            databaseURL: 'https://synapse-app.firebaseio.com',
            presencePath: 'presence',
            userStatusPath: 'user_status'
        });

        this.currentUser = userData;
        this.presenceSystem.initializeUserPresence(userData.id, userData);

        // Set up connection monitoring
        this.presenceSystem.onConnectionChange((isConnected) => {
            this.updateConnectionUI(isConnected);
        });

        // Set up activity tracking
        this.setupActivityTracking();
    }

    setupActivityTracking() {
        // Track typing activity
        const messageInput = document.getElementById('message-input');
        let typingTimeout;

        messageInput.addEventListener('input', () => {
            this.presenceSystem.updateActivity('typing');
            
            clearTimeout(typingTimeout);
            typingTimeout = setTimeout(() => {
                this.presenceSystem.updateActivity('idle');
            }, 3000);
        });

        // Track page visibility
        document.addEventListener('visibilitychange', () => {
            if (document.hidden) {
                this.presenceSystem.setUserStatus('away');
            } else {
                this.presenceSystem.setUserStatus('online');
            }
        });
    }

    updateConnectionUI(isConnected) {
        const statusIndicator = document.getElementById('connection-status');
        if (isConnected) {
            statusIndicator.textContent = '🟢 Connected';
            statusIndicator.className = 'status-connected';
        } else {
            statusIndicator.textContent = '🔴 Disconnected';
            statusIndicator.className = 'status-disconnected';
        }
    }

    listenToFriendStatus(friendId) {
        return this.presenceSystem.listenToUserStatus(friendId, (status, userData) => {
            this.updateFriendStatusUI(friendId, status, userData);
        });
    }

    updateFriendStatusUI(friendId, status, userData) {
        const friendElement = document.querySelector(`[data-user-id="${friendId}"]`);
        if (friendElement) {
            const statusDot = friendElement.querySelector('.status-dot');
            const statusText = friendElement.querySelector('.status-text');
            
            switch (status) {
                case 'online':
                    statusDot.className = 'status-dot online';
                    statusText.textContent = 'Online';
                    break;
                case 'away':
                    statusDot.className = 'status-dot away';
                    statusText.textContent = 'Away';
                    break;
                case 'busy':
                    statusDot.className = 'status-dot busy';
                    statusText.textContent = 'Busy';
                    break;
                default:
                    statusDot.className = 'status-dot offline';
                    statusText.textContent = 'Offline';
            }
        }
    }

    cleanup() {
        if (this.presenceSystem) {
            this.presenceSystem.cleanup();
        }
    }
}
```

### Android Integration

```java
public class SynapsePresenceManager {
    private FirebasePresenceSystem presenceSystem;
    private String currentUserId;
    private Handler activityHandler;
    private Runnable activityRunnable;

    public void initializePresence(String userId, UserData userData) {
        this.currentUserId = userId;
        
        // Initialize presence system via WebView or native bridge
        presenceSystem = new FirebasePresenceSystem(config);
        presenceSystem.initializeUserPresence(userId, userData);
        
        // Set up activity tracking
        setupActivityTracking();
    }

    private void setupActivityTracking() {
        activityHandler = new Handler();
        activityRunnable = new Runnable() {
            @Override
            public void run() {
                presenceSystem.updateActivity("idle");
                activityHandler.postDelayed(this, 30000); // 30 seconds
            }
        };
        
        // Start activity tracking
        activityHandler.post(activityRunnable);
    }

    public void updateActivity(String activity) {
        if (presenceSystem != null) {
            presenceSystem.updateActivity(activity);
        }
    }

    public void setUserStatus(String status) {
        if (presenceSystem != null) {
            presenceSystem.setUserStatus(status);
        }
    }

    public void cleanup() {
        if (activityHandler != null && activityRunnable != null) {
            activityHandler.removeCallbacks(activityRunnable);
        }
        
        if (presenceSystem != null) {
            presenceSystem.cleanup();
        }
    }
}
```

## Security Considerations

### Firebase Security Rules

```json
{
  "rules": {
    "presence": {
      "$userId": {
        ".read": "auth != null",
        ".write": "auth != null && auth.uid == $userId",
        ".validate": "newData.hasChildren(['user_id', 'connected_at', 'last_activity'])"
      }
    },
    "user_status": {
      "$userId": {
        ".read": "auth != null",
        ".write": "auth != null && auth.uid == $userId",
        ".validate": "newData.hasChildren(['status', 'last_changed', 'user_id'])"
      }
    }
  }
}
```

### Best Practices

1. **Authentication Required**: Always require Firebase Authentication
2. **User-Specific Access**: Users can only write to their own presence data
3. **Data Validation**: Validate presence data structure
4. **Rate Limiting**: Implement rate limiting for presence updates
5. **Privacy Controls**: Allow users to control their visibility

## Performance Optimization

### Database Optimization

1. **Indexed Queries**: Use indexed queries for online users
2. **Data Structure**: Keep presence data minimal and efficient
3. **Cleanup**: Regularly clean up old presence data
4. **Caching**: Cache frequently accessed user status

### Network Optimization

1. **Heartbeat Frequency**: Adjust heartbeat interval based on network conditions
2. **Batch Updates**: Batch multiple presence updates
3. **Connection Pooling**: Reuse Firebase connections
4. **Offline Support**: Handle offline scenarios gracefully

## Troubleshooting

### Common Issues

1. **Connection Not Detected**
   - Check Firebase configuration
   - Verify network connectivity
   - Check Firebase Security Rules

2. **Status Not Updating**
   - Verify user authentication
   - Check database permissions
   - Validate data structure

3. **Memory Leaks**
   - Ensure proper cleanup of listeners
   - Remove event listeners on component unmount
   - Clear intervals and timeouts

### Debug Mode

```javascript
// Enable debug logging
const presenceSystem = new FirebasePresenceSystem({
    ...config,
    debug: true
});

// Monitor all presence events
presenceSystem.onConnectionChange((connected) => {
    console.log('Connection changed:', connected);
});

presenceSystem.listenToUserStatus('testUser', (status, data) => {
    console.log('Status update:', status, data);
});
```

## Testing

### Unit Tests

```javascript
describe('FirebasePresenceSystem', () => {
    let presenceSystem;
    
    beforeEach(() => {
        presenceSystem = new FirebasePresenceSystem(mockConfig);
    });
    
    afterEach(() => {
        presenceSystem.cleanup();
    });
    
    test('should initialize user presence', () => {
        const userData = { id: 'test123', displayName: 'Test User' };
        presenceSystem.initializeUserPresence('test123', userData);
        expect(presenceSystem.getCurrentUser()).toEqual(userData);
    });
    
    test('should handle connection changes', () => {
        const mockCallback = jest.fn();
        presenceSystem.onConnectionChange(mockCallback);
        
        // Simulate connection change
        presenceSystem.isConnected = true;
        presenceSystem.onUserConnected();
        
        expect(mockCallback).toHaveBeenCalledWith(true);
    });
});
```

### Integration Tests

```javascript
describe('Presence Integration', () => {
    test('should track user activity', async () => {
        const presenceSystem = new FirebasePresenceSystem(config);
        await presenceSystem.initializeUserPresence('user123', userData);
        
        presenceSystem.updateActivity('typing');
        
        // Verify activity was recorded
        const presenceData = await getPresenceData('user123');
        expect(presenceData.current_activity).toBe('typing');
    });
});
```

## Deployment

### Production Configuration

```javascript
const productionConfig = {
    databaseURL: 'https://synapse-app-prod.firebaseio.com',
    presencePath: 'presence',
    userStatusPath: 'user_status',
    connectionTimeout: 30000,
    heartbeatInterval: 25000,
    debug: false,
    maxRetries: 3,
    retryDelay: 1000
};
```

### Environment Variables

```bash
# Firebase Configuration
FIREBASE_DATABASE_URL=https://synapse-app.firebaseio.com
FIREBASE_PROJECT_ID=synapse-app
FIREBASE_API_KEY=your-api-key

# Presence Configuration
PRESENCE_HEARTBEAT_INTERVAL=25000
PRESENCE_CONNECTION_TIMEOUT=30000
PRESENCE_DEBUG_MODE=false
```

## Contributing

### Development Setup

1. Clone the repository
2. Install dependencies: `npm install`
3. Set up Firebase project
4. Configure environment variables
5. Run tests: `npm test`
6. Start development server: `npm run dev`

### Code Style

- Follow ESLint configuration
- Use JSDoc for documentation
- Write unit tests for new features
- Follow conventional commit messages

### Pull Request Process

1. Fork the repository
2. Create feature branch
3. Make changes with tests
4. Update documentation
5. Submit pull request
6. Code review and merge

## License

This project is licensed under the Custom License - see the [LICENSE.md](LICENSE.md) file for details.

## Support

For support and questions:

- **GitHub Issues**: [Create an issue](https://github.com/StudioAsInc/synapse-android/issues)
- **Documentation**: [Wiki](https://github.com/StudioAsInc/synapse-android/wiki)
- **Discussions**: [GitHub Discussions](https://github.com/StudioAsInc/synapse-android/discussions)
- **Email**: mashikahamed0@gmail.com

---

**A Project by [StudioAs Inc.](https://studioas.dev)**  
*"Empowering connections through transparency"*