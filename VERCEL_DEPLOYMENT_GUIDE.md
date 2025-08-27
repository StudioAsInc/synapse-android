# 🚀 Vercel Backend Deployment Guide

## Overview

This guide shows how to deploy the Synapse AI Bot backend to Vercel, providing scalable serverless AI processing.

## 📁 **Project Structure**

```
vercel-backend/
├── api/
│   ├── ai/
│   │   ├── mention-response.js      # Handle @syra mentions
│   │   ├── auto-post.js            # Generate automatic posts
│   │   └── comment-generation.js   # Generate post comments
│   └── webhooks/
│       └── firebase-trigger.js     # Firebase webhooks
├── package.json                    # Dependencies
└── vercel.json                     # Vercel configuration
```

## 🔧 **Setup Steps**

### 1. **Install Vercel CLI**
```bash
npm install -g vercel
```

### 2. **Deploy to Vercel**
```bash
cd vercel-backend
vercel
```

### 3. **Set Environment Variables**
In Vercel Dashboard or via CLI:
```bash
vercel env add GEMINI_API_KEY
vercel env add SYRA_BOT_UID
vercel env add WEBHOOK_SECRET
vercel env add FIREBASE_PROJECT_ID
vercel env add FIREBASE_PRIVATE_KEY
vercel env add FIREBASE_CLIENT_EMAIL
```

**Environment Variables:**
- `GEMINI_API_KEY`: Your Google Gemini API key
- `SYRA_BOT_UID`: `DxSt08c8VfVjSQWCj3UGgMSeBVb2`
- `WEBHOOK_SECRET`: Random secure string for webhook auth
- `FIREBASE_PROJECT_ID`: Your Firebase project ID
- `FIREBASE_PRIVATE_KEY`: Firebase service account private key
- `FIREBASE_CLIENT_EMAIL`: Firebase service account email

### 4. **Update Android App**
Update `VercelAIService.java`:
```java
private static final String BASE_URL = "https://your-app-name.vercel.app/api";
```

### 5. **Configure Firebase Security Rules**
Update Firebase rules to allow Vercel backend:
```javascript
{
  "rules": {
    "skyline": {
      "posts": {
        ".read": true,
        ".write": "auth != null && auth.uid == 'DxSt08c8VfVjSQWCj3UGgMSeBVb2'"
      },
      "comments": {
        ".read": true,
        ".write": "auth != null && auth.uid == 'DxSt08c8VfVjSQWCj3UGgMSeBVb2'"
      },
      "users": {
        ".read": true,
        ".write": "auth != null"
      }
    },
    "chats": {
      ".read": "auth != null",
      ".write": "auth != null"
    }
  }
}
```

## 🔗 **API Endpoints**

### **POST /api/ai/mention-response**
Generate AI response for mentions
```json
{
  "message": "Hey @syra, how are you?",
  "userId": "user123",
  "userName": "John",
  "context": "chat",
  "botUserId": "DxSt08c8VfVjSQWCj3UGgMSeBVb2"
}
```

### **POST /api/ai/auto-post**
Generate automatic posts
```json
{
  "botUserId": "DxSt08c8VfVjSQWCj3UGgMSeBVb2",
  "postType": "morning"
}
```

### **POST /api/ai/comment-generation**
Generate post comments
```json
{
  "postContent": "Just had a great idea!",
  "authorName": "Alice",
  "commentType": "random",
  "botUserId": "DxSt08c8VfVjSQWCj3UGgMSeBVb2"
}
```

### **POST /api/webhooks/firebase-trigger**
Firebase webhook triggers
```json
{
  "eventType": "message_mention",
  "data": {
    "chatId": "chat123",
    "messageText": "Hey @syra",
    "senderId": "user123",
    "senderName": "John"
  },
  "auth": {
    "token": "your-webhook-secret"
  }
}
```

## 🔐 **Security Features**

