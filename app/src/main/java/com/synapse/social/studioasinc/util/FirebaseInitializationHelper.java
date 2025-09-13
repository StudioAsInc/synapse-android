package com.synapse.social.studioasinc.util;

import android.content.Context;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Helper class to ensure Firebase is properly initialized before use.
 * This prevents crashes when Firebase services are accessed too early.
 */
public class FirebaseInitializationHelper {
    
    private static final String TAG = "FirebaseInitHelper";
    
    /**
     * Ensures Firebase is properly initialized and ready for use.
     * @param context Application context
     * @return true if Firebase is ready, false otherwise
     */
    public static boolean ensureFirebaseInitialized(Context context) {
        try {
            // Check if Firebase app is initialized
            FirebaseApp app = FirebaseApp.getInstance();
            if (app == null) {
                Log.e(TAG, "Firebase app is not initialized");
                return false;
            }
            
            // Check if Firebase Auth is available
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth == null) {
                Log.e(TAG, "Firebase Auth is not available");
                return false;
            }
            
            // Check if Firebase Database is available
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if (database == null) {
                Log.e(TAG, "Firebase Database is not available");
                return false;
            }
            
            Log.d(TAG, "Firebase is properly initialized and ready");
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error checking Firebase initialization", e);
            return false;
        }
    }
    
    /**
     * Waits for Firebase to be initialized with a callback.
     * @param context Application context
     * @param callback Callback to execute when Firebase is ready
     * @param maxRetries Maximum number of retry attempts
     */
    public static void waitForFirebaseInitialization(Context context, Runnable callback, int maxRetries) {
        waitForFirebaseInitializationInternal(context, callback, maxRetries, 0);
    }
    
    private static void waitForFirebaseInitializationInternal(Context context, Runnable callback, int maxRetries, int currentRetry) {
        if (ensureFirebaseInitialized(context)) {
            // Firebase is ready, execute callback
            callback.run();
        } else if (currentRetry < maxRetries) {
            // Firebase not ready, retry after delay
            Log.w(TAG, "Firebase not ready, retrying... (" + (currentRetry + 1) + "/" + maxRetries + ")");
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                waitForFirebaseInitializationInternal(context, callback, maxRetries, currentRetry + 1);
            }, 500); // 500ms delay between retries
        } else {
            // Max retries reached, log error
            Log.e(TAG, "Firebase initialization timeout after " + maxRetries + " retries");
        }
    }
}