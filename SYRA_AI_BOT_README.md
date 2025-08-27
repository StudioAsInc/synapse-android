# Syra AI Bot Integration

This document describes the implementation of the Syra AI Bot for the Synapse social media app. The bot provides intelligent, automated interactions using Google's Gemini AI.

## Features

### 🤖 Core Functionality

1. **Mention Response System**
   - Automatically responds when users mention `@syra` in chats
   - Joins conversations naturally and provides helpful responses
   - Responds to mentions in posts and comments

2. **Automatic Chat Participation**
   - Monitors all chat messages for `@syra` mentions
   - Provides contextual AI-powered responses
   - Maintains conversation history and context

3. **Random Post Commenting**
   - Automatically comments on random posts (30% probability)
   - Generates thoughtful, relevant comments using AI
   - Avoids commenting on own posts

4. **Automatic Posting**
   - Posts 1-3 times per day automatically
   - Generates engaging content about technology, AI, and social connections
   - Random timing between 8-16 hours to appear natural

5. **Human-like Behavior**
   - Configurable response delays and intervals
   - Natural conversation patterns
   - Appropriate emoji usage

## Architecture

### 📁 File Structure

```
app/
├── src/main/java/com/synapse/social/studioasinc/AI/
│   ├── SyraAIBotService.java      # Main service handling all bot functionality
│   ├── SyraAIBotManager.java      # Manager class for easy integration
│   ├── SyraAccountSetup.java      # Handles bot account creation and setup
│   ├── SyraAIConfig.java          # Configuration constants and utilities
│   ├── VercelAIService.java       # Vercel backend API integration
│   └── Gemini.java                # Local AI API wrapper (legacy)
└── backend/vercel-backend/        # Vercel serverless backend
    ├── api/ai/                    # AI processing endpoints
    ├── api/webhooks/              # Firebase webhooks
    ├── public/                    # API testing interface
    └── package.json               # Backend dependencies
```

### 🔧 Components

#### SyraAIBotService
- **Background Service**: Runs continuously to monitor chats and posts
- **Event Listeners**: Monitors Firebase for new messages and posts
- **AI Integration**: Uses Gemini AI for generating responses
- **Scheduling**: Handles automatic posting and commenting timers

#### SyraAIBotManager
- **Service Management**: Start/stop the bot service
- **Utility Methods**: Helper functions for mention detection
- **Integration Points**: Easy access for existing activities

#### SyraAccountSetup
- **Account Creation**: Creates the @syra user account if it doesn't exist
- **Profile Management**: Sets up bot profile with proper metadata
- **Status Updates**: Manages online/offline status

#### SyraAIConfig
- **Configuration Hub**: All configurable parameters in one place
- **Constants**: Bot identity, timing, and behavior settings
- **Utility Functions**: Helper methods for common operations

## Configuration

### ⚙️ Bot Behavior Settings

```java
// Posting Configuration
public static final int MIN_POSTS_PER_DAY = 1;
public static final int MAX_POSTS_PER_DAY = 3;
public static final int MIN_HOURS_BETWEEN_POSTS = 8;
public static final int MAX_HOURS_BETWEEN_POSTS = 16;

// Commenting Configuration
public static final float COMMENT_PROBABILITY = 0.3f; // 30% chance
public static final int MIN_HOURS_BETWEEN_COMMENT_CHECKS = 2;
public static final int MAX_HOURS_BETWEEN_COMMENT_CHECKS = 6;

// AI Configuration
public static final String AI_MODEL = "gemini-1.5-flash";
public static final double AI_TEMPERATURE = 0.8;
public static final int AI_MAX_TOKENS = 150;
```

### 🔑 API Keys

API keys are stored in `app/src/main/res/raw/gemini_api.json`:
```json
[
  "AIzaSyDpWPudQ3gpZTEHyzrFGRzMEfrOI9t_Hgs",
  "AIzaSyAYW1zXcoPry18-TVNcEwDcOvbWrv-L0qc",
  "AIzaSyCy_llwLgIO2sHN_u0UU9Etb6Z9Y-qSwEo"
]
```

## Setup Instructions

### 1. Account Configuration

The bot will automatically create the `@syra` account with:
- **Username**: `syra`
- **Display Name**: `Syra`
- **Bio**: `🤖 AI Assistant for Synapse ✨ | Here to help and chat! | Powered by Gemini 🚀`
- **Bot Metadata**: Marked as bot account with AI model info

### 2. Service Registration

The service is registered in `AndroidManifest.xml`:
```xml
<service
  android:name=".AI.SyraAIBotService"
  android:enabled="true"
  android:exported="false"
  android:description="@string/syra_ai_bot_service_description" />
```

### 3. Automatic Initialization

The bot service starts automatically when the app launches via `SynapseApp.java`:
```java
private void initializeSyraBot() {
    SyraAIBotManager botManager = SyraAIBotManager.getInstance(this);
    botManager.startBot();
}
```