1. **Bot Authorization**: Only authorized bot UID can use AI endpoints
2. **Webhook Authentication**: Secure token validation for webhooks
3. **CORS Protection**: Proper headers for web security
4. **Rate Limiting**: Built-in Vercel rate limiting
5. **Environment Variables**: Secure secret storage

## 📊 **Benefits of Vercel Backend**

### ✅ **Performance**
- **Edge Computing**: Low latency responses worldwide
- **Auto-scaling**: Handles traffic spikes automatically
- **CDN**: Fast static asset delivery

### ✅ **Cost Efficiency**
- **Serverless**: Pay only for actual usage
- **Free Tier**: Generous free limits for development
- **No Server Management**: Zero infrastructure costs

### ✅ **Developer Experience**
- **Git Integration**: Auto-deploy on push
- **Instant Deployments**: Fast deployment pipeline
- **Real-time Logs**: Easy debugging and monitoring

### ✅ **Reliability**
- **99.9% Uptime**: Enterprise-grade reliability
- **Global Distribution**: Multiple regions
- **Automatic Failover**: Built-in redundancy

## 🔄 **Migration Benefits**

### **Before (Android-only)**
- ❌ AI processing on every user device
- ❌ High battery usage
- ❌ Inconsistent performance
- ❌ API quota issues
- ❌ No centralized control

### **After (Vercel + Android)**
- ✅ Centralized AI processing
- ✅ Optimized battery usage
- ✅ Consistent performance
- ✅ Controlled API usage
- ✅ Easy updates and monitoring

## 📈 **Monitoring & Analytics**

### **Vercel Dashboard**
- Function execution metrics
- Error rates and logs
- Performance analytics
- Usage statistics

### **Custom Logging**
```javascript
console.log('AI Request:', { userId, messageType, timestamp });
console.error('AI Error:', error.message);
```

### **Health Checks**
```javascript
// Add to any endpoint
export default async function handler(req, res) {
  if (req.method === 'GET' && req.url === '/health') {
    return res.status(200).json({ status: 'healthy', timestamp: Date.now() });
  }
  // ... rest of function
}
```

## 🚀 **Production Deployment**

### **1. Custom Domain**
```bash
vercel domains add your-domain.com
```

### **2. Environment Setup**
```bash
# Production
vercel env add NODE_ENV production

# Staging
vercel env add NODE_ENV staging
```

### **3. Performance Optimization**
- Enable compression in `vercel.json`
- Set appropriate cache headers
- Use environment-specific configurations

### **4. Monitoring Setup**
- Set up error tracking (Sentry, etc.)
- Configure uptime monitoring
- Set up alerts for failures

## 📝 **Testing**

### **Local Development**
```bash
cd vercel-backend
vercel dev
```

### **API Testing**
```bash
# Test mention response
curl -X POST http://localhost:3000/api/ai/mention-response \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hey @syra, how are you?",
    "userId": "test123",
    "userName": "Test User",
    "context": "chat",
    "botUserId": "DxSt08c8VfVjSQWCj3UGgMSeBVb2"
  }'
```

### **Android Integration Testing**
1. Update `BASE_URL` in `VercelAIService.java`
2. Test mention responses
3. Verify auto-posting
4. Check comment generation

## 🔧 **Troubleshooting**

### **Common Issues**

1. **Environment Variables Not Set**
   ```bash
   vercel env ls
   vercel env add MISSING_VAR
   ```

2. **CORS Errors**
   - Check `vercel.json` headers configuration
   - Verify domain allowlist

3. **Function Timeouts**
   - Optimize AI prompts
   - Add request timeout handling
   - Use async/await properly

4. **API Rate Limits**
   - Implement exponential backoff
   - Add request queuing
   - Monitor usage metrics

## 🎉 **Success!**

Your Synapse AI Bot backend is now running on Vercel with:
- ✅ Scalable serverless architecture
- ✅ Global edge distribution
- ✅ Secure API endpoints
- ✅ Real-time AI processing
- ✅ Cost-effective scaling

The bot will now provide faster, more reliable AI responses while reducing load on user devices!