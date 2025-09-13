package com.synapse.social.studioasinc.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Utility class to manage authentication state with backup mechanism
 * This helps ensure authentication persists even when Firebase Auth fails
 */
public final class AuthStateManager {
    
    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_IS_AUTHENTICATED = "is_authenticated";
    private static final String KEY_USER_UID = "user_uid";
    
    // Prevent instantiation
    private AuthStateManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Save authentication state to SharedPreferences as backup
     * @param context Application context
     * @param uid User UID
     */
    public static void saveAuthenticationState(Context context, String uid) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (uid == null || uid.trim().isEmpty()) {
            throw new IllegalArgumentException("UID cannot be null or empty");
        }
        
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit()
                    .putBoolean(KEY_IS_AUTHENTICATED, true)
                    .putString(KEY_USER_UID, uid)
                    .apply();
        } catch (Exception e) {
            // Log error but don't crash the app
            android.util.Log.e("AuthStateManager", "Failed to save authentication state", e);
        }
    }
    
    /**
     * Clear authentication state from SharedPreferences
     * @param context Application context
     */
    public static void clearAuthenticationState(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().clear().apply();
        } catch (Exception e) {
            // Log error but don't crash the app
            android.util.Log.e("AuthStateManager", "Failed to clear authentication state", e);
        }
    }
    
    /**
     * Check if user is authenticated (either via Firebase or backup)
     * @param context Application context
     * @return true if user is authenticated
     */
    public static boolean isUserAuthenticated(Context context) {
        if (context == null) {
            android.util.Log.w("AuthStateManager", "Context is null, returning false for authentication");
            return false;
        }
        
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                return true;
            }
            
            // Check backup authentication
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return prefs.getBoolean(KEY_IS_AUTHENTICATED, false);
        } catch (Exception e) {
            // Log error but don't crash the app
            android.util.Log.e("AuthStateManager", "Failed to check authentication state", e);
            return false;
        }
    }
    
    /**
     * Get user UID from Firebase or backup authentication
     * @param context Application context
     * @return User UID or null if not authenticated
     */
    public static String getUserUid(Context context) {
        if (context == null) {
            android.util.Log.w("AuthStateManager", "Context is null, returning null UID");
            return null;
        }
        
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                return currentUser.getUid();
            }
            
            // Try to get UID from backup authentication
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return prefs.getString(KEY_USER_UID, null);
        } catch (Exception e) {
            // Log error but don't crash the app
            android.util.Log.e("AuthStateManager", "Failed to get user UID", e);
            return null;
        }
    }
    
    /**
     * Sign out user from both Firebase and backup authentication
     * @param context Application context
     */
    public static void signOut(Context context) {
        if (context == null) {
            android.util.Log.w("AuthStateManager", "Context is null, cannot sign out");
            return;
        }
        
        try {
            FirebaseAuth.getInstance().signOut();
            clearAuthenticationState(context);
        } catch (Exception e) {
            // Log error but don't crash the app
            android.util.Log.e("AuthStateManager", "Failed to sign out", e);
        }
    }
    
    /**
     * Safely get current user UID with fallback to backup authentication
     * This method should be used instead of FirebaseAuth.getInstance().getCurrentUser().getUid()
     * @param context Application context
     * @return User UID or null if not authenticated
     */
    public static String getCurrentUserUidSafely(Context context) {
        return getUserUid(context);
    }
}