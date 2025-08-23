# 🔑 Quick Replacement Reference

## **Exact Lines to Replace**

### **1. OneSignal App ID**
**File:** `worker.js`  
**Line:** 53  
**Current:** `'044e1911-6911-4871-95f9-d60003002fe2'`  
**Replace with:** `'YOUR_ONESIGNAL_APP_ID'`

### **2. OneSignal REST API Key (Worker)**
**File:** `worker.js`  
**Line:** 54  
**Current:** `env.ONESIGNAL_REST_API_KEY`  
**Replace with:** `env.YOUR_ENV_VARIABLE_NAME` (or hardcode if needed)

### **3. OneSignal REST API Key (Test)**
**File:** `test-worker.js`  
**Line:** 7  
**Current:** `'test-key-for-testing'`  
**Replace with:** `'YOUR_ONESIGNAL_REST_API_KEY'`

### **4. Cloudflare Worker URL**
**File:** `NotificationHelper.kt`  
**Line:** 18  
**Current:** `"https://my-app-notification-sender.mashikahamed0.workers.dev"`  
**Replace with:** `"https://YOUR_WORKER_NAME.YOUR_SUBDOMAIN.workers.dev"`

### **5. ImgBB API Key (ImageUploader)**
**File:** `ImageUploader.java`  
**Line:** 88  
**Current:** `"https://api.imgbb.com/1/upload?expiration=0&key=faa85ffbac0217ff67b5f3c4baa7fb29"`  
**Replace with:** `"https://api.imgbb.com/1/upload?expiration=0&key=YOUR_IMGBB_API_KEY"`

### **6. ImgBB API Key (UploadFiles)**
**File:** `UploadFiles.java`  
**Line:** 43  
**Current:** `"faa85ffbac0217ff67b5f3c4baa7fb29"`  
**Replace with:** `"YOUR_IMGBB_API_KEY"`

### **7. GitHub Secret (CI/CD)**
**File:** `.github/workflows/android.yml`  
**Line:** 71  
**Current:** `${{ secrets.SKETCHUB_API_KEY }}`  
**Replace with:** Set this in GitHub repository secrets

---

## **Quick Copy-Paste Commands**

### **For OneSignal:**
```bash
# Replace in worker.js
sed -i 's/044e1911-6911-4871-95f9-d60003002fe2/YOUR_ONESIGNAL_APP_ID/g' worker.js

# Replace in test-worker.js  
sed -i 's/test-key-for-testing/YOUR_ONESIGNAL_REST_API_KEY/g' test-worker.js
```

### **For ImgBB:**
```bash
# Replace in ImageUploader.java
sed -i 's/faa85ffbac0217ff67b5f3c4baa7fb29/YOUR_IMGBB_API_KEY/g' app/src/main/java/com/synapse/social/studioasinc/ImageUploader.java

# Replace in UploadFiles.java
sed -i 's/faa85ffbac0217ff67b5f3c4baa7fb29/YOUR_IMGBB_API_KEY/g' app/src/main/java/com/synapse/social/studioasinc/UploadFiles.java
```

### **For Worker URL:**
```bash
# Replace in NotificationHelper.kt
sed -i 's|https://my-app-notification-sender.mashikahamed0.workers.dev|https://YOUR_WORKER_URL|g' app/src/main/java/com/synapse/social/studioasinc/NotificationHelper.kt
```

---

## **Priority Order**

1. **🔥 HIGH PRIORITY** - OneSignal keys (notifications won't work)
2. **🔥 HIGH PRIORITY** - Worker URL (notifications won't work)  
3. **⚡ MEDIUM PRIORITY** - ImgBB keys (image uploads won't work)
4. **📝 LOW PRIORITY** - GitHub secrets (CI/CD won't work)

---

## **Test After Each Replacement**

- ✅ OneSignal: Send test notification
- ✅ Worker URL: Check worker responds
- ✅ ImgBB: Upload test image
- ✅ GitHub: Verify CI/CD builds