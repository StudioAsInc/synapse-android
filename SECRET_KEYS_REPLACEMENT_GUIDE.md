# 🔑 Secret Keys and API Endpoints Replacement Guide

## Overview

This guide shows you exactly where to replace all the secret keys, API endpoints, and configuration values in the codebase with your own values.

## 🚨 **IMPORTANT SECURITY NOTICE**

**NEVER commit your actual secret keys to version control!** 
- Use environment variables
- Use `.env` files (added to `.gitignore`)
- Use GitHub Secrets for CI/CD
- Use local properties files

## 📍 **Location 1: OneSignal Configuration**

### **File:** `worker.js` (Cloudflare Worker)
**Replace these values:**

```javascript
// Line 53: Your OneSignal App ID
const ONESIGNAL_APP_ID = 'YOUR_ONESIGNAL_APP_ID_HERE';

// Line 54: Your OneSignal REST API Key (from environment variable)
const ONESIGNAL_REST_API_KEY = env.ONESIGNAL_REST_API_KEY;
```

**How to get these values:**
1. Go to [OneSignal Dashboard](https://app.onesignal.com/)
2. Select your app
3. Go to **Settings** → **Keys & IDs**
4. Copy **App ID** and **REST API Key**

### **File:** `test-worker.js` (Testing)
**Replace this value:**

```javascript
// Line 7: Your OneSignal REST API Key for testing
ONESIGNAL_REST_API_KEY: 'YOUR_ONESIGNAL_REST_API_KEY_HERE'
```

## 📍 **Location 2: Cloudflare Worker URL**

### **File:** `NotificationHelper.kt`
**Replace this value:**

```kotlin
// Line 18: Your Cloudflare Worker URL
private const val WORKER_URL = "https://YOUR_WORKER_SUBDOMAIN.YOUR_SUBDOMAIN.workers.dev"
```

**How to get this:**
1. Deploy your worker to Cloudflare
2. Your worker URL will be: `https://YOUR_WORKER_NAME.YOUR_SUBDOMAIN.workers.dev`
3. Or use a custom domain if configured

## 📍 **Location 3: Image Upload API (ImgBB)**

### **File:** `ImageUploader.java`
**Replace this value:**

```java
// Line 88: Your ImgBB API key
URL url = new URL("https://api.imgbb.com/1/upload?expiration=0&key=YOUR_IMGBB_API_KEY_HERE");
```

### **File:** `UploadFiles.java`
**Replace this value:**

```java
// Line 43: Your ImgBB API key
private static final String IMGBB_API_KEY = "YOUR_IMGBB_API_KEY_HERE";
```

**How to get this:**
1. Go to [ImgBB](https://imgbb.com/)
2. Sign up/Login
3. Go to **API** section
4. Generate your API key

## 📍 **Location 4: GitHub Actions Secrets**

### **File:** `.github/workflows/android.yml`
**Replace this value:**

```yaml
# Line 71: Your Sketchub API key for CI/CD
SKETCHUB_API_KEY: ${{ secrets.SKETCHUB_API_KEY }}
```

**How to set this:**
1. Go to your GitHub repository
2. **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Name: `SKETCHUB_API_KEY`
5. Value: Your actual API key

## 📍 **Location 5: Firebase Configuration**

### **File:** `google-services.json`
**This file should already contain your Firebase configuration:**
- Project ID
- Client ID
- API Key
- Database URL

**If you need to replace it:**
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. **Project Settings** → **General**
4. Download `google-services.json`
5. Replace the existing file

## 📍 **Location 6: Environment Variables**

### **For Local Development:**
Create a `.env` file in your project root:

```bash
# .env
ONESIGNAL_APP_ID=your_onesignal_app_id
ONESIGNAL_REST_API_KEY=your_onesignal_rest_api_key
IMGBB_API_KEY=your_imgbb_api_key
SKETCHUB_API_KEY=your_sketchub_api_key
WORKER_URL=https://your-worker.your-subdomain.workers.dev
```

### **For Production:**
Set these in your deployment environment:
- Cloudflare Workers environment variables
- Android app signing configuration
- CI/CD secrets

## 🔧 **Step-by-Step Replacement Process**

### **Step 1: OneSignal Setup**
1. Create OneSignal account
2. Create new app
3. Get App ID and REST API Key
4. Replace in `worker.js` and `test-worker.js`

### **Step 2: Cloudflare Worker Setup**
1. Create Cloudflare account
2. Deploy the `worker.js` file
3. Get your worker URL
4. Replace in `NotificationHelper.kt`

### **Step 3: Image Upload Setup**
1. Create ImgBB account
2. Get API key
3. Replace in `ImageUploader.java` and `UploadFiles.java`

### **Step 4: GitHub Secrets Setup**
1. Go to repository settings
2. Add `SKETCHUB_API_KEY` secret
3. Verify CI/CD works

### **Step 5: Test Configuration**
1. Build the app
2. Test image uploads
3. Test push notifications
4. Verify all services work

## 🛡️ **Security Best Practices**

### **Do's:**
✅ Use environment variables
✅ Use GitHub Secrets for CI/CD
✅ Use `.env` files (add to `.gitignore`)
✅ Rotate API keys regularly
✅ Use least privilege access

### **Don'ts:**
❌ Never commit real keys to git
❌ Don't share keys in public repositories
❌ Don't use hardcoded keys in production
❌ Don't log keys in console output

## 📋 **Checklist for Replacement**

- [ ] OneSignal App ID in `worker.js`
- [ ] OneSignal REST API Key in `worker.js`
- [ ] OneSignal REST API Key in `test-worker.js`
- [ ] Cloudflare Worker URL in `NotificationHelper.kt`
- [ ] ImgBB API Key in `ImageUploader.java`
- [ ] ImgBB API Key in `UploadFiles.java`
- [ ] GitHub Secret `SKETCHUB_API_KEY`
- [ ] Firebase configuration in `google-services.json`
- [ ] Test all services after replacement
- [ ] Verify CI/CD pipeline works

## 🚀 **Deployment Checklist**

### **Before Deploying:**
1. All keys replaced with your values
2. Environment variables set
3. GitHub secrets configured
4. Cloudflare worker deployed
5. Firebase project configured

### **After Deploying:**
1. Test push notifications
2. Test image uploads
3. Verify CI/CD builds
4. Check error logs
5. Monitor API usage

## 🔍 **Troubleshooting**

### **Common Issues:**
1. **Keys not working**: Check if keys are correctly copied
2. **Worker not responding**: Verify Cloudflare worker URL
3. **Images not uploading**: Check ImgBB API key
4. **Notifications not sending**: Verify OneSignal configuration
5. **CI/CD failing**: Check GitHub secrets

### **Debug Steps:**
1. Enable debug logging in `NotificationConfig.java`
2. Check Cloudflare worker logs
3. Verify Firebase connectivity
4. Test API endpoints individually
5. Review error messages in logs

## 📞 **Support Resources**

- **OneSignal**: [Documentation](https://documentation.onesignal.com/)
- **Cloudflare Workers**: [Documentation](https://developers.cloudflare.com/workers/)
- **ImgBB**: [API Documentation](https://api.imgbb.com/)
- **Firebase**: [Documentation](https://firebase.google.com/docs)
- **GitHub Actions**: [Documentation](https://docs.github.com/en/actions)

## ⚠️ **Important Notes**

1. **Keep your keys secure** - never share them publicly
2. **Test thoroughly** - verify all services work after replacement
3. **Monitor usage** - track API calls and costs
4. **Backup configuration** - keep a secure copy of your keys
5. **Regular rotation** - change keys periodically for security

---

**Remember:** This is a one-time setup process. Once configured correctly, your app will work with your own services and won't need further key replacements unless you change providers.