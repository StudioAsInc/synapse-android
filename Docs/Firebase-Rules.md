### Real-time Database Rules v1.0
```
{
  "rules": {
    // Keep most of the database public by default
    ".read": true,
    ".write": true,
    
    "skyline": {
      "chats": {
        "$user1": {
          "$user2": {
            // Only the two chat participants can read/write their chat data
            ".read": "auth != null && (auth.uid === $user1 || auth.uid === $user2)",
            ".write": "auth != null && (auth.uid === $user1 || auth.uid === $user2)"
          }
        }
      },
      
      "inbox": {
        "$user1": {
          "$user2": {
            // Only the two users can access their inbox entries
            ".read": "auth != null && (auth.uid === $user1 || auth.uid === $user2)",
            ".write": "auth != null && (auth.uid === $user1 || auth.uid === $user2)"
          }
        }
      }
    },
    
    "synapse": {
      "chats": {
        "$user1": {
          "$user2": {
            // Only the two chat participants can read/write their chat data
            ".read": "auth != null && (auth.uid === $user1 || auth.uid === $user2)",
            ".write": "auth != null && (auth.uid === $user1 || auth.uid === $user2)"
          }
        }
      }
    }
  }
}
```