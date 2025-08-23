# Notification Worker for Cloudflare Workers

This Cloudflare Worker handles push notifications for your Android app by forwarding requests to OneSignal's REST API.

## Features

- ✅ Receives notification requests from your Android app
- ✅ Forwards notifications to OneSignal for delivery
- ✅ Handles CORS properly
- ✅ Comprehensive error handling and logging
- ✅ Environment-based configuration
- ✅ High-priority message notifications

## Prerequisites

1. **Cloudflare Account**: You need a Cloudflare account with Workers enabled
2. **OneSignal Account**: You need a OneSignal account with your app configured
3. **OneSignal REST API Key**: Get this from your OneSignal dashboard

## Setup Instructions

### 1. Install Wrangler CLI

```bash
npm install -g wrangler
```

### 2. Login to Cloudflare

```bash
wrangler login
```

### 3. Get Your OneSignal REST API Key

1. Go to your OneSignal dashboard
2. Navigate to Settings → Keys & IDs
3. Copy the "REST API Key"

### 4. Set Environment Variables

Set your OneSignal REST API key as a secret:

```bash
# For production
wrangler secret put ONESIGNAL_REST_API_KEY --env production

# For staging
wrangler secret put ONESIGNAL_REST_API_KEY --env staging
```

### 5. Deploy the Worker

```bash
# Deploy to production
npm run deploy:production

# Deploy to staging
npm run deploy:staging

# Or deploy to default environment
npm run deploy
```

## Configuration

### Environment Variables

- `ONESIGNAL_REST_API_KEY`: Your OneSignal REST API key (set as secret)

### OneSignal App ID

The worker is configured to use the OneSignal App ID: `044e1911-6911-4871-95f9-d60003002fe2`

If you need to change this, update the `ONESIGNAL_APP_ID` constant in `worker.js`.

## Usage

### API Endpoint

The worker accepts POST requests to `/` with the following JSON payload:

```json
{
  "recipientUserId": "onesignal-player-id",
  "notificationMessage": "Your message here"
}
```

### Response Format

**Success Response:**
```json
{
  "success": true,
  "message": "Notification sent successfully",
  "oneSignalId": "notification-id-from-onesignal"
}
```

**Error Response:**
```json
{
  "error": "Error description",
  "details": "Additional error details"
}
```

## Testing

### Local Development

```bash
npm run dev
```

This will start a local development server where you can test your worker.

### Test with curl

```bash
curl -X POST http://localhost:8787 \
  -H "Content-Type: application/json" \
  -d '{
    "recipientUserId": "test-player-id",
    "notificationMessage": "Test notification"
  }'
```

## Customization

### Notification Appearance

You can customize the notification appearance by modifying the `notificationPayload` object in `worker.js`:

- `small_icon`: Small notification icon
- `large_icon`: Large notification icon
- `android_accent_color`: Accent color for Android
- `android_sound`: Custom notification sound
- `android_channel_id`: Custom notification channel

### Notification Channel

Make sure your Android app has a notification channel with ID `messages`. You can add this to your Android app:

```kotlin
// In your MainActivity or Application class
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        "messages",
        "Messages",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Chat message notifications"
        enableLights(true)
        lightColor = Color.RED
        enableVibration(true)
    }
    
    val notificationManager = getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
}
```

## Troubleshooting

### Common Issues

1. **"Server configuration error"**: Make sure you've set the `ONESIGNAL_REST_API_KEY` secret
2. **"Failed to send notification via OneSignal"**: Check your OneSignal REST API key and app ID
3. **CORS errors**: The worker handles CORS automatically, but make sure your client is sending the right headers

### Logs

Check your Cloudflare Workers logs in the Cloudflare dashboard or using:

```bash
wrangler tail
```

## Security

- The worker only accepts POST requests
- Input validation is performed on all requests
- CORS is properly configured
- API keys are stored as secrets, not in code

## Support

If you encounter issues:

1. Check the Cloudflare Workers logs
2. Verify your OneSignal configuration
3. Ensure your Android app is properly configured for notifications
4. Check that the notification channel exists in your Android app

## License

MIT License - feel free to modify and use as needed.
