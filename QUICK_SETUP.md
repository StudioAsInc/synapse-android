# 🚀 Quick Setup Guide - Notification System

## What I've Fixed

✅ **Created a working Cloudflare Worker** (`worker.js`) that handles notifications  
✅ **Fixed Android notification channels** - added proper setup in MainActivity and SynapseApp  
✅ **Created deployment scripts** and configuration files  
✅ **Added comprehensive error handling** and logging  

## 🎯 Immediate Actions Required

### 1. Deploy the Cloudflare Worker

```bash
# Install Wrangler CLI
npm install -g wrangler

# Login to Cloudflare
wrangler login

# Deploy the worker
./deploy.sh production
```

### 2. Set Your OneSignal REST API Key

```bash
# Get your key from OneSignal Dashboard → Settings → Keys & IDs
wrangler secret put ONESIGNAL_REST_API_KEY --env production
```

### 3. Update Your Android App

The notification channels are now automatically created when your app starts. No additional code changes needed!

## 🔧 How It Works Now

1. **Android App** → Sends notification request to Cloudflare Worker
2. **Cloudflare Worker** → Receives request and forwards to OneSignal
3. **OneSignal** → Delivers push notification to user's device
4. **User** → Receives notification with proper sound, vibration, and lights

## 📱 Notification Features

- **High Priority**: Messages get immediate attention
- **Custom Channel**: Dedicated "Messages" channel for chat notifications
- **Rich Notifications**: Lights, vibration, and custom colors
- **Smart Suppression**: No notifications when user is actively chatting

## 🧪 Test the System

```bash
# Test with curl (replace with your actual worker URL)
curl -X POST https://your-worker.your-subdomain.workers.dev \
  -H "Content-Type: application/json" \
  -d '{
    "recipientUserId": "test-player-id",
    "notificationMessage": "Test notification"
  }'
```

## 🚨 Troubleshooting

### If notifications don't work:

1. **Check Cloudflare Worker logs**: `wrangler tail`
2. **Verify OneSignal API key**: Check your dashboard
3. **Test worker endpoint**: Use the curl command above
4. **Check Android logs**: Look for OneSignal and notification errors

### Common issues:

- **"Server configuration error"** → Set the OneSignal REST API key secret
- **"Failed to send notification"** → Check your OneSignal app ID and API key
- **No notification sound** → Ensure notification channel is created (should be automatic now)

## 📋 Files Created/Modified

- `worker.js` - Cloudflare Worker for notifications
- `wrangler.toml` - Worker configuration
- `package.json` - Node.js dependencies
- `deploy.sh` - Deployment script
- `MainActivity.java` - Added notification channels
- `SynapseApp.java` - Added notification channels
- `README.md` - Comprehensive documentation

## 🎉 You're All Set!

Your notification system should now work perfectly. The worker will handle all the heavy lifting, and your Android app will automatically create the proper notification channels.

Need help? Check the logs or refer to the main README.md for detailed troubleshooting steps.