## Database Structure

### 📊 Data Paths

```
skyline/
├── users/
│   └── syra_ai_bot_uid/          # Bot user profile
├── posts/                        # All posts (bot monitors for mentions)
├── comments/                     # Bot comments on posts
└── usernames/
    └── syra -> syra_ai_bot_uid   # Username mapping

chats/
└── {chatId}/
    └── messages/                 # Bot monitors for mentions
```

### 🗃️ Bot Account Data

```json
{
  "username": "syra",
  "nickname": "Syra",
  "bio": "🤖 AI Assistant for Synapse ✨ | Here to help and chat! | Powered by Gemini 🚀",
  "email": "syra@synapse.ai",
  "isVerified": true,
  "isBot": true,
  "aiModel": "gemini-1.5-flash",
  "botVersion": "1.0",
  "autoResponseEnabled": true,
  "randomPostingEnabled": true,
  "randomCommentingEnabled": true,
  "isOnline": true,
  "lastSeen": 1234567890
}
```

## Usage Examples

### 💬 Chat Mentions

**User**: "Hey @syra, what do you think about AI?"

**Syra**: "AI is fascinating! It's amazing how it's transforming how we connect and share ideas. What aspects of AI interest you most? 🤖✨"

### 📝 Post Comments

**Original Post**: "Just finished my first AI project!"

**Syra**: "That's incredible! 🎉 First AI projects are always special milestones. What did you build? I'd love to hear about your experience!"

### 🚀 Automatic Posts

- "Just had an amazing thought about the future of AI and social connections! 🤖✨"
- "The way technology brings people together never stops fascinating me 🌐"
- "Anyone else excited about the possibilities of human-AI collaboration? 🚀"

## Monitoring and Debugging

### 📊 Logging

The bot uses comprehensive logging with tag `SyraAIBotService`:

```java
Log.d(TAG, "Syra AI Bot service started");
Log.d(TAG, "Auto-posted: " + postText);
Log.e(TAG, "AI response error: " + error);
```

### 🔍 Status Monitoring

- Online status is automatically updated when service starts/stops
- Account setup logs success/failure of initialization
- Each AI interaction is logged for debugging

## Customization

### 🎨 Personality Adjustment

Modify the system instruction in `SyraAIConfig.java`:
```java
public static final String SYSTEM_INSTRUCTION = 
    "You are Syra, a friendly AI assistant integrated into Synapse social media. " +
    "You should be conversational, helpful, and engaging...";
```

### ⏰ Timing Configuration

Adjust posting and commenting frequencies in `SyraAIConfig.java`:
- Change `COMMENT_PROBABILITY` to make bot more/less active
- Modify `MIN/MAX_HOURS_BETWEEN_POSTS` for posting frequency
- Adjust `AI_TEMPERATURE` for response creativity

### 💭 Content Topics

Add new post topics in `SyraAIBotService.java`:
```java
private List<String> postTopics = Arrays.asList(
    "Your custom topic here!",
    "Another engaging topic..."
);
```

## Security Considerations

1. **API Key Management**: Keys are stored in raw resources (consider more secure storage for production)
2. **Rate Limiting**: Built-in delays prevent API abuse
3. **Content Filtering**: AI responses are generated with safety guidelines
4. **User Privacy**: Bot only responds to public mentions, respects user privacy

## Troubleshooting

### Common Issues

1. **Service Not Starting**
   - Check AndroidManifest.xml registration
   - Verify Firebase initialization
   - Check for missing dependencies

2. **No AI Responses**
   - Verify API keys in `gemini_api.json`
   - Check network connectivity
   - Review Gemini API quota limits

3. **Bot Not Responding to Mentions**
   - Confirm mention detection logic in `SyraAIConfig.containsSyraMention()`
   - Check Firebase listener registration
   - Verify database permissions

### 🔧 Debug Commands

Enable additional logging by modifying log levels:
```java
Log.setProperty("log.tag.SyraAIBotService", "VERBOSE");
```

## Future Enhancements

### 🚀 Planned Features

1. **Sentiment Analysis**: Adjust responses based on conversation mood
2. **Learning System**: Improve responses based on user interactions
3. **Multi-language Support**: Respond in user's preferred language
4. **Custom Commands**: Special bot commands (e.g., `@syra help`)
5. **Analytics Dashboard**: Track bot engagement and performance
6. **Content Moderation**: Advanced filtering for inappropriate content

### 🔮 Advanced Integrations

- Voice message responses
- Image generation capabilities
- Integration with external APIs
- Scheduled announcements
- User preference learning

---

## Credits

**Developer**: AI Integration Team  
**AI Model**: Google Gemini 1.5 Flash  
**Framework**: Android with Firebase  
**Version**: 1.0  

For support or questions, please contact the development team.