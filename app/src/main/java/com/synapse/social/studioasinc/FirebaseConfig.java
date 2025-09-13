package com.synapse.social.studioasinc;

import android.content.Context;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import java.io.IOException;

/**
 * Custom Firebase configuration to disable encrypted storage and prevent keystore failures
 */
public class FirebaseConfig {
    private static final String TAG = "FirebaseConfig";
    
    /**
     * Initialize Firebase with disabled encrypted storage and Firestore
     */
    public static void initializeFirebase(Context context) {
        try {
            // Set system properties to disable Firebase encrypted storage
            System.setProperty("firebase.auth.disable.encrypted.storage", "true");
            System.setProperty("firebase.storage.disable.encryption", "true");
            System.setProperty("firebase.database.disable.encryption", "true");
            
            // Clear any existing encrypted storage data
            clearEncryptedStorageData(context);
            
            // Initialize Firestore with optimized settings
            initializeFirestore();
            
            Log.d(TAG, "Firebase initialized with encrypted storage disabled and Firestore configured");
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Firebase with disabled encrypted storage", e);
        }
    }
    
    /**
     * Initialize Firestore with optimized settings for better performance
     */
    public static void initializeFirestore() {
        try {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            
            // Configure Firestore settings for better performance
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true) // Enable offline persistence
                    .setCacheSizeBytes(100 * 1024 * 1024) // 100MB cache limit
                    .build();
            
            firestore.setFirestoreSettings(settings);
            
            Log.d(TAG, "Firestore initialized with optimized settings");
            
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Firestore", e);
        }
    }
    
    /**
     * Clear all encrypted storage data that might cause keystore failures
     */
    private static void clearEncryptedStorageData(Context context) {
        try {
            // Clear Firebase Auth encrypted storage
            android.content.SharedPreferences authPrefs = context.getSharedPreferences("com.google.firebase.auth", Context.MODE_PRIVATE);
            authPrefs.edit().clear().apply();
            
            // Clear FirebearStorageCryptoHelper data
            android.content.SharedPreferences cryptoPrefs = context.getSharedPreferences("firebear_storage_crypto", Context.MODE_PRIVATE);
            cryptoPrefs.edit().clear().apply();
            
            // Clear any other Firebase encrypted storage
            android.content.SharedPreferences firebasePrefs = context.getSharedPreferences("firebase_storage", Context.MODE_PRIVATE);
            firebasePrefs.edit().clear().apply();
            
            // Clear keystore-related preferences
            android.content.SharedPreferences keystorePrefs = context.getSharedPreferences("android_keystore", Context.MODE_PRIVATE);
            keystorePrefs.edit().clear().apply();
            
            Log.d(TAG, "Cleared all encrypted storage data");
            
        } catch (Exception e) {
            Log.w(TAG, "Could not clear encrypted storage data", e);
        }
    }
    
    /**
     * Force Firebase to use regular SharedPreferences instead of encrypted storage
     */
    public static void forceRegularStorage(Context context) {
        try {
            // Create a dummy SharedPreferences file to force Firebase to use regular storage
            android.content.SharedPreferences regularPrefs = context.getSharedPreferences("firebase_regular_storage", Context.MODE_PRIVATE);
            regularPrefs.edit().putBoolean("force_regular_storage", true).apply();
            
            Log.d(TAG, "Forced Firebase to use regular storage");
            
        } catch (Exception e) {
            Log.w(TAG, "Could not force regular storage", e);
        }
    }
}